package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

/*
 * The move class needs a board, the piece and the destination coordinate
 */
public abstract class Move {
    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;
    boolean isFirstMove;

    public static final Move NULL_MOVE = new NullMove();

    public Move(Board board, Piece movedPiece, int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(Board board, int destinationCoordinate){
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    public Piece getMovedPiece(){
        return movedPiece;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(Object other){
        if (this == other){
            return true;
        }

        if (!(other instanceof Move)){
            return false;
        }

        final Move otherMove = (Move) other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece()) &&
                getCurrentCoordinate() == otherMove.getCurrentCoordinate();
    }

    public boolean isAttack(){
        return false;
    }

    public boolean isCastlingMove(){
        return false;
    }

    public Piece getAttackedPiece(){
        return null;
    }

    public int getCurrentCoordinate(){
        return this.getMovedPiece().getPiecePosition();
    }

    public Board execute() {
        final Builder builder = new Builder();

        // set all of current player's pieces except for the moved piece 
        for (Piece piece: this.board.currentPlayer().getActivePieces()){
            if (!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }

        // set opponent's pieces
        for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }

        // movePiece returns a new piece with an updated piece position
        builder.setPiece(movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        
        // return a new board with the updated pieces
        return builder.build();
    }
    
    /*MajorMove is the movement of a major piece - non-attacking */
    public static final class MajorMove extends Move{
        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class MajorAttackMove extends AttackMove{
        public MajorAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPiece.getPieceType().toString() + "x" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class AttackMove extends Move{
        final Piece attackedPiece;

        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object other){
            if (this == other){
                return true;
            }

            if (!(other instanceof AttackMove)){
                return false;
            }

            AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public boolean isAttack(){
            return true;
        }
        @Override
        public Piece getAttackedPiece(){
            return attackedPiece;
        }
    }

    public static final class PawnMove extends Move{

        public PawnMove(Board board, Piece movedPiece, int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove{

        public PawnEnPassantAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece){
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
    
            // set all of current player's pieces except for the moved piece 
            for (Piece piece: this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
    
            // set opponent's pieces
            for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                // this if statement is the difference from the super execute method
                if (!piece.equals(this.getAttackedPiece())){
                    builder.setPiece(piece);
                }
            }
    
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }

    /* The pawn promotion class uses the decorator pattern
     * This is because the pawn promotion move can decorate a normal Pawn Move or a Pawn Attack Move
     * It delegates most of the work to the decorated move
     */

    public static class PawnPromotion extends Move{

        final Move decoratedMove;
        final Pawn promotedPawn;
       
        public PawnPromotion(Move decoratedMove){
            super(decoratedMove.getBoard(), decoratedMove.movedPiece, decoratedMove.destinationCoordinate);
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }

        @Override
        public int hashCode(){
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof PawnPromotion && super.equals(other);
        }

        @Override
        public boolean isAttack(){
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece(){
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public String toString(){
            return this.decoratedMove.toString() + "=Q";
        }

        @Override
        public Board execute() {
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Builder builder = new Builder();
    
            for (Piece piece: pawnMovedBoard.currentPlayer().getActivePieces()){
                builder.setPiece(piece);
            }
    
            for (Piece piece: pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()){
                if (!piece.equals(promotedPawn)){
                    builder.setPiece(piece);
                }
            }
            // need to get the promoted piece from the user and move it to the destination coordinate
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            // do not set the move maker to the opponent, because when the decorated move executed, the move maker was already set to the opponent
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
            return builder.build();
        }
    }

    // the pawn jump class is a special move because it has the ability to set an enpassant pawn
    public static final class PawnJump extends Move{

        public PawnJump(Board board, Piece movedPiece, int destinationCoordinate){
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
    
            // set all of current player's pieces except for the moved piece 
            for (Piece piece: this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
    
            // set opponent's pieces
            for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
    
            Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);

            // mark on the board that there is an enpassant pawn, this can only occur when the move is a pawn jump
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public String toString(){
            return BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    // in castle moves, the moved piece is the king
    static abstract class CastleMove extends Move{
        protected Rook castleRook;
        protected int castleRookStart;
        protected int castleRookDestination;
        
        public CastleMove(Board board, Piece movedPiece, 
                        int destinationCoordinate,
                        Rook castleRook,
                        int castleRookStart,
                        int castleRookDestination){
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook(){
            return castleRook;
        }

        @Override
        public boolean isCastlingMove(){
            return true;
        }

        @Override
        public Board execute() {
            final Builder builder = new Builder();
    
            // set all of current player's pieces except for the moved piece and the castle rook
            for (Piece piece: this.board.currentPlayer().getActivePieces()){
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)){
                    builder.setPiece(piece);
                }
            }
    
            // set opponent's pieces
            for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
    
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAlliance()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public int hashCode(){
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(Object other){
            if (this == other){
                return true;
            }

            if (!(other instanceof CastleMove)){
                return false;
            }

            CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    public static final class KingSideCastleMove extends CastleMove{
        public KingSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate,
                        Rook castleRook,
                        int castleRookStart,
                        int castleRookDestination){
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString(){
            return "O-O";
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }
    }

    public static final class QueenSideCastleMove extends CastleMove{
        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate,
                        Rook castleRook,
                        int castleRookStart,
                        int castleRookDestination){
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString(){
            return "O-O-O";
        }

        @Override
        public boolean equals(Object other){
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }
    }

    public static final class NullMove extends Move{
        public NullMove(){
            super(null, -1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("cannot execute null move");
        }

        @Override
        public int getCurrentCoordinate(){
            return -1;
        }

        @Override
        public String toString(){
            return "Null Move";
        }
    }

    public static class MoveFactory{
        private MoveFactory(){
            throw new RuntimeException("Non instantiable");
        }
        // return a move from the boards legal moves given the current and destination coordinates
        public static Move createMove(Board board, int currentCoordinate, int destinationCoordinate){
            for (Move move: board.getAllLegalMoves()){
                if (move.getCurrentCoordinate() == currentCoordinate && move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }

            return NULL_MOVE;
        }
        public static Move getNullMove() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getNullMove'");
        }
    }
    public int getDestinationCoordinate() {
       return this.destinationCoordinate;
    }

    public Board getBoard() {
        return this.board;
    }
}
