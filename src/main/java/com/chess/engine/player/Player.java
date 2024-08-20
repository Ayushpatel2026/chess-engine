package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;

public abstract class Player {

    // a player needs a board, a king, set of legal moves and needs to know if he is in check
    protected final Board board; 
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves){
        this.board = board;
        this.playerKing = establishKing();
        List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(legalMoves);
        allLegalMoves.addAll(calculateKingCastles(legalMoves, opponentMoves));
        this.legalMoves = allLegalMoves;
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    public static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> opponentMoves) {
        final List<Move> attackMoves = new ArrayList<>();
        for (Move move: opponentMoves){
            if (piecePosition == move.getDestinationCoordinate()){
                attackMoves.add(move);
            }
        }

        return attackMoves;
    }

    private King establishKing(){
        for (Piece piece: getActivePieces()){
            if (piece.getPieceType().isKing()){
                return (King) piece;
            }
        }
        throw new RuntimeException("Not a valid board");
    }

    // important methods for the player class

    public King getPlayerKing(){
        return this.playerKing;
    }

    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck(){
        return isInCheck;
    }

    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }

    private boolean hasEscapeMoves() {
        for (Move move: this.legalMoves){
            MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()){
                return true;
            }
        }

        return false;
    }

    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }

    public boolean isCastled(){
        return false;
    }

    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }

    public MoveTransition makeMove(Move move){

        /* when making a move, check if it is legal, and make sure it doesn't leave the player in check after the move 
            If the move is legal, return the new board, with status DONE
        */

        if (!isMoveLegal(move)){
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        Board transitionBoard = move.execute();

        Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
         transitionBoard.currentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(this.board, transitionBoard, move, MoveStatus.DONE);
    }

    public boolean isKingSideCastleCapable(){
        // delegate to the king
        return this.playerKing.isKingSideCastleCapable();
    }

    public boolean isQueenSideCastleCapable(){
        // delegate to the king
        return this.playerKing.isQueenSideCastleCapable();
    }

    // a white player and black player will have different active pieces
    public abstract Collection<Piece> getActivePieces();

    // a white player and black player will have different alliances
    public abstract Alliance getAlliance();

    // a white player and black player will have different opponents
    public abstract Player getOpponent();

    // a white player and black player will have different king castles
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals);
}
