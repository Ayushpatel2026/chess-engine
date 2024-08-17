package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.PawnAttackMove;
import com.chess.engine.board.Move.PawnEnPassantAttackMove;
import com.chess.engine.board.Move.PawnJump;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.board.Move.PawnPromotion;

public class Pawn extends Piece{
    private final static int[] CANDIDATE_MOVE_COORDINATES = {7, 8, 9, 16};

    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance, PieceType.PAWN, true);
    }

    public Pawn(final int piecePosition, final Alliance pieceAlliance, final boolean isFirstMove){
        super(piecePosition, pieceAlliance, PieceType.PAWN, isFirstMove);
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    @Override
    public Piece movePiece(Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public List<Move> calculateLegalMoves(Board board){
        int candidateDestinationCoordinate;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset: CANDIDATE_MOVE_COORDINATES){
            candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection()) * (currentCandidateOffset);
            if (!BoardUtils.isValidCoordinate(candidateDestinationCoordinate)){
                continue;
            }

            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                
                if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                }else{
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }

            } else if (currentCandidateOffset == 16 && 
                        this.isFirstMove() && 
                        ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAlliance().isBlack()) || 
                        (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAlliance().isWhite()))
            ){
                
                int behindCandidateDestinationCoordinate = this.piecePosition + this.pieceAlliance.getDirection() * 8;
                
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && 
                    !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate)); 
                }

            }else if (currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))
            ){
                
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        }else{
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }

                }else if (board.getEnPassantPawn() != null){
                    
                    // if position of enpassant pawn is next to our pawn, +1 for white, -1 for black
                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + this.pieceAlliance.getOppositeDirection())){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }

                }
            }else if (currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                    (BoardUtils.EIGTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))
            ){
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                        
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                        }else{
                            legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }

                    }

                }else if (board.getEnPassantPawn() != null){
                    
                    // if position of enpassant pawn is next to our pawn, -1 for white, +1 for black
                    if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - this.pieceAlliance.getOppositeDirection())){
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    public Piece getPromotionPiece(){
        return new Queen(this.piecePosition, this.pieceAlliance, false);
    }
}
