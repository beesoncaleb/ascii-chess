# Current Issues that need to be fixed
- Checkmate Issue (IndexOutOfBoundsException)
- Fix findKingAttackPath issue
    - No checks that hidden attack square is valid
- Fix infinite loop on piece selection during check
    - When king in check, if selected piece unable to prevent check, then infinite loop occurs
    - Two solutions
        1. When you try to move selected piece, restart selection entirely
        2. Don't allow for selection of pieces that can't do anything about check

# Quality of Life additions
- Allow board to display row and collumn headers for chess coordinate notation
- Allow for piece deselection