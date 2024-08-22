package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.Player;

public class StandardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int CASTLE_BONUS = 60;
    private static final int DEPTH_BONUS_VALUE = 100;

    @Override
    public int evaluate(Board board, int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) - scorePlayer(board, board.blackPlayer(), depth);
    }

    private static int scorePlayer(Board board, Player whitePlayer, int depth) {
        // TODO add more features to evaluate the board
        return pieceValue(whitePlayer) + castled(whitePlayer) + mobility(whitePlayer) + check(whitePlayer) + checkMate(whitePlayer, depth) + pieceValue(whitePlayer);
    }

    private static int pieceValue(Player player) {
        int pieceValueScore = 0;
        for (final Piece piece : player.getActivePieces()) {
            pieceValueScore += piece.getPieceValue();
        }
        return pieceValueScore;
    }

    private static int castled(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    private static int mobility(Player player) {
        return player.getLegalMoves().size();
    }

    private static int check(Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int checkMate(Player player, int depth) {
        // if checkmate is detected in less moves (higher depth), it is more valuable
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS_VALUE * depth;
    }

    public static Object get() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }
    
}
