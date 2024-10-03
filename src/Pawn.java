import java.util.ArrayList;
import java.util.Arrays;

public class Pawn extends Piece {
    public Pawn(int pos, boolean color, Piece[] board) {
        super(pos,color);
    }

    public int move(int coord, Piece[] board, String[] attackBoard) {
        if (coord > 63 || coord < 0 || this.pos == coord) {
            return -1;
        }

        if (this.color) {
            int check = this.pos - coord;
            if (check != 9 && check != 7 && check != 16 && check != 8) {
                return -1;
            }

            int current_row = this.pos / 8;
            int future_row = coord / 8;
            int row_diff = current_row - future_row;
            switch (check) {
                case 9:
                case 7:
                    if ((board[coord] == null || board[coord].getColor()) && row_diff != 1) {
                        return -1;
                    }
                    break;
                case 16:
                    if (current_row != 6) {
                        return -1;
                    } else if (board[coord + 8] != null && board[coord] != null) {
                        return -1;
                    }
                    break;
                default:
                    if (board[coord] != null) {
                        return -1;
                    }
            }

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

        } else {
            int check = coord - this.pos;
            if (check != 9 && check != 7 && check != 16 && check != 8) {
                return -1;
            }

            int current_row = this.pos / 8;
            int future_row = coord / 8;
            int row_diff = future_row - current_row;
            switch (check) {
                case 9:
                case 7:
                    if ((board[coord] == null || !board[coord].getColor()) && row_diff != 1)  {
                        return -1;
                    }
                    break;
                case 16:
                    if (current_row != 1) {
                        return -1;
                    } else if (board[coord - 8] != null && board[coord] != null) {
                        return -1;
                    }
                    break;
                default:
                    if (board[coord] != null) {
                        return -1;
                    }
            }

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
    }

    protected void updateAttacks(Piece[] board) {
        attacks.clear();
        attacks.add(this.pos);

        int row = this.pos / 8;
        int col = this.pos % 8;

        if (row == 7 || row == 0) {
            return;
        }

        if (this.color) {
            switch (col) {
                case 0:
                    attacks.add(this.pos - 7);
                    break;
                case 7:
                    attacks.add(this.pos - 9);
                    break;
                default:
                    attacks.add(this.pos - 7);
                    attacks.add(this.pos - 9);
            }
        }
        else {
            switch (col) {
                case 7:
                    attacks.add(this.pos + 7);
                    break;
                case 0:
                    attacks.add(this.pos + 9);
                    break;
                default:
                    attacks.add(this.pos + 7);
                    attacks.add(this.pos + 9);
            }
        }
    }

    public ArrayList<Integer> findKingAttackPath(Piece[] board, String[] attackBoard, int kingPos) {
        return new ArrayList<>(Arrays.asList(this.pos,kingPos));
    }

    @Override
    public String toString () {
        if (this.color) {
            return "P";
        } else {
            return "p";
        }
    }

}