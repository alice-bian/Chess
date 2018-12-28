import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class GameCourt extends JPanel implements MouseListener {

    // game board
    private BoardTile[][] board = new BoardTile[8][8];
    
    // living pieces and occupied Tiles
    private Map<Integer, ChessPiece> occupiedTiles;
    
    // dead pieces
    private Map<String, java.util.List<ChessPiece>> deadPieces = new TreeMap<String, java.util.List<ChessPiece>>();
    
    // MouseClick event variables
    Color currSideColor;
    int numClick;    // counts number of clicks per side
    boolean blocked;
    
    // stores all information for previous click
    int prevTileRow;
    int prevTileColumn;
    int prevTileLabel;
    ChessPiece prevPiece;
    Color prevTileColor;
    
    // indicates possible moves for selected piece
    Set<Integer> possibleMoves;
    
    // indicates whether possibleMoves contains ChessPiece of current side's color
    boolean iAmEatingMyTeammate;
    
    // in case Pawn is promoted and there are no deadPieces or there are
    // no non-Pawn deadPieces
    boolean alreadyDone;
    
    // Game constants
    public static final int COURT_WIDTH = 400;
    public static final int COURT_HEIGHT = 400;

    public boolean playing = true; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    
    // create kings
    ChessPiece whiteKing = new King(Color.WHITE, (4*50), (7*50), 60);
    ChessPiece blackKing = new King(Color.BLACK, (4*50), (0*50), 4);
        
    // create queens
    ChessPiece whiteQueen = new Queen(Color.WHITE, (3*50), (7*50), 59);
    ChessPiece blackQueen = new Queen(Color.BLACK, (3*50), (0*50), 3);
        
    // create bishops
    ChessPiece whiteBishopG = new Bishop(Color.WHITE, (2*50), (7*50), 58); // white bishop moving only on green BoardTiles
    ChessPiece whiteBishopW = new Bishop(Color.WHITE, (5*50), (7*50), 61); // white bishop moving only on white BoardTiles
    ChessPiece blackBishopG = new Bishop(Color.BLACK, (5*50), (0*50), 5);  // black bishop moving only on green BoardTiles
    ChessPiece blackBishopW = new Bishop(Color.BLACK, (2*50), (0*50), 2);  // black bishop moving only on white BoardTiles
        
    // create knights
    ChessPiece whiteKnightG = new Knight(Color.WHITE, (6*50), (7*50), 62); // white knight starting on green BoardTile
    ChessPiece whiteKnightW = new Knight(Color.WHITE, (1*50), (7*50), 57); // white knight starting on white BoardTile
    ChessPiece blackKnightG = new Knight(Color.BLACK, (1*50), (0*50), 1);  // black knight starting on green BoardTile
    ChessPiece blackKnightW = new Knight(Color.BLACK, (6*50), (0*50), 6);  // black knight starting on white BoardTile
        
    // create rooks
    ChessPiece whiteRookG = new Rook(Color.WHITE, (0*50), (7*50), 56); // white rook starting on green BoardTile
    ChessPiece whiteRookW = new Rook(Color.WHITE, (7*50), (7*50), 63); // white rook starting on white BoardTile
    ChessPiece blackRookG = new Rook(Color.BLACK, (7*50), (0*50), 7);  // black rook starting on green BoardTile
    ChessPiece blackRookW = new Rook(Color.BLACK, (0*50), (0*50), 0);  // black rook starting on white BoardTile
        
    // create pawns
    ChessPiece whitePawn1 = new Pawn(Color.WHITE, (0*50), (6*50), 48); // leftmost white Pawn
    ChessPiece whitePawn2 = new Pawn(Color.WHITE, (1*50), (6*50), 49);
    ChessPiece whitePawn3 = new Pawn(Color.WHITE, (2*50), (6*50), 50);
    ChessPiece whitePawn4 = new Pawn(Color.WHITE, (3*50), (6*50), 51);
    ChessPiece whitePawn5 = new Pawn(Color.WHITE, (4*50), (6*50), 52);
    ChessPiece whitePawn6 = new Pawn(Color.WHITE, (5*50), (6*50), 53);
    ChessPiece whitePawn7 = new Pawn(Color.WHITE, (6*50), (6*50), 54);
    ChessPiece whitePawn8 = new Pawn(Color.WHITE, (7*50), (6*50), 55); // rightmost white Pawn
        
    ChessPiece blackPawn1 = new Pawn(Color.BLACK, (0*50), (1*50), 8);  // leftmost black Pawn
    ChessPiece blackPawn2 = new Pawn(Color.BLACK, (1*50), (1*50), 9);
    ChessPiece blackPawn3 = new Pawn(Color.BLACK, (2*50), (1*50), 10);
    ChessPiece blackPawn4 = new Pawn(Color.BLACK, (3*50), (1*50), 11);
    ChessPiece blackPawn5 = new Pawn(Color.BLACK, (4*50), (1*50), 12);
    ChessPiece blackPawn6 = new Pawn(Color.BLACK, (5*50), (1*50), 13);
    ChessPiece blackPawn7 = new Pawn(Color.BLACK, (6*50), (1*50), 14);
    ChessPiece blackPawn8 = new Pawn(Color.BLACK, (7*50), (1*50), 15); // rightmost black Pawn

    public GameCourt(JLabel status) {
        this.status = status;
        
        reset();    // see reset() method below for initialization details

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        this.addMouseListener(this);
    }
    
    /**
     * The following four mouse events are not important for my game. My game
     * only responds to mouseClicked().
     */
    @Override
    public void mousePressed(MouseEvent e) {
        
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }
    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
    /**
     * During every turn, each side gets two clicks: once to select the ChessPiece
     * that the payer wants to move, and once to select the BoardTile the 
     * piece should go to. I keep track of the number of clicks per player using
     * a global numClick variable.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (playing) { // mouseClicked() only responds while King is not captured
            System.out.println("Mouse clicked: X = " + e.getX() + ", Y = " + e.getY());
            int currTileR = e.getX()/50;
            int currTileC = e.getY()/50;
            int currTileL = (currTileC*8) + currTileR;
            
            // helper function below takes care of remaining changes to the game state
            helper(currTileR, currTileC, currTileL);
        }
    }
    
    /**
     * This method is simply a continuation of mouseClicked() above. I put everything
     * in a separate function for the purposes of testing.
     */
    public void helper(int currTileRow, int currTileColumn, int currTileLabel) {
        // indicates what happens during the player's first click of any given turn
        if (numClick == 0) {
            for (Map.Entry<Integer, ChessPiece> entry : occupiedTiles.entrySet()) {
                Integer key = entry.getKey();
                ChessPiece val = entry.getValue();
                
                // proceed if currTile is occupied and player has chosen piece from correct
                // side's color
                if (key == currTileLabel && val.getColor().equals(currSideColor)) {
                    // highlight BoardTile on player's first click
                    // creates border around the court area, JComponent method
                    prevTileColor = board[currTileRow][currTileColumn].getColor();
                    board[currTileRow][currTileColumn].highlight();
                    repaint();
                    
                    // set previous information to current information in 
                    // preparation for next click
                    numClick++;
                    prevTileRow = currTileRow;
                    prevTileColumn = currTileColumn;
                    prevTileLabel = currTileLabel;
                    prevPiece = val;
                    System.out.println(prevPiece.getLocation());
                    break;
                }
            }
        } // indicates what happens during player's second click of their turn
        else if (numClick == 1) {
            possibleMoves = prevPiece.moveOptions();
            
            if (prevPiece.getType().equals("PAWN")) {
                pawnModifications();
            } else if (prevPiece.getType().equals("ROOK") || prevPiece.getType().equals("QUEEN")) {
                rookModifications();
            } else if (prevPiece.getType().equals("BISHOP")) {
                bishopModifications();
            } else if (prevPiece.getType().equals("QUEEN")) { // Queen's moves is combination of Bishop's
                rookModifications();                          // and Rook's moves
                bishopModifications();
            }
            
            /** start assessing possibleMoves */
            // Note that if prevPiece is a Pawn that has reached the other end 
            // of the board, then it can get promoted. My chess game is modified 
            // so that Pawns can only be promoted to non-Pawn ChessPieces 
            // that have been captured already. If no non-Pawn ChessPieces 
            // have been captured, then Pawn just disappears as a "deadPiece".
            System.out.println(possibleMoves);
            System.out.println("" + currTileLabel);
            if (possibleMoves.contains(currTileLabel)) {
                checkIAmEatingMyTeammate(currTileLabel);
                
                if (!iAmEatingMyTeammate) {
                    if (prevPiece.getType().equals("PAWN")) {
                        if (prevPiece.getColor().equals(Color.BLACK) && currTileColumn == 7) {
                            promoteBlackPawn();
                        } else if (prevPiece.getColor().equals(Color.WHITE) && currTileColumn == 0) {
                            promoteWhitePawn();
                        }
                    }
                    
                    if (!alreadyDone) {
                        board[prevTileRow][prevTileColumn].unhighlight(prevTileColor);
                        prevPiece.setXPos(currTileRow * 50);
                        prevPiece.setYPos(currTileColumn * 50);
                        repaint();
            
                        // reset everything
                        numClick = 0;
                        occupiedTiles.put(currTileLabel, prevPiece);
                        occupiedTiles.remove(prevTileLabel);
                        prevPiece.setLocation(currTileLabel);
                    }
                    
            
                    // switch players
                    if (currSideColor.equals(Color.WHITE)) {
                        currSideColor = Color.BLACK;
                    } else {
                        currSideColor = Color.WHITE;
                    }
                }
            }
            System.out.println("Dead black pieces: " + deadPieces.get("BLACK"));
            System.out.println("Dead white pieces: " + deadPieces.get("WHITE"));
        }
        
        // reset iAmEatingMyTeammate and alreadyDone
        iAmEatingMyTeammate = false;
        alreadyDone = false;
    
        if (deadPieces.get("BLACK").contains(blackKing)) {
            playing = false;
            status.setText("White wins!");
        }
        if (deadPieces.get("WHITE").contains(whiteKing)) {
            playing = false;
            status.setText("Black wins!");
        }
    }
    
    public int checkBlocked(int locn) {
        int ans = -1;
        for (Map.Entry<Integer, ChessPiece> entry : occupiedTiles.entrySet()) {
            Integer key = entry.getKey();
            ChessPiece val = entry.getValue();
            if (locn == key) {
                // if blocked by teammate
                if (val.getColor().equals(prevPiece.getColor())) {
                    blocked = true;
                    break;
                } else { // if blocked by opponent
                    blocked = true;
                    ans = key;
                    //possibleMoves.add(key);
                    break;
                }
            }
        }
        if (!blocked) {
            ans = locn;
            //possibleMoves.add(locn);
        }
        return ans;
    }
    
    // If prevPiece is a Pawn, then add additional move functionalities:
    // 1. If Pawn hasn't moved yet, then it can also move forward twice
    // 2. If there are enemies diagonal to it, then Pawn can capture those
    //    enemies.
    public void pawnModifications() {
        int loc = prevPiece.getLocation();
        if (prevPiece.getColor().getRGB() == Color.BLACK.getRGB()) { // black Pawn
            if (loc >= 8 && loc <= 15) {
                possibleMoves.add(prevPiece.getLocation() + 16);
            }
            for (Map.Entry<Integer, ChessPiece> entry : occupiedTiles.entrySet()) {
                Integer key = entry.getKey();
                ChessPiece val = entry.getValue();
                // if ChessPiece in front of Pawn is enemy
                if (loc + 8 == key && val.getColor().equals(Color.WHITE)) {
                    possibleMoves.remove(key);
                }
                
                // if ChessPiece diagonal of Pawn is enemy
                if (loc + 7 == key && val.getColor().equals(Color.WHITE)) {
                    possibleMoves.add(key);
                }
                if (loc + 9 == key && val.getColor().equals(Color.WHITE)) {
                    possibleMoves.add(key);
                }
            }
        } else { // white Pawn
            if (loc >= 48 && loc <= 55) {
                possibleMoves.add(prevPiece.getLocation() - 16);
            }
            for (Map.Entry<Integer, ChessPiece> entry : occupiedTiles.entrySet()) {
                Integer key = entry.getKey();
                ChessPiece val = entry.getValue();
                // if ChessPiece in front of Pawn is enemy
                if (loc - 8 == key && val.getColor().equals(Color.BLACK)) {
                    possibleMoves.remove(key);
                }
                
                // if ChessPiece diagonal of Pawn is enemy
                if (loc - 7 == key && val.getColor().equals(Color.BLACK)) {
                    possibleMoves.add(key);
                }
                if (loc - 9 == key && val.getColor().equals(Color.BLACK)) {
                    possibleMoves.add(key);
                }
            }
        }
    }
    
    // If prevPiece is a Rook, then add move possibilities in vertical and 
    // horizontal directions while other ChessPieces are not in the way.
    public void rookModifications() {
        // vertical direction
        int loc = prevPiece.getLocation() - 8;
        blocked = false;
        while (loc >= 0 && !blocked) {
            int maybeAdd = checkBlocked(loc);
            if (maybeAdd != -1) {
                possibleMoves.add(maybeAdd);
            }
            loc -= 8;
        }
        loc = prevPiece.getLocation() + 8;
        blocked = false;
        while (loc <= 63 && !blocked) {
            int maybeAdd = checkBlocked(loc);
            if (maybeAdd != -1) {
                possibleMoves.add(maybeAdd);
            }
            loc += 8;
        }
        // horizontal direction
        loc = prevPiece.getLocation() - 1;
        blocked = false;
        while (loc/8 == prevTileColumn && !blocked) {
            int maybeAdd = checkBlocked(loc);
            if (maybeAdd != -1) {
                possibleMoves.add(maybeAdd);
            }
            loc--;
        }
        loc = prevPiece.getLocation() + 1;
        blocked = false;
        while (loc/8 == prevTileColumn && !blocked) {
            int maybeAdd = checkBlocked(loc);
            if (maybeAdd != -1) {
                possibleMoves.add(maybeAdd);
            }
            loc++;
        }
    }
    
    // If prevPiece is a Bishop, then remove move possibilities in diagonal
    // directions when other ChessPieces are in the way.
    public void bishopModifications() {
        // negative diagonal direction
        int loc = prevPiece.getLocation() - 9;
        int tileR = prevTileRow - 1;
        int tileC = prevTileColumn - 1;
        blocked = false;
        while (loc >= 0 && tileR >= 0 && tileC >= 0 && !blocked) {
            int maybeAdd = checkBlocked(loc);
            if (maybeAdd != -1) {
                possibleMoves.add(maybeAdd);
            }
            loc -= 9;
            tileR--;
            tileC--;
        }
        loc = prevPiece.getLocation() + 9;
        tileR = prevTileRow + 1;
        tileC = prevTileColumn + 1;
        blocked = false;
        while (loc <= 63 && tileR <= 7 && tileC <= 7 && !blocked) {
            int maybeAdd = checkBlocked(loc);
            if (maybeAdd != -1) {
                possibleMoves.add(maybeAdd);
            }
            loc += 9;
            tileR++;
            tileC++;
        }
        // positive diagonal direction
        loc = prevPiece.getLocation() - 7;
        tileR = prevTileRow + 1;
        tileC = prevTileColumn - 1;
        blocked = false;
        while (loc >= 0 && tileR <= 7 && tileC >= 0 && !blocked) {
            int maybeAdd = checkBlocked(loc);
            if (maybeAdd != -1) {
                possibleMoves.add(maybeAdd);
            }
            loc -= 7;
            tileR++;
            tileC--;
        }
        loc = prevPiece.getLocation() + 7;
        tileR = prevTileRow - 1;
        tileC = prevTileColumn + 1;
        blocked = false;
        while (loc <= 63 && tileR >= 0 && tileC <= 7 && !blocked) {
            int maybeAdd = checkBlocked(loc);
            if (maybeAdd != -1) {
                possibleMoves.add(maybeAdd);
            }
            loc += 7;
            tileR--;
            tileC++;
        }
    }
    
    // Checks whether or not selected BoardTile contains ChessPiece of same color
    // as current side's color. If captured ChessPiece is of different color from
    // current player, then adds captured ChessPiece to deadPieces.
    public void checkIAmEatingMyTeammate(int currTile) {
        for (Map.Entry<Integer, ChessPiece> entry : occupiedTiles.entrySet()) {
            Integer key = entry.getKey();
            ChessPiece val = entry.getValue();
            if (key == currTile) {
                if (val.getColor().equals(currSideColor)) {
                    iAmEatingMyTeammate = true;
                } else {
                    if (currSideColor.equals(Color.WHITE)) {
                        deadPieces.get("BLACK").add(val);
                    } else {
                        deadPieces.get("WHITE").add(val);
                    }
                }
            }
        }
    }
    
    // put Pawn as deadPiece
    // randomly generate number to indicate new piece to be promoted
    // set prevPiece to new ChessPiece
    public void promoteBlackPawn() {
        deadPieces.get("BLACK").add(prevPiece);
        occupiedTiles.remove(prevTileLabel);
        repaint();
        boolean containsNonPawn = false;
        for (int i = 0; i < deadPieces.get("BLACK").size(); i++) {
            ChessPiece temp = deadPieces.get("BLACK").get(i);
            if (!temp.getType().equals("PAWN")) {
                prevPiece = temp;
                containsNonPawn = true;
                break;
            }
        }
        
        // in case Pawn is promoted and there are no deadPieces or there are
        // no non-Pawn deadPieces
        if (!containsNonPawn) {
            board[prevTileRow][prevTileColumn].unhighlight(prevTileColor);
            numClick = 0;
            alreadyDone = true;
            repaint();
        }
    }
    
    public void promoteWhitePawn() {
        deadPieces.get("WHITE").add(prevPiece);
        occupiedTiles.remove(prevTileLabel);
        repaint();
        boolean containsNonPawn = false;
        for (int i = 0; i < deadPieces.get("WHITE").size(); i++) {
            ChessPiece temp = deadPieces.get("WHITE").get(i);
            if (!temp.getType().equals("PAWN")) {
                prevPiece = temp;
                containsNonPawn = true;
                break;
            }
        }
        
        // in case Pawn is promoted and there are no deadPieces or there are
        // no non-Pawn deadPieces
        if (!containsNonPawn) {
            board[prevTileRow][prevTileColumn].unhighlight(prevTileColor);
            numClick = 0;
            alreadyDone = true;
            repaint();
        }
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        int lbl = 0;
        boolean stat = true;
        Color col = Color.WHITE;
        for (int i = 0; i < board.length; i++) {
            if (i > 2 || i < 5) { // initially, first and last two rows are occupied
                stat = false;
            }
            for (int j = 0; j < board[0].length; j++) {
                if ((i + j) % 2 == 1) {
                    col = new Color(34,139,34);
                } else {
                    col = Color.WHITE;
                }
                BoardTile curr = new BoardTile(i * 50, j * 50, lbl, stat, col);
                board[i][j] = curr;
                lbl++; // updates label for next BoardTile
            }
        }
        
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // reset deadPieces
        deadPieces = new TreeMap<String, java.util.List<ChessPiece>>();
        java.util.List<ChessPiece> whiteTeamD = new ArrayList<ChessPiece>();
        deadPieces.put("WHITE", whiteTeamD);
        java.util.List<ChessPiece> blackTeamD = new ArrayList<ChessPiece>();
        deadPieces.put("BLACK", blackTeamD);
        
        // reinput occupiedTiles
        occupiedTiles = new TreeMap<Integer, ChessPiece>();
        
        // set up occupiedTiles for black team
        occupiedTiles.put(0, blackRookW);
        occupiedTiles.put(1, blackKnightG);
        occupiedTiles.put(2, blackBishopW);
        occupiedTiles.put(3, blackQueen);
        occupiedTiles.put(4, blackKing);
        occupiedTiles.put(5, blackBishopG);
        occupiedTiles.put(6, blackKnightW);
        occupiedTiles.put(7, blackRookG);
        occupiedTiles.put(8, blackPawn1);
        occupiedTiles.put(9, blackPawn2);
        occupiedTiles.put(10, blackPawn3);
        occupiedTiles.put(11, blackPawn4);
        occupiedTiles.put(12, blackPawn5);
        occupiedTiles.put(13, blackPawn6);
        occupiedTiles.put(14, blackPawn7);
        occupiedTiles.put(15, blackPawn8);
        
        // set up occupiedTiles for white team
        occupiedTiles.put(56, whiteRookG);
        occupiedTiles.put(57, whiteKnightW);
        occupiedTiles.put(58, whiteBishopG);
        occupiedTiles.put(59, whiteQueen);
        occupiedTiles.put(60, whiteKing);
        occupiedTiles.put(61, whiteBishopW);
        occupiedTiles.put(62, whiteKnightG);
        occupiedTiles.put(63, whiteRookW);
        occupiedTiles.put(48, whitePawn1);
        occupiedTiles.put(49, whitePawn2);
        occupiedTiles.put(50, whitePawn3);
        occupiedTiles.put(51, whitePawn4);
        occupiedTiles.put(52, whitePawn5);
        occupiedTiles.put(53, whitePawn6);
        occupiedTiles.put(54, whitePawn7);
        occupiedTiles.put(55, whitePawn8);
        
        
        // MouseClick event variables
        currSideColor = Color.WHITE;
        numClick = 0;    // counts number of clicks per side
        blocked = false;
        
        // stores all information for previous click
        prevTileRow = 0;
        prevTileColumn = 0;
        prevTileLabel = 0;
        prevPiece = null;
        prevTileColor = Color.WHITE;
        
        // indicates possible moves for selected piece
        possibleMoves = new TreeSet<Integer>();
        
        // indicates whether possibleMoves contains ChessPiece of current side's color
        iAmEatingMyTeammate = false;
        
        // in case Pawn is promoted and there are no deadPieces or there are
        // no non-Pawn deadPieces
        alreadyDone = false;
        
        // reset king position
        whiteKing.setXPos(4*50);
        whiteKing.setYPos(7*50);
        whiteKing.setLocation(60);
        blackKing.setXPos(4*50);
        blackKing.setYPos(0*50);
        blackKing.setLocation(4);
        
        // reset queen position
        whiteQueen.setXPos(3*50);
        whiteQueen.setYPos(7*50);
        whiteQueen.setLocation(59);
        blackQueen.setXPos(3*50);
        blackQueen.setYPos(0*50);
        blackQueen.setLocation(3);
        
        // reset bishop positions
        whiteBishopG.setXPos(2*50);
        whiteBishopG.setYPos(7*50);
        whiteBishopG.setLocation(58);
        whiteBishopW.setXPos(5*50);
        whiteBishopW.setYPos(7*50);
        whiteBishopW.setLocation(61);
        blackBishopG.setXPos(5*50);
        blackBishopG.setYPos(0*50);
        blackBishopG.setLocation(5);
        blackBishopW.setXPos(2*50);
        blackBishopW.setYPos(0*50);
        blackBishopW.setLocation(2);
        
        // reset knight positions
        whiteKnightG.setXPos(6*50);
        whiteKnightG.setYPos(7*50);
        whiteKnightG.setLocation(62);
        whiteKnightW.setXPos(1*50);
        whiteKnightW.setYPos(7*50);
        whiteKnightW.setLocation(57);
        blackKnightG.setXPos(1*50);
        blackKnightG.setYPos(0*50);
        blackKnightG.setLocation(1);
        blackKnightW.setXPos(6*50);
        blackKnightW.setYPos(0*50);
        blackKnightW.setLocation(6);
        
        // reset rook positions
        whiteRookG.setXPos(0*50);
        whiteRookG.setYPos(7*50);
        whiteRookG.setLocation(56);
        whiteRookW.setXPos(7*50);
        whiteRookW.setYPos(7*50);
        whiteRookW.setLocation(63);
        blackRookG.setXPos(7*50);
        blackRookG.setYPos(0*50);
        blackRookG.setLocation(7);
        blackRookW.setXPos(0*50);
        blackRookW.setYPos(0*50);
        blackRookW.setLocation(0);
        
        // reset pawn positions
        whitePawn1.setXPos(0*50);
        whitePawn1.setYPos(6*50);
        whitePawn1.setLocation(48);
        whitePawn2.setXPos(1*50);
        whitePawn2.setYPos(6*50);
        whitePawn2.setLocation(49);
        whitePawn3.setXPos(2*50);
        whitePawn3.setYPos(6*50);
        whitePawn3.setLocation(50);
        whitePawn4.setXPos(3*50);
        whitePawn4.setYPos(6*50);
        whitePawn4.setLocation(51);
        whitePawn5.setXPos(4*50);
        whitePawn5.setYPos(6*50);
        whitePawn5.setLocation(52);
        whitePawn6.setXPos(5*50);
        whitePawn6.setYPos(6*50);
        whitePawn6.setLocation(53);
        whitePawn7.setXPos(6*50);
        whitePawn7.setYPos(6*50);
        whitePawn7.setLocation(54);
        whitePawn8.setXPos(7*50);
        whitePawn8.setYPos(6*50);
        whitePawn8.setLocation(55);
        
        blackPawn1.setXPos(0*50);
        blackPawn1.setYPos(1*50);
        blackPawn1.setLocation(8);
        blackPawn2.setXPos(1*50);
        blackPawn2.setYPos(1*50);
        blackPawn2.setLocation(9);
        blackPawn3.setXPos(2*50);
        blackPawn3.setYPos(1*50);
        blackPawn3.setLocation(10);
        blackPawn4.setXPos(3*50);
        blackPawn4.setYPos(1*50);
        blackPawn4.setLocation(11);
        blackPawn5.setXPos(4*50);
        blackPawn5.setYPos(1*50);
        blackPawn5.setLocation(12);
        blackPawn6.setXPos(5*50);
        blackPawn6.setYPos(1*50);
        blackPawn6.setLocation(13);
        blackPawn7.setXPos(6*50);
        blackPawn7.setYPos(1*50);
        blackPawn7.setLocation(14);
        blackPawn8.setXPos(7*50);
        blackPawn8.setYPos(1*50);
        blackPawn8.setLocation(15);
        
        playing = true;
        status.setText("Running...");
        
        repaint();

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // paint board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                BoardTile curr = board[i][j];
                curr.draw(g);
            }
        }
        
        // paint all occupied BoardTiles
        for (Map.Entry<Integer, ChessPiece> entry : occupiedTiles.entrySet()) {
            ChessPiece val = entry.getValue();
            val.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    /*** GETTERS **********************************************************************************/
    
    /**
     * The following are some getter functions for my JUnit tests.
     */
    
    public BoardTile[][] getBoard() {
        return board;
    }
    
    // creates and returns a copy of occupiedTiles so that client classes cannot directly
    // modify occupiedTiles
    public Map<Integer, ChessPiece> getOccupiedTiles() {
        Map<Integer, ChessPiece> tempOccupiedTiles = new TreeMap<Integer, ChessPiece>();
        for (Map.Entry<Integer, ChessPiece> entry : occupiedTiles.entrySet()) {
            Integer key = entry.getKey();
            ChessPiece val = entry.getValue();
            tempOccupiedTiles.put(key, val);
        }
        return tempOccupiedTiles;
    }
    
    // creates and returns a copy of deadPieces so that client classes cannot directly
    // modify deadPieces
    public Map<String, java.util.List<ChessPiece>> getDeadPieces() {
        Map<String, java.util.List<ChessPiece>> tempDeadPieces = new TreeMap<String, java.util.List<ChessPiece>>();
        for (Map.Entry<String, java.util.List<ChessPiece>> entry : deadPieces.entrySet()) {
            String key = entry.getKey();
            java.util.List<ChessPiece> val = entry.getValue();
            tempDeadPieces.put(key, val);
        }
        return tempDeadPieces;
    }
    
    public Color getCurrSideColor() {
        return currSideColor;
    }
    
    public int getNumClick() {
        return numClick;
    }
    
    public boolean getBlocked() {
        return blocked;
    }
    
    public int getPrevTileRow() {
        return prevTileRow;
    }
    
    public int getPrevTileColumn() {
        return prevTileColumn;
    }
    
    public int getPrevTileLabel() {
        return prevTileLabel;
    }
    
    public ChessPiece getPrevPiece() {
        return prevPiece;
    }
    
    public Color getPrevTileColor() {
        return prevTileColor;
    }
    
    public boolean getPlaying() {
        return playing;
    }
    
    // returning each individual piece
    
    public ChessPiece getWhiteKing() {
        return whiteKing;
    }
    
    public ChessPiece getBlackKing() {
        return blackKing;
    }
    
    public ChessPiece getWhiteQueen() {
        return whiteQueen;
    }
    
    public ChessPiece getBlackQueen() {
        return blackQueen;
    }
    
    public ChessPiece getWhiteBishop1() {
        return whiteBishopG;
    }
    
    public ChessPiece getWhiteBishop2() {
        return whiteBishopW;
    }
    
    public ChessPiece getBlackBishop1() {
        return blackBishopW;
    }
    
    public ChessPiece getBlackBishop2() {
        return blackBishopG;
    }
    
    public ChessPiece getWhiteKnight1() {
        return whiteKnightW;
    }
    
    public ChessPiece getWhiteKnight2() {
        return whiteKnightG;
    }
    
    public ChessPiece getBlackKnight1() {
        return blackKnightG;
    }
    
    public ChessPiece getBlackKnight2() {
        return blackKnightW;
    }
    
    public ChessPiece getWhiteRook1() {
        return whiteRookG;
    }
    
    public ChessPiece getWhiteRook2() {
        return whiteRookW;
    }
    
    public ChessPiece getBlackRook1() {
        return blackRookW;
    }
    
    public ChessPiece getBlackRook2() {
        return blackRookG;
    }
    
    public ChessPiece getWhitePawn1() {
        return whitePawn1;
    }
    
    public ChessPiece getWhitePawn2() {
        return whitePawn2;
    }
    
    public ChessPiece getWhitePawn3() {
        return whitePawn3;
    }
    
    public ChessPiece getWhitePawn4() {
        return whitePawn4;
    }
    
    public ChessPiece getWhitePawn5() {
        return whitePawn5;
    }
    
    public ChessPiece getWhitePawn6() {
        return whitePawn6;
    }
    
    public ChessPiece getWhitePawn7() {
        return whitePawn7;
    }
    
    public ChessPiece getWhitePawn8() {
        return whitePawn8;
    }
    
    public ChessPiece getBlackPawn1() {
        return blackPawn1;
    }
    
    public ChessPiece getBlackPawn2() {
        return blackPawn2;
    }
    
    public ChessPiece getBlackPawn3() {
        return blackPawn3;
    }
    
    public ChessPiece getBlackPawn4() {
        return blackPawn4;
    }
    
    public ChessPiece getBlackPawn5() {
        return blackPawn5;
    }
    
    public ChessPiece getBlackPawn6() {
        return blackPawn6;
    }
    
    public ChessPiece getBlackPawn7() {
        return blackPawn7;
    }
    
    public ChessPiece getBlackPawn8() {
        return blackPawn8;
    }
    
}
