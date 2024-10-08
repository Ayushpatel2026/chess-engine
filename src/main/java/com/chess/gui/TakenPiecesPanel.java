package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;
import com.chess.gui.Table.BoardDirection;
import com.chess.gui.Table.MoveLog;

public class TakenPiecesPanel extends JPanel {

    private final JPanel northPanel;
    private final JPanel southPanel;

    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(80, 80);
    
    public TakenPiecesPanel() {
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(MoveLog moveLog){
        this.southPanel.removeAll();
        this.northPanel.removeAll();

        List<Piece> whiteTakenPieces = new ArrayList<>();
        List<Piece> blackTakenPieces = new ArrayList<>();
        
        for (final Move move : moveLog.getMoves()){
            if (move.isAttack()){
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceAlliance().isWhite()){
                    whiteTakenPieces.add(takenPiece);
                } else if (takenPiece.getPieceAlliance().isBlack()){
                    blackTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("Should not reach here!");
                }
            }
        }

        Collections.sort(whiteTakenPieces, new Comparator<Piece>(){
            @Override
            public int compare(Piece o1, Piece o2) {
                return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        Collections.sort(blackTakenPieces, new Comparator<Piece>(){
            @Override
            public int compare(Piece o1, Piece o2) {
                return Integer.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        for (final Piece takenPiece : whiteTakenPieces){
            try {
                final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/pieces/simple/" + takenPiece.getPieceAlliance().toString().substring(0, 1) + "" + takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
                if (Table.get().boardDirection == BoardDirection.FLIPPED){
                    this.southPanel.add(imageLabel);
                } else {
                    this.northPanel.add(imageLabel);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        for (final Piece takenPiece : blackTakenPieces){
            try {
                final BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/pieces/simple/" + takenPiece.getPieceAlliance().toString().substring(0, 1) + "" + takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
                if (Table.get().boardDirection == BoardDirection.FLIPPED){
                    this.northPanel.add(imageLabel);
                } else {
                    this.southPanel.add(imageLabel);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        
        validate();
    }
}
