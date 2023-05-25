import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;

public class CustomGameOfLife {
    private static final int WIDTH = 25;
    private static final int HEIGHT = 25;
    private static final int CELL_SIZE = 30;
    private static final int DELAY = 100;


    public static void main(String[] args) {
        Random random = new Random();
        int numCells = WIDTH * HEIGHT;
        int[] initialState = new int[numCells * 2];
        for (int i = 0; i < numCells*2; i+=2) {
            initialState[i] = random.nextInt(WIDTH);
            initialState[i + 1] = random.nextInt(HEIGHT);
        }


        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Custom Game of Life");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            String survivalRules = "34";
            String birthRules = "34";

            CustomGameOfLifeBoard board = new CustomGameOfLifeBoard(WIDTH, HEIGHT, CELL_SIZE, initialState, survivalRules, birthRules);

            frame.add(board);

            frame.pack();
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            Timer timer = new Timer(DELAY, e -> {
                if (!board.nextGeneration()) {
                    ((Timer) e.getSource()).stop();
                }
                board.repaint();
            });

            timer.start();
        });
    }
}

class CustomGameOfLifeBoard extends JPanel {
    private final int width;
    private final int height;
    private final int cellSize;
    private int[][] board;
    private final Set<Integer> survivalRules;
    private final Set<Integer> birthRules;
    private final Set<String> previousStates;

    public CustomGameOfLifeBoard(int width, int height, int cellSize, int[] initialState, String survivalRules, String birthRules) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.survivalRules = parseRules(survivalRules);
        this.birthRules = parseRules(birthRules);
        this.previousStates = new HashSet<>();

        board = new int[width][height];
        for (int i = 0; i < initialState.length; i += 2) {
            int x = initialState[i];
            int y = initialState[i + 1];
            board[x][y] = 1;
        }

        setPreferredSize(new Dimension(width * cellSize, height * cellSize));
    }



    private Set<Integer> parseRules(String rules) {
        Set<Integer> parsedRules = new HashSet<>();
        for (char c : rules.toCharArray()) {
            parsedRules.add(Character.getNumericValue(c));
        }
        return parsedRules;
    }

    public boolean nextGeneration() {
        int[][] newBoard = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int liveNeighbors = countNeighbors(x, y);
                if (board[x][y] == 1 && survivalRules.contains(liveNeighbors)) {
                    newBoard[x][y] = 1;
                } else if (board[x][y] == 0 && birthRules.contains(liveNeighbors)) {
                    newBoard[x][y] = 1;
                }
            }
        }


        String currentState = boardToString(newBoard);
        if (previousStates.contains(currentState)) {
            return false;
        }

        previousStates.add(currentState);
        boolean isBoardChanged = !Arrays.deepEquals(board, newBoard);
        board = newBoard;

        return isBoardChanged;
    }

    private String boardToString(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                sb.append(board[x][y]);
            }
        }
        return sb.toString();
    }

    private int countNeighbors(int x, int y) {
        int totalNeighbors = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                totalNeighbors += board[(x + i + width) % width][(y + j + height) % height];
            }
        }

        return totalNeighbors;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (board[x][y] == 1) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);

                g.setColor(Color.GRAY);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }
}

