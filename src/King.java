import java.util.ArrayList;

public class King extends Piece{
    King(int pos, boolean color, Piece[] board) {
        super(pos,color);
    }
    
    @Override
    public int move(int coord, Piece[] board, String[] attackBoard) {
        if (coord > 63 || coord < 0 || this.pos == coord) {
            return -1;
        }
        
        //check if move is potentially valid
        int check = Math.abs(coord - pos);
        if (check != 1 && check != 8 && check != 7 && check != 9) {
            return -1;
        }
        
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

    @Override
    protected void updateAttacks(Piece[] board)  {
        attacks.clear();
        int[] steps = {-9,-8,-7,-1,0,1,7,8,9};
        for (int step:steps) {
            int pos = this.pos + step;
            if (pos > 63 || pos < 0) {
                continue;
            }
            int kingRow = this.pos / 8;
            int posRow = pos / 8;
            switch (step) {
                case -9:
                case -8:
                case -7:
                    if (posRow == kingRow - 1) {
                        attacks.add(pos);
                    }
                    break;
                case -1:
                case 0:
                case 1:
                    if (posRow == kingRow) {
                        attacks.add(pos);
                    }
                    break;
                case 7:
                case 8:
                case 9:
                    if (posRow == kingRow + 1) {
                        attacks.add(pos);
                    }
                    break;
            }
        }
    }

    //this method should never be called on a King Piece
    @Override
    public ArrayList<Integer> findKingAttackPath(Piece[] board, String[] attackBoard, int kingPos) {
        return null;
    }

    @Override
    public String toString() {
        if (this.color) {
            return "K";
        }
        else {
            return "k";
        }
    }
}
