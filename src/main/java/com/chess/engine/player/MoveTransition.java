package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/* The purpose of this class is to create a transition state with the board, move, and move status
 * This will allow us to make a move
 * We can then check the move status
 * We can also check the transition board to see if the move was successful
*/

public class MoveTransition {
    private final Board fromBoard;
    private final Board transitionBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;

    public MoveTransition(Board fromBoard, Board transitionBoard, Move move, MoveStatus moveStatus){
        this.transitionBoard = transitionBoard;
        this.transitionMove = move;
        this.moveStatus = moveStatus;
        this.fromBoard = fromBoard;
    }

    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }

    public Board getFromBoard() {
        return this.fromBoard;
    }

    public Move getTransitionMove() {
        return this.transitionMove;
    }
}
