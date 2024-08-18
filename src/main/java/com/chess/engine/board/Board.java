package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;
    private final Pawn enPassantPawn;


    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);        
        this.enPassantPawn = builder.enPassantPawn;
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }

    public Pawn getEnPassantPawn(){
        return this.enPassantPawn;
    }

    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    private static List<Tile> createGameBoard(Builder builder){
        List<Tile> tiles = new ArrayList<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++){
            tiles.add(Tile.createTile(i, builder.boardConfig.getOrDefault(i, null)));
        }

        return Collections.unmodifiableList(tiles);
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer(){
        return this.currentPlayer;
    }

    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK, true, true));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6, Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));

        //White Layout
        builder.setPiece(new Rook(63, Alliance.WHITE));
        builder.setPiece(new Knight(62, Alliance.WHITE));
        builder.setPiece(new Bishop(61, Alliance.WHITE));
        builder.setPiece(new Queen(59, Alliance.WHITE));
        builder.setPiece(new King(60, Alliance.WHITE, true, true));
        builder.setPiece(new Bishop(58, Alliance.WHITE));
        builder.setPiece(new Knight(57, Alliance.WHITE));
        builder.setPiece(new Rook(56, Alliance.WHITE));
        builder.setPiece(new Pawn(55, Alliance.WHITE));
        builder.setPiece(new Pawn(54, Alliance.WHITE));
        builder.setPiece(new Pawn(53, Alliance.WHITE));
        builder.setPiece(new Pawn(52, Alliance.WHITE));
        builder.setPiece(new Pawn(51, Alliance.WHITE));
        builder.setPiece(new Pawn(50, Alliance.WHITE));
        builder.setPiece(new Pawn(49, Alliance.WHITE));
        builder.setPiece(new Pawn(48, Alliance.WHITE));

        builder.setMoveMaker(Alliance.WHITE);
        
        return builder.build();
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance){
        List<Piece> activePieces = new ArrayList<>();

        for (final Tile tile: gameBoard){
            if (tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance){
                    activePieces.add(piece);
                }
            }
        }

        return Collections.unmodifiableList(activePieces);
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces){
        List<Move> legalMoves = new ArrayList<>();

        for (final Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }

        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < BoardUtils.NUM_TILES; i++){
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % 8 == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    // using the Builder design pattern

    public static class Builder {
        Map<Integer, Piece> boardConfig;
        Pawn enPassantPawn;

        // keep track of whose turn it is
        Alliance nextMoveMaker; 

        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }

        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build(){
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn) {
            this.enPassantPawn = enPassantPawn;
        }
    }

    public Iterable<Move> getAllLegalMoves() {

        List<Move> allLegalMoves = new ArrayList<>();

        allLegalMoves.addAll(this.whitePlayer.getLegalMoves());
        allLegalMoves.addAll(this.blackPlayer.getLegalMoves());

        return Collections.unmodifiableList(allLegalMoves);
    }

    public Collection<Piece> getAllPieces() {
        List<Piece> allPieces = new ArrayList<>();
        allPieces.addAll(this.whitePieces);
        allPieces.addAll(this.blackPieces);
        return Collections.unmodifiableList(allPieces);
    }

    public Piece getPiece(int i) {
        return this.gameBoard.get(i).getPiece();
    }
}
