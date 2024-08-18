package com.chess.pgn;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;

public class FenUtilities {
    private FenUtilities() {
        throw new RuntimeException("Not instantiable!");
    }

    public static Board createGameFromFEN(final String fenString) {
        return null;
    }

    public static String createFENFromGame(Board board){
        return calculateBoardText(board) + " " +
                calculateCurrentPlayerText(board) + " " +
                calculateCastleText(board) + " " +
                calculateEnPassantSquare(board) + " " +
                "0 1";
    }

    private static String calculateCurrentPlayerText(Board board) {
        return board.currentPlayer().toString().substring(0, 1).toLowerCase();
    }

    private static String calculateCastleText(Board board) {
        final StringBuilder builder = new StringBuilder();
        if (board.whitePlayer().isKingSideCastleCapable()) {
            builder.append("K");
        }
        if (board.whitePlayer().isQueenSideCastleCapable()) {
            builder.append("Q");
        }
        if (board.blackPlayer().isKingSideCastleCapable()) {
            builder.append("k");
        }
        if (board.blackPlayer().isQueenSideCastleCapable()) {
            builder.append("q");
        }
        final String result = builder.toString();
        return result.isEmpty() ? "-" : result;
    }

    private static String calculateEnPassantSquare(Board board) {
        return board.getEnPassantPawn() == null ? "-" :
                BoardUtils.getPositionAtCoordinate(board.getEnPassantPawn().getPiecePosition() + (8 * board.getEnPassantPawn().getPieceAlliance().getOppositeDirection()));
    }

    private static String calculateBoardText(Board board) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            final String tileText = board.getTile(i).toString();
            builder.append(tileText);
            if ((i + 1) % 8 == 0) {
                builder.append("/");
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString().replaceAll("--------", "8")
                .replaceAll("-------", "7")
                .replaceAll("------", "6")
                .replaceAll("-----", "5")
                .replaceAll("----", "4")
                .replaceAll("---", "3")
                .replaceAll("--", "2")
                .replaceAll("-", "1");
    }
}
