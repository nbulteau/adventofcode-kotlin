package me.nicolas.adventofcode.year2025

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToLong

/**
 * A custom Integer Linear Programming (ILP) solver.
 *
 * This solver is designed to solve problems of the form:
 *   Minimize: sum(x_i)
 *   Subject to:
 *     - A * x = b
 *     - x_i >= 0
 *     - x_i are integers
 *
 * It uses a combination of techniques:
 * 1.  **Gaussian Elimination**: To simplify the system of equations A * x = b into row echelon form.
 *     This separates variables into pivot variables and free variables.
 * 2.  **Branch and Bound**: To find an integer solution for the free variables. It recursively explores
 *     the solution space, pruning branches that cannot yield a better solution than the one already found.
 * 3.  **LP Solver (Simplex-like)**: At each node of the branch and bound tree, a Linear Programming (LP)
 *     relaxation is solved by checking vertices of the feasible region to find a lower bound for the objective.
 *
 * Note: This is a simplified, educational implementation and may not be as robust or performant as
 * commercial-grade solvers like Gurobi or open-source ones like GLPK.
 */
class LinearSolver {

    private val epsilon = 1e-9

    // Solves Ax = b, minimizes sum(x), subject to x >= 0, integers.
    fun solve(
        numberOfEquations: Int,
        numberOfVariables: Int,
        coefficientMatrix: Array<DoubleArray>,
        constantVector: DoubleArray,
    ): Long {
        if (numberOfVariables == 0) {
            return if (constantVector.all { abs(it) < epsilon }) 0L else Long.MAX_VALUE
        }

        // --- Gaussian Elimination with partial pivoting ---
        val workingMatrix = Array(numberOfEquations) { index -> coefficientMatrix[index].clone() }
        val rightHandSide = constantVector.clone()

        val pivotColumns = mutableListOf<Int>()
        val freeColumns = mutableListOf<Int>()
        val pivotColumnToRowMap = mutableMapOf<Int, Int>()

        var currentRow = 0
        var currentColumn = 0
        while (currentRow < numberOfEquations && currentColumn < numberOfVariables) {
            // Find pivot in currentColumn
            var pivotRowIndex = currentRow
            var maxAbsoluteValue = abs(workingMatrix[currentRow][currentColumn])
            for (rowIndex in currentRow + 1 until numberOfEquations) {
                if (abs(workingMatrix[rowIndex][currentColumn]) > maxAbsoluteValue) {
                    maxAbsoluteValue = abs(workingMatrix[rowIndex][currentColumn])
                    pivotRowIndex = rowIndex
                }
            }

            if (maxAbsoluteValue < epsilon) {
                freeColumns.add(currentColumn)
                currentColumn++
            } else {
                // Swap rows
                if (pivotRowIndex != currentRow) {
                    val tempRow = workingMatrix[currentRow]
                    workingMatrix[currentRow] = workingMatrix[pivotRowIndex]
                    workingMatrix[pivotRowIndex] = tempRow
                    val tempValue = rightHandSide[currentRow]
                    rightHandSide[currentRow] = rightHandSide[pivotRowIndex]
                    rightHandSide[pivotRowIndex] = tempValue
                }

                // Normalize pivot row
                val pivotValue = workingMatrix[currentRow][currentColumn]
                for (colIndex in currentColumn until numberOfVariables) workingMatrix[currentRow][colIndex] /= pivotValue
                rightHandSide[currentRow] /= pivotValue

                // Eliminate other entries in the pivot column
                for (rowIndex in 0 until numberOfEquations) {
                    if (rowIndex != currentRow) {
                        val eliminationFactor = workingMatrix[rowIndex][currentColumn]
                        if (abs(eliminationFactor) > epsilon) {
                            for (colIndex in currentColumn until numberOfVariables) workingMatrix[rowIndex][colIndex] -= eliminationFactor * workingMatrix[currentRow][colIndex]
                            rightHandSide[rowIndex] -= eliminationFactor * rightHandSide[currentRow]
                        }
                    }
                }
                pivotColumns.add(currentColumn)
                pivotColumnToRowMap[currentColumn] = currentRow
                currentRow++
                currentColumn++
            }
        }
        // Remaining columns are free
        while (currentColumn < numberOfVariables) {
            freeColumns.add(currentColumn)
            currentColumn++
        }

        // Check for inconsistency in the remaining rows
        for (rowIndex in currentRow until numberOfEquations) {
            if (abs(rightHandSide[rowIndex]) > epsilon) return Long.MAX_VALUE // Inconsistent system
        }

        // --- ILP Solver using Branch & Bound ---
        val numberOfFreeVariables = freeColumns.size

        // If no free variables, the solution is unique. Check if it's a valid integer solution.
        if (numberOfFreeVariables == 0) {
            var sum = 0L
            for (pivotColumn in pivotColumns) {
                val row = pivotColumnToRowMap[pivotColumn]!!
                val variableValue = rightHandSide[row]
                if (variableValue < -epsilon) return Long.MAX_VALUE // Negative value
                val roundedValue = variableValue.roundToLong()
                if (abs(variableValue - roundedValue) > epsilon) return Long.MAX_VALUE // Not an integer
                sum += roundedValue
            }
            return sum
        }

        var bestObjectiveValue = Double.MAX_VALUE

        // Objective function in terms of free variables:
        // min sum(x_p) + sum(x_f) = min (baseObjectiveValue + sum(objectiveCoefficients * x_f))
        val baseObjectiveValue = pivotColumns.sumOf { pc -> rightHandSide[pivotColumnToRowMap[pc]!!] }
        val objectiveCoefficients = DoubleArray(numberOfFreeVariables) { freeVarIndex ->
            val freeColumn = freeColumns[freeVarIndex]
            1.0 - pivotColumns.sumOf { pc -> workingMatrix[pivotColumnToRowMap[pc]!!][freeColumn] }
        }

        // Constraints for pivot variables (x_p >= 0):
        // x_p = rhs_row - sum(mat[row][f] * x_f) >= 0  =>  sum(mat[row][f] * x_f) <= rhs_row
        val constraintCoefficients = mutableListOf<DoubleArray>()
        val constraintConstants = mutableListOf<Double>()
        for (pivotColumn in pivotColumns) {
            val row = pivotColumnToRowMap[pivotColumn]!!
            val coeffs = DoubleArray(numberOfFreeVariables) { freeVarIndex -> workingMatrix[row][freeColumns[freeVarIndex]] }
            if (coeffs.any { abs(it) > epsilon }) {
                constraintCoefficients.add(coeffs)
                constraintConstants.add(rightHandSide[row])
            } else if (rightHandSide[row] < -epsilon) {
                return Long.MAX_VALUE // Impossible constraint
            }
        }

        // Recursive search function for Branch & Bound
        fun search(lowerBounds: DoubleArray, upperBounds: DoubleArray) {
            // Solve LP relaxation to get a lower bound
            val (lpObjectiveValue, lpSolution) = solveLpByVertexEnumeration(
                numberOfFreeVariables,
                objectiveCoefficients,
                constraintCoefficients,
                constraintConstants,
                lowerBounds,
                upperBounds
            )

            if (lpObjectiveValue == Double.MAX_VALUE) return // Infeasible branch
            if (baseObjectiveValue + lpObjectiveValue >= bestObjectiveValue - epsilon) return // Prune branch

            // Find the first free variable that is not an integer
            val fractionalFreeVariableIndex = lpSolution.indexOfFirst { abs(it - it.roundToLong()) > epsilon }

            if (fractionalFreeVariableIndex == -1) { // All free variables are integers
                // Now check pivot variables for integrality
                var firstFractionalPivotRow = -1
                var firstFractionalPivotValue = 0.0
                for (pc in pivotColumns) {
                    val row = pivotColumnToRowMap[pc]!!
                    val pivotValue =
                        rightHandSide[row] - (0 until numberOfFreeVariables).sumOf { freeVarIndex -> workingMatrix[row][freeColumns[freeVarIndex]] * lpSolution[freeVarIndex] }
                    if (abs(pivotValue - pivotValue.roundToLong()) > epsilon) {
                        firstFractionalPivotValue = pivotValue
                        firstFractionalPivotRow = row
                        break
                    }
                }

                if (firstFractionalPivotRow == -1) { // All variables are integers, found a valid solution
                    val currentObjective = baseObjectiveValue + lpObjectiveValue
                    if (currentObjective < bestObjectiveValue) {
                        bestObjectiveValue = currentObjective
                    }
                } else { // Branch on a fractional pivot variable
                    val floorValue = floor(firstFractionalPivotValue)
                    val ceilValue = ceil(firstFractionalPivotValue)
                    val coeffs = DoubleArray(numberOfFreeVariables) { freeVarIndex -> workingMatrix[firstFractionalPivotRow][freeColumns[freeVarIndex]] }

                    // Branch 1: p <= floor(val) => sum(coeffs*f) >= rhs - floor
                    constraintCoefficients.add(coeffs.map { -it }.toDoubleArray())
                    constraintConstants.add(-(rightHandSide[firstFractionalPivotRow] - floorValue))
                    search(lowerBounds, upperBounds)
                    constraintCoefficients.removeAt(constraintCoefficients.lastIndex)
                    constraintConstants.removeAt(constraintConstants.lastIndex)

                    // Branch 2: p >= ceil(val) => sum(coeffs*f) <= rhs - ceil
                    constraintCoefficients.add(coeffs)
                    constraintConstants.add(rightHandSide[firstFractionalPivotRow] - ceilValue)
                    search(lowerBounds, upperBounds)
                    constraintCoefficients.removeAt(constraintCoefficients.lastIndex)
                    constraintConstants.removeAt(constraintConstants.lastIndex)
                }
            } else { // Branch on a fractional free variable
                val freeVariableValue = lpSolution[fractionalFreeVariableIndex]
                val floorValue = floor(freeVariableValue)
                val ceilValue = ceil(freeVariableValue)

                // Branch 1: x_f <= floor(val)
                val previousUpperBound = upperBounds[fractionalFreeVariableIndex]
                upperBounds[fractionalFreeVariableIndex] = floorValue
                search(lowerBounds, upperBounds)
                upperBounds[fractionalFreeVariableIndex] = previousUpperBound

                // Branch 2: x_f >= ceil(val)
                val previousLowerBound = lowerBounds[fractionalFreeVariableIndex]
                lowerBounds[fractionalFreeVariableIndex] = ceilValue
                search(lowerBounds, upperBounds)
                lowerBounds[fractionalFreeVariableIndex] = previousLowerBound
            }
        }

        // Start the search with initial bounds [0, infinity)
        search(DoubleArray(numberOfFreeVariables) { 0.0 }, DoubleArray(numberOfFreeVariables) { Double.MAX_VALUE })

        return if (bestObjectiveValue >= Double.MAX_VALUE - 1.0) Long.MAX_VALUE else bestObjectiveValue.roundToLong()
    }

    // Simple LP solver: Min c*x subject to A*x <= b and bounds.
    // It works by checking all vertices of the feasible region.
    private fun solveLpByVertexEnumeration(
        numberOfVariables: Int,
        objectiveCoefficients: DoubleArray,
        constraintCoefficients: List<DoubleArray>,
        constraintConstants: List<Double>,
        lowerBounds: DoubleArray,
        upperBounds: DoubleArray,
    ): Pair<Double, DoubleArray> {
        if (numberOfVariables == 0) return 0.0 to DoubleArray(0)

        var bestObjectiveValue = Double.MAX_VALUE
        var bestSolution = DoubleArray(numberOfVariables)
        var solutionFound = false

        // Combine all constraints (Ax<=b and bounds)
        val allConstraintCoefficients = mutableListOf<DoubleArray>()
        val allConstraintConstants = mutableListOf<Double>()
        allConstraintCoefficients.addAll(constraintCoefficients)
        allConstraintConstants.addAll(constraintConstants)

        for (variableIndex in 0 until numberOfVariables) {
            // f_j >= L  =>  -f_j <= -L
            val rowL = DoubleArray(numberOfVariables).also { it[variableIndex] = -1.0 }
            allConstraintCoefficients.add(rowL)
            allConstraintConstants.add(-lowerBounds[variableIndex])
            // f_j <= U
            if (upperBounds[variableIndex] < Double.MAX_VALUE) {
                val rowU = DoubleArray(numberOfVariables).also { it[variableIndex] = 1.0 }
                allConstraintCoefficients.add(rowU)
                allConstraintConstants.add(upperBounds[variableIndex])
            }
        }

        val numberOfConstraints = allConstraintCoefficients.size
        if (numberOfConstraints < numberOfVariables) return Double.MAX_VALUE to bestSolution

        // Iterate through all combinations of nVars constraints to find vertices
        val indices = IntArray(numberOfVariables)
        fun findVertices(start: Int, depth: Int) {
            if (depth == numberOfVariables) {
                val matrix = Array(numberOfVariables) { rowIndex -> allConstraintCoefficients[indices[rowIndex]] }
                val vector = DoubleArray(numberOfVariables) { rowIndex -> allConstraintConstants[indices[rowIndex]] }
                val solution = solveSquareLinearSystem(numberOfVariables, matrix, vector)

                if (solution != null) {
                    // Check if the solution is feasible against all constraints
                    val isFeasible = (0 until numberOfConstraints).all { constraintIndex ->
                        val (coeffs, constant) = allConstraintCoefficients[constraintIndex] to allConstraintConstants[constraintIndex]
                        (0 until numberOfVariables).sumOf { variableIndex -> coeffs[variableIndex] * solution[variableIndex] } < constant + epsilon
                    }

                    if (isFeasible) {
                        val objectiveValue = (0 until numberOfVariables).sumOf { variableIndex -> objectiveCoefficients[variableIndex] * solution[variableIndex] }
                        if (objectiveValue < bestObjectiveValue) {
                            bestObjectiveValue = objectiveValue
                            bestSolution = solution
                            solutionFound = true
                        }
                    }
                }
                return
            }
            for (constraintIndex in start until numberOfConstraints) {
                indices[depth] = constraintIndex
                findVertices(constraintIndex + 1, depth + 1)
            }
        }

        findVertices(0, 0)

        return if (!solutionFound) Double.MAX_VALUE to bestSolution else bestObjectiveValue to bestSolution
    }

    // Solves a square linear system Ax = b using Gaussian elimination.
    private fun solveSquareLinearSystem(size: Int, matrix: Array<DoubleArray>, vector: DoubleArray): DoubleArray? {
        val workingMatrix = matrix.map { it.clone() }.toTypedArray()
        val rightHandSide = vector.clone()
        for (pivotIndex in 0 until size) {
            // Find pivot
            var pivotRow = pivotIndex
            for (rowIndex in pivotIndex + 1 until size) {
                if (abs(workingMatrix[rowIndex][pivotIndex]) > abs(workingMatrix[pivotRow][pivotIndex])) {
                    pivotRow = rowIndex
                }
            }
            if (abs(workingMatrix[pivotRow][pivotIndex]) < epsilon) return null // Singular matrix

            // Swap rows to bring pivot to current row
            workingMatrix[pivotIndex] = workingMatrix[pivotRow].also { workingMatrix[pivotRow] = workingMatrix[pivotIndex] }
            rightHandSide[pivotIndex] = rightHandSide[pivotRow].also { rightHandSide[pivotRow] = rightHandSide[pivotIndex] }

            // Normalize the pivot row
            val pivotValue = workingMatrix[pivotIndex][pivotIndex]
            for (colIndex in pivotIndex until size) {
                workingMatrix[pivotIndex][colIndex] /= pivotValue
            }
            rightHandSide[pivotIndex] /= pivotValue

            // Eliminate other entries in the pivot column
            for (rowIndex in 0 until size) {
                if (rowIndex != pivotIndex) {
                    val eliminationFactor = workingMatrix[rowIndex][pivotIndex]
                    for (colIndex in pivotIndex until size) {
                        workingMatrix[rowIndex][colIndex] -= eliminationFactor * workingMatrix[pivotIndex][colIndex]
                    }
                    rightHandSide[rowIndex] -= eliminationFactor * rightHandSide[pivotIndex]
                }
            }
        }
        return rightHandSide
    }
}