import java.util.ArrayList;

public class Rook extends Piece{
    Rook(int pos, boolean color, Piece[] board) {
        super(pos,color);
    }

    public int move(int coord, Piece[] board, String[] attackBoard) {
        if (coord > 63 || coord < 0 || this.pos == coord) {
            return -1;
        }

        //check if move is potentially valid and initialize step value for later validation
        int current_row = pos / 8;
        int target_row = coord / 8;
        int current_column = pos % 8;
        int target_column = coord % 8;
        int step;
        if (current_column == target_column && current_row < target_row) {
            step = 8;
        }
        else if (current_column == target_column && current_row > target_row) {
            step = -8;
        }
        else if (current_row == target_row && current_column < target_column) {
            step = 1;
        }
        else if (current_row == target_row && current_column > target_column) {
            step = -1;
        }
        else {
            return -1;
        }

        //check if any Pieces are obstructing the path
        for (int i = pos+step; i != coord; i += step) {
            if (board[i] != null) {
                return -1;
            }
        }

        //check if square is empty or contains enemy Piece and moves this rook there
        if (board[coord] != null && board[coord].getColor() == this.color) {
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
    }

    protected void updateAttacks(Piece[] board) {
        attacks.clear();
        attacks.add(this.pos);
        int row = this.pos / 8;

        //adds all attacking squares to the right of rook
        for (int i = pos+1; i < (row+1)*8; i++) {
            if (board[i] == null) {
                this.attacks.add(i);
            }
            else {
                this.attacks.add(i);
                break;
            }

        }

        //adds all attacking squares to the left of rook
        for (int i = pos-1; i > ((row-1)*8)+7; i--) {
            if (board[i] == null) {
                this.attacks.add(i);
            }
            else {
                this.attacks.add(i);
                break;
            }
        }

        //adds all attacking squares below rook
        for (int i = pos+8; i < 64; i += 8) {
            if (board[i] == null) {
                this.attacks.add(i);
            }
            else {
                this.attacks.add(i);
                break;
            }
        }

        //adds all attacking squares above rook
        for (int i = pos-8; i > -1; i -= 8) {
            if (board[i] == null) {
                this.attacks.add(i);
            }
            else {
                this.attacks.add(i);
                break;
            }
        }
    }

    public ArrayList<Integer> findKingAttackPath(Piece[] board, String[] attackBoard, int kingPos) {
        //find step parameter to return king attack path
        int current_row = this.pos / 8;
        int king_row = kingPos / 8;
        int current_column = this.pos % 8;
        int king_column = kingPos % 8;
        int step=0;
        if (current_column == king_column && current_row < king_row) {
            step = 8;
        }
        else if (current_column == king_column && current_row > king_row) {
            step = -8;
        }
        else if (current_row == king_row && current_column < king_column) {
            step = 1;
        }
        else if (current_row == king_row && current_column > king_column) {
            step = -1;
        }

        //find king path to return
        ArrayList<Integer> kingAttackPath = new ArrayList<>();
        for (int i=this.pos; i != kingPos; i += step) {
            kingAttackPath.add(i);
        }
        //additional attacking squares to account for false safe moves for the king
        kingAttackPath.add(kingPos);
        if ((kingPos + step) < 64 && (kingPos + step) > -1) {
            kingAttackPath.add(kingPos + step);
        }
        return kingAttackPath;
    }

    @Override
    public String toString() {
        if (this.color) {
            return "R";
        }
        else {
            return "r";
        }
    }
}
