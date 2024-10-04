import java.util.*;
import javax.security.auth.callback.TextOutputCallback;

public class Main {
    public static void main(String[] args) {
        Piece[] board = new Piece[64];
        String[] attackBoard = new String[64];
        HashMap<Integer, Piece> pieceHashMap = new HashMap<>();

        initializeBoard(board, pieceHashMap);
        initializeAttackBoard(attackBoard, board);

        boolean noCheckmate = true;
        boolean whiteTurn = true;    //true = white turn, false = black turn
        while (noCheckmate) {
            displayBoard(board);

            if (whiteTurn) {
                playerTurn(board, attackBoard, true);
                pawnPromotion(board,attackBoard);
                noCheckmate = isNoCheckmate(board, attackBoard, pieceHashMap, false);
                whiteTurn = false;
            }
            else {
                playerTurn(board, attackBoard, false);
                pawnPromotion(board,attackBoard);
                noCheckmate = isNoCheckmate(board, attackBoard, pieceHashMap, true);
                whiteTurn = true;
            }
        }
        displayBoard(board);
        String winner;
        winner = whiteTurn ? "Black" : "White";
        System.out.println("Game over, " + winner + " wins!");
    }

    public static void initializeBoard(Piece[] board, HashMap<Integer, Piece> pieceHashMap)  {
        //initialize pawns
       for (int i = 8; i < 16; i++) {
            Pawn black = new Pawn(i,false, board);
            Pawn white = new Pawn(i+40, true, board);
            board[i] = black;
            board[i+40] = white;
            pieceHashMap.put(black.getId(), black);
            pieceHashMap.put(white.getId(), white);
       }

        //initialize rooks
        board[0] = new Rook(0, false, board);
        board[7] = new Rook(7, false, board);
        board[56] = new Rook(56, true, board);
        board[63] = new Rook(63, true, board);

        pieceHashMap.put(board[0].getId(), board[0]);
        pieceHashMap.put(board[7].getId(), board[7]);
        pieceHashMap.put(board[56].getId(), board[56]);
        pieceHashMap.put(board[63].getId(), board[63]);

        //initialize knights
        board[1] = new Knight(1, false, board);
        board[6] = new Knight(6, false, board);
        board[57] = new Knight(57, true, board);
        board[62] = new Knight(62, true, board);

        pieceHashMap.put(board[1].getId(), board[1]);
        pieceHashMap.put(board[6].getId(), board[6]);
        pieceHashMap.put(board[57].getId(), board[57]);
        pieceHashMap.put(board[62].getId(), board[62]);

        //initialize bishops
        board[2] = new Bishop(2, false, board);
        board[5] = new Bishop(5, false, board);
        board[58] = new Bishop(58, true, board);
        board[61] = new Bishop(61, true, board);

        pieceHashMap.put(board[2].getId(), board[2]);
        pieceHashMap.put(board[5].getId(), board[5]);
        pieceHashMap.put(board[58].getId(), board[58]);
        pieceHashMap.put(board[61].getId(), board[61]);

        //initialize queen
        board[3] = new Queen(3, false, board);
        board[59] = new Queen(59, true, board);

        pieceHashMap.put(board[3].getId(), board[3]);
        pieceHashMap.put(board[59].getId(), board[59]);

        //initialize king
        board[4] = new King(4, false, board);
        board[60] = new King(60, true, board);

        pieceHashMap.put(board[4].getId(), board[4]);
        pieceHashMap.put(board[60].getId(), board[60]);
    }

    public static void displayBoard(Piece[] board) {
        String divider = "-------------------------------\n";

        for (int i = 0; i < 64; i++) {
            if (i == 63 && board[i] == null) {
                System.out.println("\n");
            }
            else if (i == 63) {
                System.out.println(" " + board[i] + "\n");
            }
            else if ((i+1) % 8 == 0 && board[i] == null) {
                System.out.print(" \n" + divider);
            }
            else if((i+1) % 8 == 0) {
                System.out.print(" " + board[i] + "\n" + divider);
            }
            else if (board[i] == null){
               System.out.print("   |");
            }
            else {
                System.out.print(" " + board[i] + " " + "|");
            }
        }
    }

    public static void playerTurn(Piece[] board, String[] attackBoard, boolean flag) {
        final int row0, row1, row2, row3, row4, row5, row6, row7;
        row0 = 0; row1 = 8; row2 = 16; row3 = 24; row4 = 32; row5 = 40; row6 = 48; row7 = 56;
        int[] rowTable = {-1, row7, row6, row5, row4, row3, row2, row1, row0};    //table for converting chess row notation to left offset chess board coordinate
        Scanner input = new Scanner(System.in);

        if (flag) {
            System.out.println("It is White's turn, enter the position of the piece you want to move");
            String selected;
            int Scoordinate;
            while (true) {      //Piece to move loop
                selected = input.nextLine().toLowerCase();
                if (!selected.matches("[a-h][1-8]")) {
                    System.out.println("That is not a valid selection, please try selecting a piece using chess notation");
                    continue;
                }
                int column = (int)selected.charAt(0) - 97;     //converts chess notation to useful index
                int row = (int)selected.charAt(1) - (int)'0';
                Scoordinate = rowTable[row] + column;

                if (board[Scoordinate] == null) {
                    System.out.println("That is not a valid selection, the square you selected is empty, try selecting another");
                } else if (!board[Scoordinate].getColor()) {
                    System.out.println("That is not a valid selection, the square you selected is a black piece, please select a white piece");
                } else {
                    break;
                }
            }

            System.out.println("You have selected " + selected + ", now select the square you would like to move to");
            int Tcoordinate;
            String target;
            while (true) {      //location to move to loop
                target = input.nextLine().toLowerCase();
                if (!target.matches("[a-h][1-8]")) {
                    System.out.println("That is not a valid selection, please try selecting a piece using chess notation");
                    continue;
                }
                int column = (int) target.charAt(0) - 97;     //converts chess notation to useful index
                int row = (int) target.charAt(1) - (int) '0';
                Tcoordinate = rowTable[row] + column;

                if (board[Scoordinate].move(Tcoordinate, board, attackBoard) == -1) {
                    System.out.println("That is not a valid move, please try a different move");
                }
                else {
                    break;
                }
            }
        }
        else {
            System.out.println("It is Black's turn, enter the position of the piece you want to move");
            String selected;
            int Scoordinate;
            while (true) {      //piece to move loop
                selected = input.nextLine().toLowerCase();
                if (!selected.matches("[a-h][1-8]")) {
                    System.out.println("That is not a valid selection, please try selecting a piece using chess notation");
                    continue;
                }
                int column = (int)selected.charAt(0) - 97;     //converts chess notation to useful index
                int row = (int)selected.charAt(1) - (int)'0';
                Scoordinate = rowTable[row] + column;

                if (board[Scoordinate] == null) {
                    System.out.println("That is not a valid selection, the square you selected is empty, try selecting another");
                } else if (board[Scoordinate].getColor()) {
                    System.out.println("That is not a valid selection, the square you selected is a white piece, please select a black piece");
                } else {
                    break;
                }
            }

            System.out.println("You have selected " + selected + ", now select the square you would like to move to");
            int Tcoordinate;
            String target;
            while (true) {      //location to move to loop
                target = input.nextLine().toLowerCase();
                if (!target.matches("[a-h][1-8]")) {
                    System.out.println("That is not a valid selection, please try selecting a piece using chess notation");
                    continue;
                }
                int column = (int) target.charAt(0) - 97;     //converts chess notation to useful index
                int row = (int) target.charAt(1) - (int) '0';
                Tcoordinate = rowTable[row] + column;

                if (board[Scoordinate].move(Tcoordinate, board, attackBoard) == -1) {
                    System.out.println("That is not a valid move, please try a different move");
                }
                else {
                    break;
                }
            }
        }
    }

    public static void initializeAttackBoard (String[] attackBoard, Piece[] board) {
        for (String attacking: attackBoard) {
            attacking = "";
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
                attackBoard[attacking] += color_flag + thisID + "_";
            }
        }
    }

    public static void pawnPromotion (Piece[] board, String[] attackBoard) {
        boolean thereisPawn = false;
        Piece pawn = null;
        for (int i = 0; i < 8; i++) {
            if (board[i] == null || board[i+56] == null) {
                continue;
            }

            String topPiece = board[i].toString().toLowerCase();
            String bottomPiece = board[i+56].toString().toLowerCase();

            if (topPiece.equals("p")) {
                pawn = board[i];
                thereisPawn = true;
                break;
            }
            else if (bottomPiece.equals("p")) {
                pawn = board[i+56];
                thereisPawn = true;
                break;
            }
        }

        if (!thereisPawn) {
            return;
        }

        Scanner input = new Scanner(System.in);

        int pawnRow = pawn.getPos() / 8;
        String position = null;
        int column;
        char chessCol;
        switch(pawnRow) {
            case 0:
                column = pawn.getPos() % 8;
                chessCol = (char)(column + (int) 'a');
                position = chessCol + "8";
                break;
            case 7:
                column = pawn.getPos() % 8;
                chessCol = (char)(column + (int) 'a');
                position = chessCol + "1";
                break;
        }
        boolean promotionNotOccured = true;
        System.out.println("You can promote the pawn at " + position);
        System.out.println("Choose either Rook (R), Knight (H), Bishop (B), or Queen (Q)");
        while (promotionNotOccured) {
            String promotion = input.nextLine().toLowerCase();

            if (pawn.getColor()) {
                switch (promotion) {
                    case "r":
                        Rook r = new Rook(pawn.getPos(), true, board);
                        board[pawn.getPos()] = r;
                        promotionNotOccured = false;
                        break;
                    case "h":
                        Knight h = new Knight(pawn.getPos(), true, board);
                        board[pawn.getPos()] = h;
                        promotionNotOccured = false;
                        break;
                    case "b":
                        Bishop b = new Bishop(pawn.getPos(), true, board);
                        board[pawn.getPos()] = b;
                        promotionNotOccured = false;
                        break;
                    case "q":
                        Queen q = new Queen(pawn.getPos(), true, board);
                        board[pawn.getPos()] = q;
                        promotionNotOccured = false;
                        break;
                    default:
                        System.out.println("That is not a correct input, your input should be one of the follow: R H B Q");
                }
            }
            else {
                switch (promotion) {
                    case "r":
                        Rook r = new Rook(pawn.getPos(), false, board);
                        board[pawn.getPos()] = r;
                        promotionNotOccured = false;
                        break;
                    case "h":
                        Knight h = new Knight(pawn.getPos(), false, board);
                        board[pawn.getPos()] = h;
                        promotionNotOccured = false;
                        break;
                    case "b":
                        Bishop b = new Bishop(pawn.getPos(), false, board);
                        board[pawn.getPos()] = b;
                        promotionNotOccured = false;
                        break;
                    case "q":
                        Queen q = new Queen(pawn.getPos(), false, board);
                        board[pawn.getPos()] = q;
                        promotionNotOccured = false;
                        break;
                    default:
                        System.out.println("That is not a correct input, your input should be one of the follow: R H B Q");
                }
            }
        }
        initializeAttackBoard(attackBoard, board);
    }

    //return false when checkmate occurs, return true if no checkmate
    public static boolean isNoCheckmate(Piece[] board, String[] attackBoard, HashMap<Integer, Piece> pieceHashMap, boolean color) {
        //initialize color identifiers for later use
        String enemyColorFlag = color ? "-" : "+";
        String friendlyColorFlag = color ? "+" : "-";

        //find king position and king ID
        int kingPos = -1;
        int kingID = -1;
        for (int i=0; i<64; i++) {
            Piece current = board[i];
            if (current == null) {
                continue;
            }
            if (current.getColor() == color && current instanceof King) {
                kingPos = i;
                kingID = current.getId();
                break;
            }
        }

        //if there is no check, then there is no checkmate
        if (!attackBoard[kingPos].contains(enemyColorFlag)) {
            return true;
        }

        //checks how many pieces are attacking the king and saves those pieces to a list
        String[] kingPosAttacks = attackBoard[kingPos].split("_");

        ArrayList<Piece> checkingPieces = new ArrayList<>();
        for (String attack:kingPosAttacks) {
            if (attack.contains(enemyColorFlag)) {
                int checkID = Integer.parseInt(attack.substring(1));
                checkingPieces.add(pieceHashMap.get(checkID));
            }
        }

        //for the checking pieces, get the hidden attack that isn't accounted for by the attackboard
        ArrayList<Integer> hiddenAttacks = new ArrayList<>();
        for (Piece attacking:checkingPieces) {
            ArrayList<Integer> kingAttack = attacking.findKingAttackPath(board, attackBoard, kingPos);
            int lastIndex = kingAttack.size() - 1;
            if (kingAttack.get(lastIndex) == kingPos) {
                continue;
            }
            hiddenAttacks.add(kingAttack.get(lastIndex));
        }

        //create grid of positions to consider for checkmate
        ArrayList<Integer> kingGrid = new ArrayList<>();
        int[] steps = {-9,-8,-7,-1,1,7,8,9};
        for (int step:steps) {
            int pos = kingPos + step;
            if (pos > 63 || pos < 0) {
                continue;
            }
            int kingRow = kingPos / 8;
            int posRow = pos / 8;
            switch (step) {
                case -9:
                case -8:
                case -7:
                    if (posRow == kingRow - 1) {
                        kingGrid.add(pos);
                    }
                    break;
                case -1:
                case 1:
                    if (posRow == kingRow) {
                        kingGrid.add(pos);
                    }
                    break;
                case 7:
                case 8:
                case 9:
                    if (posRow == kingRow + 1) {
                        kingGrid.add(pos);
                    }
            }
        }

        //refine grid to only positions that king can move to by removing friendly occupied spaces
        ArrayList<Integer> removals = new ArrayList();
        for (int position:kingGrid) {
            if (board[position] == null) {
                continue;
            }
            if (board[position].getColor() == color) {
                removals.add(position);
            }
        }
        kingGrid.removeAll(removals);

        //check if king's possible moves contains a safe move for the king, if it does then there is no checkmate
        for (int pos:kingGrid) {
            if (hiddenAttacks.contains(pos)) {
                continue;
            }
            if (!attackBoard[pos].contains(enemyColorFlag)) {
                return true;
            }
        }

        //if no safe moves for king and more than 1 enemy Piece is attacking king, then there is checkmate
        if (checkingPieces.size() > 1) {
            return false;
        }

        //there is only one Piece checking king, this code checks that attack path to see if it can be disrupted
        Piece checkPiece = checkingPieces.get(0);
        ArrayList<Integer> kingAttackPath = checkPiece.findKingAttackPath(board, attackBoard, kingPos);

        //trim king attack path to remove hidden attack and king attack
        int attackLastIndex = kingAttackPath.size() - 1;
        kingAttackPath.remove(attackLastIndex);
        attackLastIndex -= 1;
        if (kingAttackPath.get(attackLastIndex) == kingPos) {
            kingAttackPath.remove(attackLastIndex);
            attackLastIndex -= 1;
        }

        //checks for disruption, if found then return true that there is checkmate
        for (int i=0; i <= attackLastIndex; i++) {
            int currentPos = kingAttackPath.get(i);
            String[] attacks = attackBoard[currentPos].split("_");
            if (attacks[0] == "") {     //skip spots that are not being attacked
                continue;
            }
            for (String attack:attacks) {
                int attackID = Integer.parseInt(attack.substring(1));
                if (attack.contains(friendlyColorFlag) && attackID != kingID) {
                    return false;
                }
            }
        }

        return true;
    }
}