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
import java.util.Scanner;

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
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
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
            // move 1 square forward
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                
                if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                }else{
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }

            } 
            // move 2 squares forward
            else if (currentCandidateOffset == 16 && 
                        this.isFirstMove() 
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
                    
                    // if position of enpassant pawn is next to our pawn, +1 for white, -1 for black (for offset of 7)
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
                    
                    // if position of enpassant pawn is next to our pawn, -1 for white, +1 for black (for offset of 9)
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
        // want to be able to underpromote to a knight, bishop, or rook
        // prompt the user to choose the promotion piece
        // and return the corresponding piece based on their choice
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose promotion piece: (N)Knight, (B)Bishop, (R)Rook");
        String choice = scanner.nextLine();
        scanner.close();
        switch (choice.toUpperCase()) {
            case "N":
                return new Knight(this.piecePosition, this.pieceAlliance, false);
            case "B":
                return new Bishop(this.piecePosition, this.pieceAlliance, false);
            case "R":
                return new Rook(this.piecePosition, this.pieceAlliance, false);
            default:
                return new Queen(this.piecePosition, this.pieceAlliance, false);
        }

    } 
}
