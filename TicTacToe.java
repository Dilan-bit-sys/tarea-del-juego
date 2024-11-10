import java.io.File;
import java.util.Timer;

import javax.sound.sampled.AudioSystem;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.text.AttributeSet.ColorAttribute;
import javax.swing.text.AttributeSet.FontAttribute;

import org.w3c.dom.events.MouseEvent;

public class TicTacToe {
    private char[][] board;
    private char currentPlayer;

    public TicTacToe() {
        board = new char[3][3];
        currentPlayer = 'X';
    }

    public boolean placePiece(int row, int col) {
        if (board[row][col] == '\0') {
            board[row][col] = currentPlayer;
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            return true;
        }
        return false;
    }

    public char checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '\0' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return board[i][0];
            if (board[0][i] != '\0' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return board[0][i];
        }
        if (board[0][0] != '\0' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return board[0][0];
        if (board[0][2] != '\0' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return board[0][2];
        return '\0'; 
    }

    public boolean isBoardFull() {
        for (char[] row : board) for (char cell : row) if (cell == '\0') return false;
        return true;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }
}
public class Piece {
    private int x, y;
    private char type; 
    private boolean isAnimating;

    public Piece(int x, int y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.isAnimating = true;
    }

    public void move() {
        if (isAnimating) {
            x += (int) (Math.random() * 5);
            y += (int) (Math.random() * 5);
            if (x >= 100 || y >= 100) isAnimating = false; 
        }
    }

    public char getType() {
        return type;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
public class board extends JPanel {
    private Piece[][] pieces = new Piece[3][3];
    private TicTacToe game;
    private Timer timer;

    public board(TicTacToe game) {
        this.game = game;
        setLayout(new gridlayoud (3, 3, 5, 5)); 
        setBackground(Color.WHITE);
        setPreferredSize
        (new DimensionUIResource(300, 300));
        addmauselistener (new mauseadpter() {
            public void mousecliked (MouseEvent e) {
                int row = e.getClientY() / 100;
                int col = e.getClientX() / 100;
                if (game.placePiece(row, col)) {
                    pieces[row][col] = new Piece(col * 100, row * 100, game.getCurrentPlayer());
                    playSound("move.wav"); 
                    startAnimation();
                    checkGameStatus();
                }
            }
        });
    }
    protected void paintComponent(GroupLayout g) {
        super.paintComponent(g);
        g.setColor(ColorAttribute.black);
        for (int i = 1; i < 3; i++) {
            g.drawLine(i * 100, 0, i * 100, getHeight());
            g.drawLine(0, i * 100, getWidth(), i * 100);
        }
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (pieces[row][col] != null) {
                    Piece piece = pieces[row][col];
                    g.setfront(new FontAttribute("Arial", FontAttribute.board, 40));
                    g.drawString(String.valueOf(piece.getType()), piece.getX() + 30, piece.getY() + 70);
                }
            }
        }
    }
    private void startAnimation() {
        timer = new timer(10, e -> {
            boolean animating = false;
            for (Piece[] row : pieces) {
                for (Piece piece : row) {
                    if (piece != null && piece.isAnimating()) {
                        piece.move();
                        animating = true;
                    }
                }
            }
            repaint();
            if (!animating) timer.stop();
        });
        timer.start();
    }

    private void checkGameStatus() {
        char winner = game.checkWinner();
        if (winner != '\0') {
            JOptionPane.showMessageDialog(null, game);(this, "ganador: " + winner);
            resetGame();
        } else if (game.isBoardFull()) {
            JOptionPane.showMessageDialog(null, game);(this,"Empate");
            resetGame();
        }
    }

    private void resetGame() {
        pieces = new Piece[3][3];
        game = new TicTacToe();
        repaint();
    }

    private void playSound(String soundFile) {
        try {
            clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(soundFile)));
            clip.start();
        } catch (Exception e) {
            System.err.println("Error al reproducir sonido: " + e.getMessage());
        }
    }
}
public class GameFrame extends JFrame {
    public GameFrame() {
        TicTacToe game = new TicTacToe();
        board board = new board(game);
        add(board);
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe - Pool Inspired");
        setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
