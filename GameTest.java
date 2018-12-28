import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameTest { 
    private GameCourt board;

    @Before
    public void setUp() {
        // We initialize a fresh GameCourt for each test
        final JLabel status = new JLabel("Running...");
        board = new GameCourt(status);
    }

    /**
     * Here are some test cases for the ChessPieces' movements.
     */
    
    // testing King's movements
    @Test
    public void testKingMovingNormally() {
        board.helper(4, 6, 52); // move Pawn in front of white King forward 2 spaces
        board.helper(4, 4, 36);
        
        board.helper(4, 1, 12); // move Pawn in front of black King forward 2 spaces
        board.helper(4, 3, 28);
        
        board.helper(4, 7, 60); // move white King forward 1 space
        board.helper(4, 6, 52);
        
        board.helper(4, 0, 4); // move black King forward 1 space
        board.helper(4, 1, 12);
        
        assertEquals(board.getOccupiedTiles().get(52), board.getWhiteKing());
        assertEquals(board.getOccupiedTiles().get(12), board.getBlackKing());
    }
    
    @Test
    public void testKingMovingToBlockedBoardTile() {
        board.helper(4, 7, 60); // move white King forward 1 space
        board.helper(4, 6, 52);
        
        assertEquals(board.getOccupiedTiles().get(60), board.getWhiteKing());
    }
    
    @Test
    public void testKingCaptured() {
        board.helper(5, 6, 53); // move white Pawn forward 1 space
        board.helper(5, 5, 45);
        
        board.helper(4, 1, 12); // move black Pawn forward 1 space
        board.helper(4, 2, 20);
        
        board.helper(6, 7, 62); // move white Knight
        board.helper(7, 5, 47);
        
        board.helper(3, 0, 3); // move black Queen
        board.helper(7, 4, 39);
        
        board.helper(4, 6, 52); // move white Pawn forward 2 spaces
        board.helper(4, 4, 36);
        
        board.helper(7, 4, 39); // move black Queen to capture white King
        board.helper(4, 7, 60);
        
        assertEquals(board.getDeadPieces().get("WHITE").contains(board.getWhiteKing()), true);
        assertEquals(board.getPlaying(), false);
    }
    
    @Test
    public void testKingCapturingPiece() {
        board.helper(5, 6, 53); // move white Pawn forward 1 space
        board.helper(5, 5, 45);
        
        board.helper(4, 1, 12); // move black Pawn forward 1 space
        board.helper(4, 2, 20);
        
        board.helper(6, 7, 62); // move white Knight
        board.helper(7, 5, 47);
        
        board.helper(3, 0, 3); // move black Queen
        board.helper(7, 4, 39);
        
        board.helper(4, 6, 52); // move white Pawn forward 2 spaces
        board.helper(4, 4, 36);
        
        board.helper(7, 4, 39); // move black Queen
        board.helper(5, 6, 53);
        
        board.helper(4, 7, 60); // move white King to capture black Queen
        board.helper(5, 6, 53);
        
        assertEquals(board.getOccupiedTiles().get(53), board.getWhiteKing());
        assertEquals(board.getDeadPieces().get("BLACK").contains(board.getBlackQueen()), true);
    }
    
    // testing Queen's movements
    @Test
    public void testQueenMovingNormally() {
        board.helper(4, 6, 52); // move Pawn in front of white King forward 2 spaces
        board.helper(4, 4, 36);
        
        board.helper(4, 1, 12); // move Pawn in front of black King forward 2 spaces
        board.helper(4, 3, 28);
        
        board.helper(3, 7, 59); // move white Queen diagonal 3 spaces
        board.helper(6, 4, 38);
        
        board.helper(3, 0, 3); // move black Queen diagonal 4 spaces
        board.helper(7, 4, 39);
        
        assertEquals(board.getOccupiedTiles().get(38), board.getWhiteQueen());
        assertEquals(board.getOccupiedTiles().get(39), board.getBlackQueen());
    }
    
    @Test
    public void testQueenNotJumpingOverPieces() {
        board.helper(3, 7, 59); // move white King forward 1 space
        board.helper(3, 3, 27);
        
        assertEquals(board.getOccupiedTiles().get(59), board.getWhiteQueen());
    }
    
    @Test
    public void testQueenCaptured() {
        board.helper(5, 6, 53); // move white Pawn forward 1 space
        board.helper(5, 5, 45);
        
        board.helper(4, 1, 12); // move black Pawn forward 1 space
        board.helper(4, 2, 20);
        
        board.helper(6, 7, 62); // move white Knight
        board.helper(7, 5, 47);
        
        board.helper(3, 0, 3); // move black Queen
        board.helper(7, 4, 39);
        
        board.helper(4, 6, 52); // move white Pawn forward 2 spaces
        board.helper(4, 4, 36);
        
        board.helper(7, 4, 39); // move black Queen
        board.helper(5, 6, 53);
        
        board.helper(4, 7, 60); // move white King to capture black Queen
        board.helper(5, 6, 53);
        
        assertEquals(board.getOccupiedTiles().get(53), board.getWhiteKing());
        assertEquals(board.getDeadPieces().get("BLACK").contains(board.getBlackQueen()), true);
    }
    
    @Test
    public void testQueenCapturingPiece() {
        board.helper(5, 6, 53); // move white Pawn forward 1 space
        board.helper(5, 5, 45);
        
        board.helper(4, 1, 12); // move black Pawn forward 1 space
        board.helper(4, 2, 20);
        
        board.helper(6, 7, 62); // move white Knight
        board.helper(7, 5, 47);
        
        board.helper(3, 0, 3); // move black Queen
        board.helper(7, 4, 39);
        
        board.helper(4, 6, 52); // move white Pawn forward 2 spaces
        board.helper(4, 4, 36);
        
        board.helper(7, 4, 39); // move black Queen to captare white Knight
        board.helper(7, 5, 47);
        
        assertEquals(board.getOccupiedTiles().get(47), board.getBlackQueen());
        assertEquals(board.getDeadPieces().get("WHITE").contains(board.getWhiteKnight2()), true);
    }
    
    // testing Bishop's movements
    @Test
    public void testBishopMovingNormally() {
        board.helper(4, 6, 52); // move Pawn in front of white King forward 2 spaces
        board.helper(4, 4, 36);
        
        board.helper(4, 1, 12); // move Pawn in front of black King forward 2 spaces
        board.helper(4, 3, 28);
        
        board.helper(5, 7, 61); // move white Bishop diagonal 3 spaces
        board.helper(2, 4, 34);
        
        board.helper(5, 0, 5); // move black Bishop diagonal 4 spaces
        board.helper(1, 4, 33);
        
        assertEquals(board.getOccupiedTiles().get(34), board.getWhiteBishop2());
        assertEquals(board.getOccupiedTiles().get(33), board.getBlackBishop2());
    }
    
    @Test
    public void testBishopNotJumpingOverPieces() {
        board.helper(2, 7, 58); // move white King forward 1 space
        board.helper(4, 5, 44);
        
        assertEquals(board.getOccupiedTiles().get(58), board.getWhiteBishop1());
    }
    
    @Test
    public void testBishopCaptured() {
        board.helper(4, 6, 52); // move white Pawn forward 1 space
        board.helper(4, 5, 44);
        
        board.helper(4, 1, 12); // move black Pawn forward 1 space
        board.helper(4, 2, 20);
        
        board.helper(5, 7, 61); // move white Bishop
        board.helper(0, 2, 16);
        
        board.helper(1, 0, 1); // move black Knight to capture white Bishop
        board.helper(0, 2, 16);
        
        assertEquals(board.getOccupiedTiles().get(16), board.getBlackKnight1());
        assertEquals(board.getDeadPieces().get("WHITE").contains(board.getWhiteBishop2()), true);
    }
    
    @Test
    public void testBishopCapturingPiece() {
        board.helper(4, 6, 52); // move white Pawn forward 1 space
        board.helper(4, 5, 44);
        
        board.helper(1, 0, 1); // move black Knight
        board.helper(0, 2, 16);
        
        board.helper(5, 7, 61); // move white Bishop to capture black Knight
        board.helper(0, 2, 16);
        
        assertEquals(board.getOccupiedTiles().get(16), board.getWhiteBishop2());
        assertEquals(board.getDeadPieces().get("BLACK").contains(board.getBlackKnight1()), true);
    }
    
    // testing Knight's movements
    @Test
    public void testKnightMovingNormally() {
        board.helper(1, 7, 57); // move white Knight forward 1 space
        board.helper(0, 5, 40);
        
        assertEquals(board.getOccupiedTiles().get(40), board.getWhiteKnight1());
    }
    
    @Test
    public void testKnightMovingAbnormally() {
        board.helper(1, 7, 57); // move white Knight forward 1 space
        board.helper(1, 5, 41);
        
        assertEquals(board.getOccupiedTiles().get(57), board.getWhiteKnight1());
    }
    
    @Test
    public void testKnightCaptured() {
        board.helper(4, 6, 52); // move white Pawn forward 1 space
        board.helper(4, 5, 44);
        
        board.helper(1, 0, 1); // move black Knight
        board.helper(0, 2, 16);
        
        board.helper(5, 7, 61); // move white Bishop to capture black Knight
        board.helper(0, 2, 16);
        
        assertEquals(board.getOccupiedTiles().get(16), board.getWhiteBishop2());
        assertEquals(board.getDeadPieces().get("BLACK").contains(board.getBlackKnight1()), true);
    }
    
    @Test
    public void testKnightCapturingPiece() {
        board.helper(4, 6, 52); // move white Pawn forward 1 space
        board.helper(4, 5, 44);
        
        board.helper(4, 1, 12); // move black Pawn forward 1 space
        board.helper(4, 2, 20);
        
        board.helper(5, 7, 61); // move white Bishop
        board.helper(0, 2, 16);
        
        board.helper(1, 0, 1); // move black Knight to capture white Bishop
        board.helper(0, 2, 16);
        
        assertEquals(board.getOccupiedTiles().get(16), board.getBlackKnight1());
        assertEquals(board.getDeadPieces().get("WHITE").contains(board.getWhiteBishop2()), true);
    }
    
    // testing Rook's movements
    @Test
    public void testRookMovingNormally() {
        board.helper(0, 6, 48); // move Pawn in front of white Rook forward 2 spaces
        board.helper(0, 4, 32);
        
        board.helper(0, 1, 8); // move Pawn in front of black Rook forward 2 spaces
        board.helper(0, 3, 24);
        
        board.helper(0, 7, 56); // move white Rook forward 2 spaces
        board.helper(0, 5, 40);
        
        board.helper(0, 0, 0); // move black Rook forward 2 spaces
        board.helper(0, 2, 16);
        
        assertEquals(board.getOccupiedTiles().get(40), board.getWhiteRook1());
        assertEquals(board.getOccupiedTiles().get(16), board.getBlackRook1());
    }
    
    @Test
    public void testRookNotJumpingOverPieces() {
        board.helper(0, 0, 0); // try to move black Rook left 6 spaces
        board.helper(6, 0, 6);
        
        assertEquals(board.getOccupiedTiles().get(0), board.getBlackRook1());
    }
    
    @Test
    public void testRookCaptured() {
        board.helper(0, 6, 48); // move Pawn in front of white Rook forward 2 spaces
        board.helper(0, 4, 32);
        
        board.helper(4, 1, 12); // move Pawn in front of black King forward 2 spaces
        board.helper(4, 3, 28);
        
        board.helper(0, 7, 56); // move white Rook forward 2 spaces
        board.helper(0, 5, 40);
        
        board.helper(5, 0, 5); // move black Bishop diagonal 5 spaces
        board.helper(0, 5, 40);
        
        assertEquals(board.getOccupiedTiles().get(40), board.getBlackBishop2());
        assertEquals(board.getDeadPieces().get("WHITE").contains(board.getWhiteRook1()), true);
    }
    
    @Test
    public void testRookCapturingPiece() {
        board.helper(0, 6, 48); // move Pawn in front of white Rook forward 2 spaces
        board.helper(0, 4, 32);
        
        board.helper(4, 1, 12); // move Pawn in front of black King forward 2 spaces
        board.helper(4, 3, 28);
        
        board.helper(0, 7, 56); // move white Rook forward 1 space
        board.helper(0, 6, 48);
        
        board.helper(5, 0, 5); // move black Bishop diagonal 5 spaces
        board.helper(0, 5, 40);
        
        board.helper(0, 6, 48); // move white Rook forward 1 space
        board.helper(0, 5, 40);
        
        assertEquals(board.getOccupiedTiles().get(40), board.getWhiteRook1());
        assertEquals(board.getDeadPieces().get("BLACK").contains(board.getBlackBishop2()), true);
    }
    
    // testing Pawn's movements
    @Test
    public void testPawnMovingOneStepForward() {
        board.helper(0, 6, 48); // move Pawn in front of white Rook forward 1 space
        board.helper(0, 5, 40);
        
        assertEquals(board.getOccupiedTiles().get(40), board.getWhitePawn1());
    }
    
    @Test
    public void testPawnMovingTwoStepsForward() {
        board.helper(0, 6, 48); // move Pawn in front of white Rook forward 2 spaces
        board.helper(0, 4, 32);
        
        assertEquals(board.getOccupiedTiles().get(32), board.getWhitePawn1());
    }
    
    @Test
    public void testPawnNotMovingTwoStepsForward() {
        board.helper(0, 6, 48); // move Pawn in front of white Rook forward 1 space
        board.helper(0, 5, 40);
        
        board.helper(0, 1, 8); // move Pawn in front of black Rook forward 2 spaces
        board.helper(0, 3, 24);
        
        board.helper(0, 5, 40); // try to move white Pawn forward 2 spaces
        board.helper(0, 3, 24);
        
        assertEquals(board.getOccupiedTiles().get(40), board.getWhitePawn1());
    }
    
    @Test
    public void testPawnBlocked() {
        board.helper(0, 6, 48); // move Pawn in front of white Rook forward 1 space
        board.helper(0, 4, 32);
        
        board.helper(0, 1, 8); // move Pawn in front of black Rook forward 2 spaces
        board.helper(0, 3, 24);
        
        board.helper(0, 4, 32); // try to move white Pawn forward 1 space
        board.helper(0, 3, 24);
        
        assertEquals(board.getOccupiedTiles().get(32), board.getWhitePawn1());
        assertEquals(board.getOccupiedTiles().get(24), board.getBlackPawn1());
    }
    
    @Test
    public void testPawnCaptured() {
        board.helper(0, 6, 48); // move Pawn in front of white Rook forward 2 spaces
        board.helper(0, 4, 32);
        
        board.helper(1, 1, 9); // move Pawn in front of black Knight forward 2 spaces
        board.helper(1, 3, 25);
        
        board.helper(0, 4, 32); // move white Pawn to capture black Pawn
        board.helper(1, 3, 25);
        
        assertEquals(board.getOccupiedTiles().get(25), board.getWhitePawn1());
        assertEquals(board.getDeadPieces().get("BLACK").contains(board.getBlackPawn2()), true);
    }
    
    
    /**
     * Here are a few more miscellaneous test cases for the server state.
     */
    
    @Test
    public void testBoardTileHighlight() {
        board.helper(0, 6, 48); // Pawn in front of white Rook
        BoardTile[][] tempBoard = board.getBoard();
        
        assertEquals(tempBoard[0][6].getColor(), Color.RED);
    }
    
    @Test
    public void testBoardTileUghighlight() {
        board.helper(0, 6, 48); // Pawn in front of white Rook
        board.helper(0, 4, 32);
        
        BoardTile[][] tempBoard = board.getBoard();
        assertEquals(tempBoard[0][6].getColor(), Color.WHITE);
    }
    
    @Test
    public void testCurrSideColorSwitchesToBlack() {
        board.helper(0, 6, 48); // Pawn in front of white Rook
        board.helper(0, 4, 32);
        
        assertEquals(board.getCurrSideColor(), Color.BLACK);
    }
    
    @Test
    public void testCurrSideColorSwitchesToWhite() {
        board.helper(0, 6, 48); // Pawn in front of white Rook
        board.helper(0, 4, 32);
        
        board.helper(1, 1, 9); // move Pawn in front of black Knight forward 2 spaces
        board.helper(1, 3, 25);
        
        assertEquals(board.getCurrSideColor(), Color.WHITE);
    }
    
    @Test
    public void testNumClick() {
        board.helper(0, 6, 48); // Pawn in front of white Rook
        
        assertEquals(board.getNumClick(), 1);
    }
    
    @Test
    public void testNumClickReset() {
        board.helper(0, 6, 48); // Pawn in front of white Rook
        board.helper(0, 4, 32);
        
        assertEquals(board.getNumClick(), 0);
    }
}

