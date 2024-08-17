package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils {
    
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGTH_COLUMN = initColumn(7);

    public static final boolean[] EIGHTH_RANK = init_row(0);
    public static final boolean[] SEVENTH_RANK = init_row(8);
    public static final boolean[] SIXTH_RANK = init_row(16);
    public static final boolean[] FIFTH_RANK = init_row(24);
    public static final boolean[] FOURTH_RANK = init_row(32);
    public static final boolean[] THIRD_RANK = init_row(40);
    public static final boolean[] SECOND_RANK = init_row(48);
    public static final boolean[] FIRST_RANK = init_row(56);

    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();

    public static final int NUM_TILES = 64;
    public static boolean isValidCoordinate(int coordinate){
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    private static String[] initializeAlgebraicNotation() {
        return new String[]{
            "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
            "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
            "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
            "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
            "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
            "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
            "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
            "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++){
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return positionToCoordinate;
    }

    public static boolean[] initColumn(int colNumber){
        boolean[] column = new boolean[NUM_TILES];
        do{
            column[colNumber] = true;
            colNumber += 8;
        } while(colNumber < NUM_TILES);
        return column;
    }

    public static boolean[] init_row(int rowNumber){
        boolean[] row = new boolean[NUM_TILES];
        for (int i = rowNumber; i < rowNumber + 8; i++){
            row[i] = true;
        }
        return row;
    }

    public static String getPositionAtCoordinate(int destinationCoordinate) {
        return ALGEBRAIC_NOTATION[destinationCoordinate]; 
    }

    public static int getCoordinateAtPosition(String position) {
        return POSITION_TO_COORDINATE.get(position);
    }   
}
