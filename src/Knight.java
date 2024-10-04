import java.util.ArrayList;
import java.util.Arrays;

public class Knight extends Piece{
     Knight(int pos, boolean color, Piece[] board) {
        super(pos,color);
    }

    @Override
    public int move(int coord, Piece[] board, String[] attackBoard) {
        if (coord > 63 || coord < 0 || coord == this.pos) {
            return -1;
        }

        int check = Math.abs(coord - pos);
        if (check != 15 && check != 17 && check != 6 && check != 10) {
            return -1;
        }

        Piece p = board[coord];
        int current_row = pos/8;
        int target_row = coord/8;
        switch (check) {
            case 6:
            case 10:
                if (Math.abs(current_row - target_row) != 1) {
                    return -1;
                }
                else if(board[coord] != null && this.color == p.getColor()) {
                    return -1;
                }
                else {
                    int oldpos = this.pos;
                    board[coord] = this;
                    board[pos] = null;
                    pos = coord;
                    updateAttackBoard(board, attackBoard);
                    if (isKingExposed(board, attackBoard)) {
                        this.pos = oldpos;
                        board[coord] = null;
                        board[pos] = this;
                        updateAttackBoard(board, attackBoard);
                        return -1;
                    }
                    return 1;
                }
            case 15:
            case 17:
                if (Math.abs(current_row - target_row) != 2) {
                    return -1;
                }
                else if(board[coord] != null && this.color == p.getColor()) {
                    return -1;
                }
                else {
                    int oldpos = this.pos;
                    board[coord] = this;
                    board[pos] = null;
                    pos = coord;
                    updateAttackBoard(board, attackBoard);
                    if (isKingExposed(board, attackBoard)) {
                        this.pos = oldpos;
                        board[coord] = null;
                        board[pos] = this;
                        updateAttackBoard(board, attackBoard);
                        return -1;
                    }
                    return 1;
                }
            default:
                return -1;
        }
    }

    @Override
    protected void updateAttacks(Piece[] board) {
        attacks.clear();
        attacks.add(this.pos);
        int current_row = this.pos / 8;
        int row1 = current_row - 2;
        int row2 = current_row - 1;
        int row3 = current_row + 1;
        int row4 = current_row + 2;

        int[] steps = {-17, -15, -10, -6, 6, 10, 15, 17};

        for (int step:steps) {
            int destination = this.pos + step;
            if (destination > 63 || destination < 0) {
                continue;
            }
            int row = destination/8;

            switch (step) {
                case -17:
                case -15:
                    if (row == row1) {
                        attacks.add(destination);
                    }
                    break;
                case -10:
                case -6:
                    if (row == row2) {
                        attacks.add(destination);
                    }
                    break;
                case 6:
                case 10:
                    if (row == row3) {
                        attacks.add(destination);
                    }
                    break;
                case 15:
                case 17:
                    if (row == row4) {
                        attacks.add(destination);
                    }
            }
        }
    }

    @Override
    public ArrayList<Integer> findKingAttackPath(Piece[] board, String[] attackBoard, int kingPos) {
        return new ArrayList<>(Arrays.asList(this.pos,kingPos));
    }

    @Override
    public String toString() {
        if (this.color) {
            return "H";
        }
        else {
            return "h";
        }
    }
}
