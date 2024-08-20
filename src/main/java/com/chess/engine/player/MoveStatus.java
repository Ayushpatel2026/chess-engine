package com.chess.engine.player;

// once we make a move and create a transition board, we need to know if the move was successful
// if it was successful, we set the current game board to the transition board
public enum MoveStatus {
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }
    },
    ILLEGAL_MOVE{
        @Override
        public boolean isDone(){
            return false;
        }
    }, 
    LEAVES_PLAYER_IN_CHECK{
        @Override
        public boolean isDone(){
            return false;
        }
    };

    public abstract boolean isDone();
}
