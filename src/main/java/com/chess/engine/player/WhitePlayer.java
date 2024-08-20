package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.chess.engine.pieces.Piece.PieceType;


public class WhitePlayer extends Player {

    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, Collection<Move> blackStandardLegalMoves){
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces(){
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    /*When calculating the castle moves, need to ensure that:
     *  1. The king is in its starting position
     *  2. The king is not in check
     *  3. The squares between the king and the rook are empty
     *  4. The squares that the king moves through are not under attack
     *  5. The king is not moving into check
     *  6. The rook is in its starting position 
     */

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && this.playerKing.getPiecePosition() == 60 && !this.isInCheck()) {
            //whites king side castle
            if(this.board.getPiece(61) == null && this.board.getPiece(62) == null) {
                final Piece kingSideRook = this.board.getPiece(63);
                if(kingSideRook != null && kingSideRook.isFirstMove()) {
                    if(Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTile(62, opponentLegals).isEmpty() &&
                       kingSideRook.getPieceType() == PieceType.ROOK) {
                        if(!BoardUtils.isKingPawnTrap(this.board, this.playerKing, 52)) {
                            kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 62, (Rook) kingSideRook, kingSideRook.getPiecePosition(), 61));
                        }
                    }
                }
            }
            //whites queen side castle
            if(this.board.getPiece(59) == null && this.board.getPiece(58) == null &&
               this.board.getPiece(57) == null) {
                final Piece queenSideRook = this.board.getPiece(56);
                if(queenSideRook != null && queenSideRook.isFirstMove()) {
                    if(Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                       Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() && queenSideRook.getPieceType() == PieceType.ROOK) {
                        if(!BoardUtils.isKingPawnTrap(this.board, this.playerKing, 52)) {
                            kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58, (Rook) queenSideRook, queenSideRook.getPiecePosition(), 59));
                        }
                    }
                }
            }
        }
        return kingCastles;
    }
    
}
