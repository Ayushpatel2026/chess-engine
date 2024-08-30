package com.chess.gui;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.board.Move.PawnPromotion;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.Player;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.MoveStrategy;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.*;

public class Table extends Observable{
    private final JFrame gameFrame;
    public final BoardPanel boardPanel;
    private final GameHistoryPanel gameHistoryPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final MoveLog moveLog;
    private final GameSetup gameSetup;

    public Board chessBoard;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    BoardDirection boardDirection;
    private boolean highLightLegalMoves;
    
    private static String defaultPieceImagesPath = "art/pieces/simple/";

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private  final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    
    Color lightTileColor = Color.decode("#FFFACD");
    Color darkTileColor = Color.decode("#593E1A");
    private Move computerMove;

    private static final Table INSTANCE = new Table();

    public static Table get(){
        return INSTANCE;
    }

    public void show(){
       Table.get().moveLog.clear();
       Table.get().gameHistoryPanel.redo(chessBoard, Table.get().moveLog);
       Table.get().takenPiecesPanel.redo(Table.get().moveLog);
       Table.get().boardPanel.drawBoard(chessBoard);
    }

    public void flipBoard(){
        Collections.reverse(boardPanel.boardTiles);
        boardPanel.drawBoard(chessBoard);
        boardDirection = boardDirection.opposite();
        Table.get().takenPiecesPanel.redo(moveLog);
    }

   
    private Table(){
        this.gameFrame = new JFrame("JChess");
        this.chessBoard = Board.createStandardBoard();

        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.highLightLegalMoves = false;
        this.boardPanel = new BoardPanel();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.boardDirection = BoardDirection.NORMAL;
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setVisible(true);
    }

    private GameSetup getGameSetup(){
        return this.gameSetup;
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("Open up that png file");
            }
        });
        fileMenu.add(openPGN);


        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                gameFrame.dispose();
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private JMenu createPreferencesMenu(){
        final JMenu preferencesMenu = new JMenu("Preferences");

        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                flipBoard();
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();

        final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", false);
        legalMoveHighlighterCheckbox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                highLightLegalMoves = legalMoveHighlighterCheckbox.isSelected();
            }   
        });
        preferencesMenu.add(legalMoveHighlighterCheckbox);
        return preferencesMenu;
    }

    private JMenu createOptionsMenu(){
        final JMenu optionsMenu = new JMenu("Play");

        final JMenuItem setupGameMenuItem = new JMenuItem("Setup New Game");
        setupGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });

        optionsMenu.add(setupGameMenuItem);
        return optionsMenu;
    }

    // use of the observer pattern to tell the ai to move
    private void setupUpdate(GameSetup gameSetup){
        setChanged();
        notifyObservers(gameSetup);
    }

    private static class TableGameAIWatcher implements Observer{
        @Override
        public void update(Observable o, Object arg){
            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) &&
            !Table.get().getGameBoard().currentPlayer().isInCheckMate() &&
            !Table.get().getGameBoard().currentPlayer().isInStaleMate()){
                final AIThinkTank thinkTank = new AIThinkTank(Table.get().getGameSetup().getSearchDepth());
                thinkTank.execute();
            }

            if (Table.get().getGameBoard().currentPlayer().isInCheckMate()) {
                String message;
                if (Table.get().getGameBoard().currentPlayer().getAlliance().isBlack()) {
                    message = "Game Over, Black is in checkmate! White WINS!";
                } else {
                    message = "Game Over, White is in checkmate! Black WINS!";
                }
            
                int response = JOptionPane.showOptionDialog(
                        Table.get().getGameFrame(),
                        message + "\nWould you like to play again?",
                        "Checkmate",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Play Again", "Exit"},
                        "Play Again");
            
                if (response == JOptionPane.YES_OPTION) {
                    Table.get().getGameSetup().promptUser();
                    Table.get().setupUpdate(Table.get().getGameSetup());
                } else {
                    System.exit(0);
                }
            }
            
            if (Table.get().getGameBoard().currentPlayer().isInStaleMate()) {
                int response = JOptionPane.showOptionDialog(
                        Table.get().getGameFrame(),
                        "Game Over, Stalemate!\nWould you like to play again?",
                        "Stalemate",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Play Again", "Exit"},
                        "Play Again");
            
                if (response == JOptionPane.YES_OPTION) {
                    Table.get().getGameSetup().promptUser();
                    Table.get().setupUpdate(Table.get().getGameSetup());
                } else {
                    System.exit(0);
                }
            }
        }
    }

    public JFrame getGameFrame(){
        return this.gameFrame;
    }

    public void updateGameBoard(Board board){
        this.chessBoard = board;
    }

    public void updateComputerMove(Move move){
        this.computerMove = move;
    }

    public void moveMadeUpdate(PlayerType playerType){
        setChanged();
        notifyObservers(playerType);
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    private static class AIThinkTank extends SwingWorker<Move, String>{
        private int searchDepth;

        private AIThinkTank(int searchDepth){
            this.searchDepth = searchDepth;
        }

        @Override
        protected Move doInBackground() throws Exception {
            // Start time
            long startTime = System.currentTimeMillis();

            // Run the minimax algorithm
            final MoveStrategy miniMax = new MiniMax(searchDepth);
            final Move bestMove = miniMax.execute(Table.get().getGameBoard());

            // End time
            long endTime = System.currentTimeMillis();

            // Calculate the elapsed time
            long elapsedTime = endTime - startTime;

            // Ensure the method takes at least 1 second (1000 milliseconds)
            if (elapsedTime < 1000) {
                try {
                    Thread.sleep(1000 - elapsedTime);
                } catch (InterruptedException e) {
                    // Handle the interruption
                    Thread.currentThread().interrupt();
                }
            }

            return bestMove;
        }

        @Override
        public void done(){
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().moveLog.addMove(bestMove);
                Table.get().gameHistoryPanel.redo(Table.get().getGameBoard(), Table.get().moveLog);
                Table.get().takenPiecesPanel.redo(Table.get().moveLog);
                Table.get().boardPanel.drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public enum BoardDirection{
        NORMAL{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles){
                return boardTiles;
            }

            @Override
            BoardDirection opposite(){
                return FLIPPED;
            }
        },
        FLIPPED{
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles){
                Collections.reverse(boardTiles);
                return boardTiles;
            }

            @Override
            BoardDirection opposite(){
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }

    class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;

        BoardPanel(){
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();

            for (int i = 0; i < BoardUtils.NUM_TILES; i++){
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(Board board){
            removeAll();
            for (final TilePanel tilePanel : boardTiles){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public static class MoveLog{
        private final List<Move> moves;

        MoveLog(){
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves(){
            return this.moves;
        }

        public void addMove(Move move){
            this.moves.add(move);
        }

        public int size(){
            return this.moves.size();
        }

        public void clear(){
            this.moves.clear();
        }

        public Move removeMove(int index){
            return this.moves.remove(index);
        }

        public boolean removeMove(Move move){
            return this.moves.remove(move);
        }
    }

    enum PlayerType{
        HUMAN,
        COMPUTER
    }

    private static Piece getPromotionPiece(Move move){
        // want to be able to underpromote to a knight, bishop, or rook
        // prompt the user to choose the promotion piece
        // and return the corresponding piece based on their choice
        String[] options = {"Knight", "Bishop", "Rook", "Queen"};
        int choice = JOptionPane.showOptionDialog(
            Table.get().getGameFrame(),
            "Choose promotion piece:",
            "Pawn Promotion",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,   
            options,
            options[3]); // Default selection is "Queen"

        switch (choice) {
            case 0: // Knight
                return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
            case 1: // Bishop
                return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
            case 2: // Rook
                return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
            default: // Queen
                return new Queen(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance(), false);
        }
    }

    private static Move showPromotionOptions(PawnPromotion move){
        Piece promotionPiece = getPromotionPiece(move);
        return new Move.PawnPromotion(move.getDecoratedMove(), promotionPiece);
    }

    private class TilePanel extends JPanel{
        private final int tileId;

        TilePanel(BoardPanel boardPanel, 
                  int tileId
        ){
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isRightMouseButton(e)){
                        sourceTile = null;
                        humanMovedPiece = null;

                    }else if (isLeftMouseButton(e)){
                        // first click - source tile is null
                        if (sourceTile == null){
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();

                            // clicked tile does not have a piece
                            if (humanMovedPiece == null){
                                sourceTile = null;
                            }
                        }else{
                            // second click
                            destinationTile = chessBoard.getTile(tileId);
                            final Move move = MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());

                            if (humanMovedPiece instanceof Pawn && 
                                (humanMovedPiece.getPieceAlliance().isWhite() && 
                                BoardUtils.SEVENTH_RANK[sourceTile.getTileCoordinate()] && 
                                BoardUtils.EIGHTH_RANK[destinationTile.getTileCoordinate()]) ||
                                (humanMovedPiece.getPieceAlliance().isBlack() &&
                                BoardUtils.SECOND_RANK[sourceTile.getTileCoordinate()] &&
                                BoardUtils.FIRST_RANK[destinationTile.getTileCoordinate()])
                                ) {
                                
                                // Show the promotion options only when moving to the 8th rank
                                Move promotionMove = Table.showPromotionOptions((PawnPromotion) move);
                                if (promotionMove != null) {
                                    final MoveTransition transition = chessBoard.currentPlayer().makeMove(promotionMove);
                                    if (transition.getMoveStatus().isDone()) {
                                        chessBoard = transition.getTransitionBoard();
                                        moveLog.addMove(promotionMove);
                                    } else {
                                        System.out.println("Invalid move");
                                    }
                                }

                            } else {
                                final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                                if (transition.getMoveStatus().isDone()) {
                                    chessBoard = transition.getTransitionBoard();
                                    moveLog.addMove(move);
                                } else {
                                    System.out.println("Invalid move");
                                }
                            }

                            sourceTile = null;
                            humanMovedPiece = null;
                        }
                        invokeLater(new Runnable(){
                            @Override
                            public void run(){
                                gameHistoryPanel.redo(chessBoard, moveLog);
                                takenPiecesPanel.redo(moveLog);
                                boardPanel.drawBoard(chessBoard);
                                Table.get().moveMadeUpdate(PlayerType.HUMAN);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    
                }
            });
            validate();
        }

        private void highLightLegals(Board board){
            if (highLightLegalMoves){
                for (Move move : pieceLegalMoves(board)){
                    if (move.getDestinationCoordinate() == this.tileId){
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(Board board){
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance()){
                Collection<Move> legalMoves = board.currentPlayer().getLegalMoves();
                Collection<Move> actualLegalMoves = calculateLegalMoves(legalMoves, board.currentPlayer().getOpponent().getLegalMoves());
                Collection<Move> pieceLegalMoves = new ArrayList<>();
                for (Move move : actualLegalMoves){
                    if (move.getMovedPiece().equals(humanMovedPiece)){
                        pieceLegalMoves.add(move);
                    }
                }
                return pieceLegalMoves;
            }
            return Collections.emptyList();
        }

        public Collection<Move> calculateLegalMoves(Collection<Move> allLegalMoves, Collection<Move> opponentMoves) {
            Collection<Move> actualLegalMoves = new ArrayList<>();
        
            for (Move move: allLegalMoves){
                // TODO if it is a pawn promotion move add it to the list without executing (this is a bug that needs to be fixed)
                if (move instanceof PawnMove.PawnPromotion){
                    PawnPromotion pawnPromotion = (PawnPromotion) move;
                    move = pawnPromotion.getDecoratedMove();
                }

                Board transitionBoard = move.execute();

                Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());

                if (kingAttacks.isEmpty()){
                    actualLegalMoves.add(move);
                } 
            }

            return actualLegalMoves;
        }

        public void drawTile(Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highLightLegals(board);
            validate();
            repaint();
        }

        private void assignTileColor() {
            if (BoardUtils.EIGHTH_RANK[this.tileId] ||
            BoardUtils.SIXTH_RANK[this.tileId] ||
            BoardUtils.FOURTH_RANK[this.tileId] ||
            BoardUtils.SECOND_RANK[this.tileId]
            ){
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            }else if(
                BoardUtils.SEVENTH_RANK[this.tileId] ||
                BoardUtils.FIFTH_RANK[this.tileId] ||
                BoardUtils.THIRD_RANK[this.tileId] ||
                BoardUtils.FIRST_RANK[this.tileId]
            ){
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);

            }
        }

        private void assignTilePieceIcon(Board board){
            this.removeAll();
            if (board.getTile(tileId).isTileOccupied()){
                String pieceIconPath = defaultPieceImagesPath;
                try {
                    BufferedImage image = ImageIO.read(new File(pieceIconPath+board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + board.getTile(tileId).getPiece().toString() + ".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }  
}
