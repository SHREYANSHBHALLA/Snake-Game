import java.awt.Point;
import java.awt.Color;

public class GameLoop implements GameConstants {
    private Snake snake;
    private Food food;
    private PowerUp powerUp;
    private GamePanel panel;
    private boolean running;
    private int score;
    private int highScore;
    private Difficulty difficulty;
    private ScoreManager scoreManager;
    private ParticleSystem particles;
    private long powerUpEndTime;
    private boolean doublePointsActive;
    private boolean speedBoostActive;
    private boolean invincibilityActive;
    private int framesSinceLastFood;

    public GameLoop(Snake snake, Food food, GamePanel panel, Difficulty difficulty) {
        this.snake = snake;
        this.food = food;
        this.panel = panel;
        this.difficulty = difficulty;
        this.powerUp = new PowerUp();
        this.scoreManager = new ScoreManager();
        this.particles = new ParticleSystem();
        this.highScore = scoreManager.getTopScores(1).stream()
                          .findFirst()
                          .map(entry -> entry.getValue())
                          .orElse(0);
    }

    // Getter for the Snake instance
    public Snake getSnake() {
        return snake;
    }

    public void start() {
        running = true;
        new Thread(() -> {
            long lastUpdate = System.currentTimeMillis();
            int speed = difficulty.gameSpeed;
            
            while (running) {
                long now = System.currentTimeMillis();
                if (now - lastUpdate >= speed) {
                    update();
                    panel.repaint();
                    lastUpdate = now;
                    speed = speedBoostActive ? difficulty.gameSpeed / 2 : difficulty.gameSpeed;
                }
                try { Thread.sleep(1); } 
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        }).start();
    }

    private void update() {
        if (!running) return;

        food.update();
        particles.update();
        framesSinceLastFood++;
        checkPowerUpStatus();

        Point nextPosition = calculateNextPosition();
        if (checkWallCollision(nextPosition) || checkSelfCollision(nextPosition)) {
            endGame();
            return;
        }

        snake.move();
        checkFoodCollision();
        checkPowerUpCollision();

        if (framesSinceLastFood > 100 && !powerUp.isActive()) {
            spawnPowerUp();
        }
    }

    private Point calculateNextPosition() {
        Point head = snake.getHead();
        Point next = new Point(head);
        
        switch(snake.getDirection()) {
            case UP -> next.y--;
            case DOWN -> next.y++;
            case LEFT -> next.x--;
            case RIGHT -> next.x++;
        }
        
        if (next.x < 0) next.x = GRID_WIDTH - 1;
        if (next.x >= GRID_WIDTH) next.x = 0;
        if (next.y < 0) next.y = GRID_HEIGHT - 1;
        if (next.y >= GRID_HEIGHT) next.y = 0;
        
        return next;
    }

    private boolean checkWallCollision(Point position) {
        return !invincibilityActive && panel.isWall(position);
    }

    private boolean checkSelfCollision(Point position) {
        return !invincibilityActive && 
               snake.getBody().subList(1, snake.getBody().size()).contains(position);
    }

    private void checkFoodCollision() {
        if (snake.getHead().equals(food.getPosition())) {
            SoundManager.playSound(food.getSoundFile());
            addScore(food.getScoreValue());
            particles.createExplosion(food.getPosition(), food.getType().getColor());
            
            int growth = food.getType() == Food.FoodType.GOLDEN ? 2 : 1;
            for (int i = 0; i < growth; i++) snake.grow();
            
            if (food.shouldRespawn()) {
                food.respawn();
                framesSinceLastFood = 0;
            } else {
                framesSinceLastFood = 100;
            }
        }
    }

    private void checkPowerUpCollision() {
        if (powerUp.isActive() && snake.getHead().equals(powerUp.getPosition())) {
            SoundManager.playSound("powerup.wav");
            particles.createExplosion(powerUp.getPosition(), Color.MAGENTA);
            
            switch(powerUp.getType()) {
                case SPEED_BOOST -> speedBoostActive = true;
                case DOUBLE_POINTS -> doublePointsActive = true;
                case INVINCIBILITY -> invincibilityActive = true;
            }
            powerUpEndTime = System.currentTimeMillis() + 8000;
            powerUp.respawn();
        }
    }

    private void checkPowerUpStatus() {
        if (System.currentTimeMillis() > powerUpEndTime) {
            speedBoostActive = false;
            doublePointsActive = false;
            invincibilityActive = false;
        }
    }

    private void spawnPowerUp() {
        Point position;
        do {
            position = new Point(
                (int)(Math.random() * (GRID_WIDTH - 2)) + 1,
                (int)(Math.random() * (GRID_HEIGHT - 2)) + 1
            );
        } while (snake.getBody().contains(position) || 
                 panel.isWall(position) || 
                 position.equals(food.getPosition()));
        
        powerUp.setPosition(position);
        powerUp.activate();
        framesSinceLastFood = 0;
    }

    private void addScore(int basePoints) {
        int points = doublePointsActive ? basePoints * 2 : basePoints;
        score += points;
        highScore = Math.max(score, highScore);
        panel.setScore(score);
        panel.setHighScore(highScore);
    }

    private void endGame() {
        running = false;
        SoundManager.playSound("gameover.wav");
        panel.setGameOver(true);
        scoreManager.saveScore("Player", score);
    }

    public void reset() {
        snake.reset();
        food.respawn();
        powerUp.respawn();
        score = 0;
        running = false;
        doublePointsActive = false;
        speedBoostActive = false;
        invincibilityActive = false;
        panel.setScore(0);
        panel.setGameOver(false);
        particles.clear();
    }

    public boolean isRunning() { return running; }
    public int getScore() { return score; }
    public int getHighScore() { return highScore; }
    public ParticleSystem getParticles() { return particles; }
    public PowerUp getPowerUp() { return powerUp; }
    public boolean isDoublePointsActive() { return doublePointsActive; }
    public boolean isSpeedBoostActive() { return speedBoostActive; }
    public boolean isInvincibilityActive() { return invincibilityActive; }
}