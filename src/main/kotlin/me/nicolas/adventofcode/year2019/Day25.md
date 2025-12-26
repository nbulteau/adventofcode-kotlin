--- Discovered map (grid-aligned) ---
Directions: north ↑ (up), south ↓ (down), east ← (left), west → (right)
Legend: * = collected   X = blacklisted

Grid (rows top→down, cols left→right). Cells may contain multiple rooms where the map forces overlap.

| Row | Col 0 | Col 1 | Col 2                                  | Col 3                      | Col 4 |
| --- | --- | --- |----------------------------------------|----------------------------| --- |
| Row 0 |  |  |                                        |                            | Gift Wrapping Ctr. (X) |
| Row 1 |  | Storage (photons X) |                                        | Corridor (infinite loop X) | Science Lab (*) |
| Row 2 | Navigation (*) | Crew Quarters (giant electromagnet X) | Sick Bay (sand*) / Security Checkpoint | Kitchen                    | Warp Drive Maintenance (spool*) / Observatory (*) |
| Row 3 | Stables | Holodeck (*) | Arcade (festive hat *)                 | Engineering | Engineering |
| Row 4 |  |  |                                        | Hallway                    | Hull Breach |
| Row 5 |  |  |                                        | Passages                |  |

Notes:
- Connections in this grid obey: north = up, south = down, east = left, west = right.
  Example: "Science east -> Corridor" is represented with Corridor to the left of Science.
- Some rooms overlap in the same cell where the original room links force them to share coordinates (e.g., Sick Bay and Security Checkpoint).
- Collected items: asterisk, festive hat, monolith, mug, prime number, sand, spool of cat6, tambourine
- Blacklisted items: escape pod, giant electromagnet, infinite loop, molten lava, photons

Room: Arcade
Items: festive hat
east -> Holodeck
north -> Security Checkpoint

Room: Corridor
Items: infinite loop
south -> Kitchen
west -> Science Lab

Room: Crew Quarters
Items: giant electromagnet
west -> Sick Bay
north -> Storage
south -> Holodeck

Room: Engineering
south -> Hallway
north -> Warp Drive Maintenance
east -> Hot Chocolate Fountain

Room: Gift Wrapping Center
Items: molten lava
south -> Science Lab

Room: Hallway
west -> Hull Breach
north -> Engineering
south -> Passages

Room: Holodeck
Items: tambourine
north -> Crew Quarters
west -> Arcade

Room: Hot Chocolate Fountain
Items: mug
west -> Engineering
north -> Kitchen
Room: Hull Breach
east -> Hallway

Room: Kitchen
south -> Hot Chocolate Fountain
north -> Corridor
east -> Sick Bay

Room: Navigation
Items: prime number
east -> Stables

Room: Observatory
Items: monolith
north -> Science Lab

Room: Passages
Items: escape pod
north -> Hallway

Room: Science Lab
Items: asterisk
east -> Corridor
north -> Gift Wrapping Center
south -> Observatory

Room: Security Checkpoint
south -> Arcade
west -> Security Checkpoint
east -> Security Checkpoint

Room: Sick Bay
Items: sand
west -> Kitchen
east -> Crew Quarters
south -> Stables

Room: Stables
north -> Sick Bay
west -> Navigation

Room: Storage
Items: photons
south -> Crew Quarters

Room: Warp Drive Maintenance
Items: spool of cat6
south -> Engineering
