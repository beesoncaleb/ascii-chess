import java.util.ArrayList;

public class Bishop extends Piece{
    Bishop(int pos, boolean color, Piece[] board) {
        super(pos,color);
    }

    @Override
    public int move(int coord, Piece[] board, String[] attackBoard) {
        if (coord > 63 || coord < 0 || coord == this.pos) {
            return -1;
        }

        //checks that coord is a potential valid move and gives step for later validation
        int check = coord - pos;
        int current_column = pos % 8;
        int target_column = coord % 8;
        int step;
        if (check > 0 && check % 9 == 0 && target_column > current_column) {
            step = 9;
        }
        else if (check < 0 && check % 9 == 0 && target_column < current_column) {
            step = -9;
        }
        else if (check > 0 && check % 7 == 0 && target_column < current_column) {
            step = 7;
        }
        else if (check < 0 && check % 7 == 0 && target_column > current_column) {
            step = -7;
        }
        else {
            return -1;
        }

        //guarantees that theoretically valid move is actually valid and moves piece there
        for (int i = pos+step; i != coord; i += step) {
            if (board[i] != null) {
                return -1;
            }
        }
        if (board[coord] != null && this.color == board[coord].getColor()) {
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
    protected void updateAttacks(Piece[] board) {
        attacks.clear();
        int current_row = this.pos / 8;
        int current_column = this.pos % 8;

        //adds all attacking square to top left
        int row_check = current_row;
        for (int i = this.pos-9; row_check - (i/8) == 1 && i < 64 && i > -1; i -= 9) {
            if (board[i] == null) {
                this.attacks.add(i);
                row_check= i/8;
            }
            else {
                this.attacks.add(i);
                break;
            }
        }

        //adds all attacking squares to the top right
        int column_check = current_column;
        for (int i = this.pos-7; (i%8) - column_check == 1 && i < 64 && i > -1; i -= 7) {
            if (board[i] == null) {
                this.attacks.add(i);
                column_check = i%8;
            }
            else {
                this.attacks.add(i);
                break;
            }
        }

        //adds all attacking square to the bottom left
        column_check = current_column;
        for (int i = this.pos+7; column_check - (i%8) == 1 && i < 64 && i > -1; i += 7) {
            if (board[i] == null) {
                this.attacks.add(i);
                column_check = i%8;
            }
            else {
                this.attacks.add(i);
                break;
            }
        }

        //adds all attacking square to the bottom right
        for (int i = this.pos+9; (i/8) - row_check == 1 && i < 64 && i > -1; i += 9) {
            if (board[i] == null) {
                this.attacks.add(i);
                row_check= i/8;
            }
            else {
                this.attacks.add(i);
                break;
            }
        }
    }

    @Override
    public ArrayList<Integer> findKingAttackPath(Piece[] board, String[] attackBoard, int kingPos) {
        //find step parameter to return king attack path
        int check = kingPos - this.pos;
        int current_column = this.pos % 8;
        int king_column = kingPos % 8;
        int king_row = kingPos / 8;
        int step=0;
        if (check > 0 && check % 9 == 0 && king_column > current_column) {
            step = 9;
        }
        else if (check < 0 && check % 9 == 0 && king_column < current_column) {
            step = -9;
        }
        else if (check > 0 && check % 7 == 0 && king_column < current_column) {
            step = 7;
        }
        else if (check < 0 && check % 7 == 0 && king_column > current_column) {
            step = -7;
        }

        //find king path to return
        ArrayList<Integer> kingAttackPath = new ArrayList<>();
        for (int i=this.pos; i != kingPos; i += step) {
            kingAttackPath.add(i);
        }
        kingAttackPath.add(kingPos);
        //additional attacking squares to account for false safe moves for the king
        int hiddenAttack = kingPos + step;
        if (hiddenAttack > 63 || hiddenAttack < 0) {    //ensure that hidden attack is on board
            return kingAttackPath;
        }
        int hiddenCheck = king_row - (hiddenAttack / 8);    //ensure that hidden attack is valid attack by comparing rows of hidden and king
        if (hiddenCheck == 0 || hiddenCheck == 2 || hiddenCheck == -2) {
            return kingAttackPath;
        }
        kingAttackPath.add(hiddenAttack);
        return kingAttackPath;
    }

    @Override
    public String toString() {
        if (this.color) {
            return "B";
        }
        else {
            return "b";
        }
    }
}
