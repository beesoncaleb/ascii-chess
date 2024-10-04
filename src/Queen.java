import java.util.ArrayList;
public class Queen extends Piece{
    Queen(int pos, boolean color, Piece[] board) {
        super(pos,color);
    }

    @Override
    public int move(int coord, Piece[] board, String[] attackBoard) {
        if (coord > 63 || coord < 0 || this.pos == coord) {
            return -1;
        }

        //check if move is potentially valid and initialize step value for later validation
        int check = coord - pos;
        int current_row = pos / 8;
        int target_row = coord / 8;
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
        else if (current_column == target_column && current_row < target_row) {
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
        //check if any pieces are obstructing the path
        for (int i = pos+step; i != coord; i += step) {
            if (board[i] != null) {
                return -1;
            }
        }
        //check if square is empty or contains enemy piece and moves this queen there
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
    protected void updateAttacks(Piece[] board) {
        this.attacks.clear();
        this.attacks.add(this.pos);
        int current_row = this.pos / 8;
        int current_column = this.pos % 8;

        //adds all attacking square to top left
        int row_check = current_row;
        for (int i = this.pos-9; row_check - (i/8) == 1 && i > -1; i -= 9) {
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
        for (int i = this.pos-7; (i%8) - column_check == 1 && i > -1; i -= 7) {
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
        for (int i = this.pos+7; column_check - (i%8) == 1 && i < 64; i += 7) {
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
        for (int i = this.pos+9; (i/8) - row_check == 1 && i < 64; i += 9) {
            if (board[i] == null) {
                this.attacks.add(i);
                row_check= i/8;
            }
            else {
                this.attacks.add(i);
                break;
            }
        }

        //adds all attacking squares to the right of queen
        for (int i = pos+1; i < (current_row+1)*8; i++) {
            if (board[i] == null) {
                this.attacks.add(i);
            }
            else {
                this.attacks.add(i);
                break;
            }

        }

        //adds all attacking squares to the left of queen
        for (int i = pos-1; i > ((current_row-1)*8)+7; i--) {
            if (board[i] == null) {
                this.attacks.add(i);
            }
            else {
                this.attacks.add(i);
                break;
            }
        }

        //adds all attacking squares below queen
        for (int i = pos+8; i < 64; i += 8) {
            if (board[i] == null) {
                this.attacks.add(i);
            }
            else {
                this.attacks.add(i);
                break;
            }
        }

        //adds all attacking squares above queen
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

    @Override
    public ArrayList<Integer> findKingAttackPath(Piece[] board, String[] attackBoard, int kingPos) {
        //find step parameter to return king attack path
        int check = kingPos - this.pos;
        int current_row = this.pos / 8;
        int king_row = kingPos / 8;
        int current_column = this.pos % 8;
        int king_column = kingPos % 8;
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
        else if (current_column == king_column && current_row < king_row) {
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
        kingAttackPath.add(kingPos);
        //additional attacking squares to account for false safe moves for the king in the attack board
        int hiddenAttack = kingPos + step;
        if (hiddenAttack > 63 || hiddenAttack < 0) {    //check that hidden attack is on board
            return kingAttackPath;
        }
        int hiddenRow = hiddenAttack / 8;
        switch (step) {                                 //check that hidden attack is a valid attack
            case 1:
            case -1:
                if (hiddenRow != king_row) {
                    return kingAttackPath;
                }
                break;
            case -7:
            case -9:
            case 9:
            case 7:
                int hiddenCheck = king_row - hiddenRow;
                if (hiddenCheck == 0 || hiddenCheck == -1 || hiddenCheck == 1) {
                    return kingAttackPath;
                }
                break;
        }
        kingAttackPath.add(hiddenAttack);
        return kingAttackPath;
    }

    @Override
    public String toString() {
        if (this.color) {
            return "Q";
        }
        else {
            return "q";
        }
    }
}
