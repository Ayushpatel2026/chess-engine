package com.chess;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.pieces.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TestPieces {

    @Test
    public void testMiddleBishopOnEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(35, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 18);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("a7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("b6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("c5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("f2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("g1"))));
    }

    @Test
    public void testTopLeftBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(0, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(board.getPiece(0), board.getPiece(0));
        assertNotNull(board.getPiece(0));
        assertEquals(whiteLegals.size(), 12);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("b7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("c6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("d5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("e4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("f3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("g2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a8"), BoardUtils.getCoordinateAtPosition("h1"))));
    }

    @Test
    public void testTopRightBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(7, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 12);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("g7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("f6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("c3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("b2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h8"), BoardUtils.getCoordinateAtPosition("a1"))));
    }

    @Test
    public void testBottomLeftBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(56, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 12);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("b2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("c3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("f6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("g7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("a1"), BoardUtils.getCoordinateAtPosition("h8"))));
    }

    @Test
    public void testBottomRightBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Bishop(63, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 12);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("g2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("f3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("e4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("d5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("c6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("b7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("h1"), BoardUtils.getCoordinateAtPosition("a8"))));
    }

    @Test
    public void testMiddleRookOnEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        // White Layout
        builder.setPiece(new Rook(36, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 18);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e8"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("a4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("b4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("c4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("f4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("g4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("h4"))));
    }

    @Test
    public void testSimpleWhiteEnPassant() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final Move m1 = Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition(
                "e2"), BoardUtils.getCoordinateAtPosition("e4"));
        final MoveTransition t1 = board.currentPlayer().makeMove(m1);
        Assert.assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e8"), BoardUtils.getCoordinateAtPosition("d8"));
        final MoveTransition t2 = t1.getTransitionBoard().currentPlayer().makeMove(m2);
        Assert.assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("e5"));
        final MoveTransition t3 = t2.getTransitionBoard().currentPlayer().makeMove(m3);
        Assert.assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = Move.MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d7"), BoardUtils.getCoordinateAtPosition("d5"));
        final MoveTransition t4 = t3.getTransitionBoard().currentPlayer().makeMove(m4);
        Assert.assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = Move.MoveFactory.createMove(t4.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("d6"));
        final MoveTransition t5 = t4.getTransitionBoard().currentPlayer().makeMove(m5);
        Assert.assertTrue(t5.getMoveStatus().isDone());
    }

    @Test
    public void testSimpleBlackEnPassant() {
        final Board.Builder builder = new Board.Builder();
        // Black Layout
        builder.setPiece(new King(4, Alliance.BLACK, false, false));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        final Move m1 = Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition(
                "e1"), BoardUtils.getCoordinateAtPosition("d1"));
        final MoveTransition t1 = board.currentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d7"), BoardUtils.getCoordinateAtPosition("d5"));
        final MoveTransition t2 = t1.getTransitionBoard().currentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d1"), BoardUtils.getCoordinateAtPosition("c1"));
        final MoveTransition t3 = t2.getTransitionBoard().currentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = Move.MoveFactory.createMove(t3.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d5"), BoardUtils.getCoordinateAtPosition("d4"));
        final MoveTransition t4 = t3.getTransitionBoard().currentPlayer().makeMove(m4);
        assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = Move.MoveFactory.createMove(t4.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e4"));
        final MoveTransition t5 = t4.getTransitionBoard().currentPlayer().makeMove(m5);
        assertTrue(t5.getMoveStatus().isDone());
        final Move m6 = Move.MoveFactory.createMove(t5.getTransitionBoard(), BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("e3"));
        final MoveTransition t6 = t5.getTransitionBoard().currentPlayer().makeMove(m6);
        Assert.assertTrue(t6.getMoveStatus().isDone());
    }
    

}