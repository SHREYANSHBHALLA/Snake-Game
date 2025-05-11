import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameWindow extends JFrame {
    private GameLoop gameLoop;
    private GamePanel gamePanel;
    private GameMenu gameMenu;
    private boolean inGame = false;

    public GameWindow() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        showMenu();
        setupKeyListener();
    }

    private void showMenu() {
        getContentPane().removeAll();
        gameMenu = new GameMenu(this);
        add(gameMenu);
        pack();
        setLocationRelativeTo(null);
        inGame = false;
    }

    public void startGame(int difficultyLevel) {
        getContentPane().removeAll();
        
        Difficulty difficulty = switch(difficultyLevel) {
            case 0 -> Difficulty.EASY;
            case 1 -> Difficulty.MEDIUM;
            case 2 -> Difficulty.HARD;
            default -> Difficulty.MEDIUM;
        };

        Snake snake = new Snake();
        Food food = new Food();
        gamePanel = new GamePanel(snake, food);
        gameLoop = new GameLoop(snake, food, gamePanel, difficulty);
        
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
        gameLoop.start();
        inGame = true;

        // Ensure the game window gets focus for key events
        requestFocusInWindow(); // <- Ensures the window accepts key events
    }

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (inGame) {
                    handleGameKeys(e);
                }
            }
        });
    }

    private void handleGameKeys(KeyEvent e) {
        Snake snake = gameLoop.getSnake();
        
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (snake.getDirection() != Snake.Direction.DOWN)
                    snake.setDirection(Snake.Direction.UP);
            }
            case KeyEvent.VK_DOWN -> {
                if (snake.getDirection() != Snake.Direction.UP)
                    snake.setDirection(Snake.Direction.DOWN);
            }
            case KeyEvent.VK_LEFT -> {
                if (snake.getDirection() != Snake.Direction.RIGHT)
                    snake.setDirection(Snake.Direction.LEFT);
            }
            case KeyEvent.VK_RIGHT -> {
                if (snake.getDirection() != Snake.Direction.LEFT)
                    snake.setDirection(Snake.Direction.RIGHT);
            }
            case KeyEvent.VK_SPACE -> {
                if (!gameLoop.isRunning()) {
                    gameLoop.reset();
                    gameLoop.start();
                }
            }
            case KeyEvent.VK_ESCAPE -> showMenu(); // To go back to the menu
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}
