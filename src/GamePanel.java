import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements GameConstants {
    private Snake snake;
    private Food food;
    private boolean gameOver;
    private int score;
    private int highScore;
    private List<Point> walls;

    public GamePanel(Snake snake, Food food) {
        this.snake = snake;
        this.food = food;
        this.walls = new ArrayList<>();
        generateWalls();
        
        setPreferredSize(new Dimension(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
    }

    private void generateWalls() {
        // Border walls
        for (int x = 0; x < GRID_WIDTH; x++) {
            walls.add(new Point(x, 0));
            walls.add(new Point(x, GRID_HEIGHT - 1));
        }
        for (int y = 1; y < GRID_HEIGHT - 1; y++) {
            walls.add(new Point(0, y));
            walls.add(new Point(GRID_WIDTH - 1, y));
        }

        // Internal obstacles
        for (int x = 5; x < 15; x++) walls.add(new Point(x, 5));
        for (int y = 5; y < 10; y++) walls.add(new Point(10, y));
    }

    public boolean isWall(Point position) {
        return walls.contains(position);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw grid
        g.setColor(new Color(30, 30, 30));
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                g.drawRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        // Draw walls
        for (Point wall : walls) {
            g.setColor(WALL_COLOR);
            g.fillRect(wall.x * TILE_SIZE, wall.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g.setColor(WALL_BORDER_COLOR);
            g.drawRect(wall.x * TILE_SIZE, wall.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw food
        food.draw(g);

        // Draw snake
        snake.draw(g);

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("High Score: " + highScore, 10, 40);

        // Draw game over screen
        if (gameOver) {
            g.setColor(new Color(255, 255, 255, 200));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            String message = "Game Over!";
            int textWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (getWidth() - textWidth) / 2, getHeight() / 2 - 30);

            g.setFont(new Font("Arial", Font.PLAIN, 24));
            message = "Final Score: " + score;
            textWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (getWidth() - textWidth) / 2, getHeight() / 2 + 20);

            message = "Press SPACE to restart";
            textWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (getWidth() - textWidth) / 2, getHeight() / 2 + 60);
        }
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
        repaint();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}
