package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;

public class King extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;
    private final boolean isCastled;
    
    public King(final int piecePosition, final Alliance pieceAlliance, final boolean kingSideCastleCapable,
                final boolean queenSideCastleCapable){
        super(piecePosition, pieceAlliance, PieceType.KING, true);
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
        this.isCastled = false;
    }

    public King(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove,
                final boolean isCastled, final boolean kingSideCastleCapable, final boolean queenSideCastleCapable){
        super(piecePosition, pieceAlliance, PieceType.KING, isFirstMove);
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
        this.isCastled = isCastled;
        
    }

    public boolean isKingSideCastleCapable(){
        return this.kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable(){
        return this.queenSideCastleCapable;
    }

    public boolean isCastled(){
        return this.isCastled;
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    @Override
    public Piece movePiece(Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), 
                        false, move.isCastlingMove(), false, false);
    }

    // Note that castle moves are calculated separately for each player
    // Checks are also not considered here
    @Override
    public List<Move> calculateLegalMoves(Board board){
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();
        for (int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
            candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEigthColumnExclusion(this.piecePosition, currentCandidateOffset)
                ){
                    continue;
                }
            
            if (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                
                if (!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    if (pieceAtDestination.getPieceAlliance() != this.pieceAlliance){
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }

        }
        return legalMoves;
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset){
        // from first column cannot move left, top left, bottom left
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
    }

    private static boolean isEigthColumnExclusion(int currentPosition, int candidateOffset){
        // from eigth column cannot move right, top right, bottom right
        return BoardUtils.EIGTH_COLUMN[currentPosition] && (candidateOffset == 1 || candidateOffset == -7 || candidateOffset == 9);
    }
}
