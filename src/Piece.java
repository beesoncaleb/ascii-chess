import java.util.ArrayList;
public abstract class Piece {

    static int pieceIdCounter = 0;

    protected int id;

    protected int pos;  //position as index of board array of size 64

    protected boolean color;  //white represented with TRUE, black represented with FALSE

    protected ArrayList<Integer> attacks;  //List of all positions that the piece can currently attack, includes the current position

    Piece(int pos, boolean color) {
        this.pos = pos;
        this.color = color;
        this.attacks = new ArrayList();
        this.id = pieceIdCounter++;
    }

    public int getPos() {return this.pos;}

    public boolean getColor() {return this.color;}

    public ArrayList<Integer> getAttacks() {return this.attacks;}

    public int getId() {return this.id;}

    protected void updateAttackBoard(Piece[] board, String[] attackBoard) {
        //clear attackboard so that it can be updated
        for (int i=0; i<64; i++) {
            attackBoard[i] = null;
        }

        //update all pieces attacking squares
        for (Piece current:board) {
            if (current != null) {
                current.updateAttacks(board);
            }
        }

        //update attackboard
        for (Piece current:board) {
            if (current == null) {
                continue;
            }
            for(int attacking:current.getAttacks()) {
                String thisID = Integer.toString(current.getId());
                String color_flag = current.getColor() ? "+" : "-";
                if (attackBoard[attacking] == null) {
                    attackBoard[attacking] = color_flag + thisID + "_";
                }
                else {
                    attackBoard[attacking] += color_flag + thisID + "_";
                }
            }
        }
    }

    protected boolean isKingExposed (Piece[] board, String[] attackBoard) {
        int kingPos = findKing(board);

        //set flag to determine if king in check
        String color_flag = this.color ? "-" : "+";

        return attackBoard[kingPos].contains(color_flag);
    }

    protected int findKing(Piece[] board) {
        for (int i = 0; i<64; i++) {
            Piece current = board[i];
            if (current == null) {
                continue;
            }
            if (current.getColor() == this.color && current instanceof King) {
                return i;
            }
        }
        return -1;
    }

    abstract public int move(int coord, Piece[] board, String[] attackBoard);

    abstract protected void updateAttacks(Piece[]board);

    abstract public ArrayList<Integer> findKingAttackPath(Piece[] board, String[] attackBoard, int kingPos);
}
