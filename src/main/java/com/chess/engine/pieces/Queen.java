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

public class Queen extends Piece {
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -8, -7, -1, 1,7, 8, 9};

    public Queen(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.QUEEN, true);
    }

    public Queen(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove){
        super(piecePosition, pieceAlliance, PieceType.QUEEN, isFirstMove);
    }

    @Override
    public String toString(){
        return PieceType.QUEEN.toString();
    }

    @Override
    public Piece movePiece(Move move) {
        return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public List<Move> calculateLegalMoves(Board board){
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();
        for (int currentCandidateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
            candidateDestinationCoordinate = this.piecePosition;

            while (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)){
                
                if (isFirstColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset) ||
                    isEigthColumnExclusion(candidateDestinationCoordinate, currentCandidateOffset)
                ){
                    break;
                }
                
                candidateDestinationCoordinate += currentCandidateOffset;

                if (BoardUtils.isValidCoordinate(candidateDestinationCoordinate)){
                    
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    
                    if (!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }else{
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        if (pieceAtDestination.getPieceAlliance() != this.pieceAlliance){
                            legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break; // cannot jump over pieces, do not want to continue
                    }
                    
                }
            }
        }
        return legalMoves;
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
    }

    private static boolean isEigthColumnExclusion(int currentPosition, int candidateOffset){
        return BoardUtils.EIGTH_COLUMN[currentPosition] && (candidateOffset == 1 || candidateOffset == -7 || candidateOffset == 9);
    }
}