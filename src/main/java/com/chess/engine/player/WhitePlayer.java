package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

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

    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentLegals) {
        List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && this.playerKing.getPiecePosition() == 60 && !this.isInCheck()){
            // king side castle for white
            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()){
                Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(62, opponentLegals).isEmpty()
                    ){
                        kingCastles.add(new KingSideCastleMove(board, 
                                                               playerKing, 
                                                               62, 
                                                               (Rook) rookTile.getPiece(), 
                                                               rookTile.getTileCoordinate(), 
                                                               61));
                    }
                }
            }

            // queen side castle
            if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied() && !this.board.getTile(57).isTileOccupied()){
                Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()){
                    if (Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(57, opponentLegals).isEmpty()
                    ){
                        kingCastles.add(new QueenSideCastleMove(board, 
                                                                playerKing, 
                                                                58, 
                                                                (Rook) rookTile.getPiece(), 
                                                                rookTile.getTileCoordinate(), 
                                                                59));

                    }
                }
            }
        }

        return kingCastles;
    }
    
}
