package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Move;
import com.chess.engine.board.Board;

public abstract class Piece {
    
    protected final int piecePosition;
    protected final Alliance pieceAlliance; // white or black
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    protected final PieceType pieceType;

    Piece(final int piecePosition, final Alliance pieceAlliance, final PieceType pieceType, boolean isFirstMove){
        this.pieceAlliance = pieceAlliance;
        this.piecePosition = piecePosition;
        this.pieceType = pieceType;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = this.hashCode();

    }

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (!(other instanceof Piece)){
            return false;
        }

        final Piece otherPiece = (Piece) other;

        // Two pieces are equal if: same alliance, same position, same pieceType and same isFirstMove
        return pieceAlliance == otherPiece.getPieceAlliance() && pieceType == otherPiece.getPieceType()
        && piecePosition == otherPiece.getPiecePosition() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode(){
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }

    public boolean isFirstMove(){
        return isFirstMove;
    }

    public int getPiecePosition(){
        return this.piecePosition;
    }

    public PieceType getPieceType(){
        return this.pieceType;
    }

    public int getPieceValue(){
        return this.pieceType.getPieceValue();
    }

    // returns a new piece with an updated piece position
    public abstract Piece movePiece(Move move);

    // Given a board, calculate all the legal moves for this piece
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    // This enum represents the type of piece, value of piece, and whether it is a king
    public enum PieceType{
        PAWN("P", 100) {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT("N", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public int getPieceValue(){
                return 300;
            }
        },
        BISHOP("B", 300) {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R", 500) {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q", 900) {
            @Override
            public boolean isKing() {
                return false;
            }


        },
        KING("K", 10000) {
            @Override
            public boolean isKing() {
                return true;
            }
        };

        private String pieceName;
        private int pieceValue;
        PieceType(final String pieceName, int pieceValue){
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public abstract boolean isKing();
        public int getPieceValue(){
            return this.pieceValue;
        }
    }
}
