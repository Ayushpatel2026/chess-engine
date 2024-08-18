package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements MoveStrategy {

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MiniMax(int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.currentPlayer() + " THINKING with depth = " + searchDepth);

        int numMoves = board.currentPlayer().getLegalMoves().size();
        
        for (Move move: board.currentPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()){
                // if current player is white, minimize the value of the next board (where black is playing)
                currentValue = board.currentPlayer().getAlliance().isWhite() ? 
                    min(moveTransition.getTransitionBoard(), searchDepth - 1) : 
                    max(moveTransition.getTransitionBoard(), searchDepth - 1);
                
                // white wants the highest value, black wants the lowest value
                if (board.currentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.currentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;
        System.out.println("Execution time: " + executionTime + " ms, best move: " + bestMove + ", highestSeenValue: " + highestSeenValue);
        return bestMove;
    }

    // Cool algo

    public int min(Board board, int searchDepth) {
        // Base case
        //TODO or game over
        if (searchDepth == 0 || isEndGameScenario(board)){
            return this.boardEvaluator.evaluate(board, searchDepth);
        }

        // recursive case
        int lowestSeenValue = Integer.MAX_VALUE;
        // go through the current player's legal moves and make the move
        // if the move is legal, evaluate the transition board, update the lowestSeenValue
        for (Move move: board.currentPlayer().getLegalMoves()){
            
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            
            if (moveTransition.getMoveStatus().isDone()){
                final int currentValue = max(moveTransition.getTransitionBoard(), searchDepth - 1);
                if (currentValue <= lowestSeenValue){
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;  
    }

    public int max(Board board, int searchDepth) {
        //TODO or game over
        if (searchDepth == 0 || isEndGameScenario(board)){
            return this.boardEvaluator.evaluate(board, searchDepth);
        }

        int highestSeenValue = Integer.MIN_VALUE;
        for (Move move: board.currentPlayer().getLegalMoves()){
            
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            
            if (moveTransition.getMoveStatus().isDone()){
                final int currentValue = min(moveTransition.getTransitionBoard(), searchDepth - 1);
                if (currentValue >= highestSeenValue){
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }

    private static boolean isEndGameScenario(Board board) {
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStaleMate();
    }

    @Override
    public String toString() {
        return "MiniMax";
    }
    
}
