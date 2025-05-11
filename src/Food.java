import java.awt.*;
import java.util.Random;

public class Food implements GameConstants {
    private Point position;
    private FoodType type;
    private Random random;
    private int blinkTimer;
    private boolean visible;
    
    public enum FoodType {
        NORMAL(10, Color.RED, "normal.wav"),
        GOLDEN(30, Color.YELLOW, "gold.wav"),
        ROTTEN(-5, new Color(139, 69, 19), "rotten.wav"),
        BONUS(15, Color.CYAN, "bonus.wav") {
            @Override
            public boolean shouldRespawn() {
                return false;
            }
        };

        private final int scoreValue;
        private final Color color;
        private final String soundFile;

        FoodType(int scoreValue, Color color, String soundFile) {
            this.scoreValue = scoreValue;
            this.color = color;
            this.soundFile = soundFile;
        }

        public int getScoreValue() { return scoreValue; }
        public Color getColor() { return color; }
        public String getSoundFile() { return soundFile; }
        public boolean shouldRespawn() { return true; }
    }

    public Food() {
        this.random = new Random();
        this.blinkTimer = 0;
        this.visible = true;
        respawn();
    }

    public void respawn() {
        position = new Point(
            random.nextInt(GRID_WIDTH - 2) + 1,
            random.nextInt(GRID_HEIGHT - 2) + 1
        );
        
        double rand = random.nextDouble();
        if (rand < 0.05) type = FoodType.GOLDEN;
        else if (rand < 0.1) type = FoodType.ROTTEN;
        else if (rand < 0.15) type = FoodType.BONUS;
        else type = FoodType.NORMAL;
    }

    public void update() {
        if (type == FoodType.BONUS) {
            blinkTimer++;
            if (blinkTimer % 10 == 0) visible = !visible;
        }
    }

    public void draw(Graphics g) {
        if (!visible && type == FoodType.BONUS) return;
        
        int padding = 4;
        int size = TILE_SIZE - 2 * padding;
        g.setColor(type.getColor());
        
        switch (type) {
            case NORMAL -> g.fillOval(
                position.x * TILE_SIZE + padding,
                position.y * TILE_SIZE + padding,
                size, size);
            case GOLDEN -> {
                g.fillOval(
                    position.x * TILE_SIZE + padding,
                    position.y * TILE_SIZE + padding,
                    size, size);
                g.setColor(Color.WHITE);
                g.drawString("$", 
                    position.x * TILE_SIZE + TILE_SIZE/2 - 3,
                    position.y * TILE_SIZE + TILE_SIZE/2 + 5);
            }
            case ROTTEN -> {
                int[] xPoints = {
                    position.x * TILE_SIZE + padding,
                    position.x * TILE_SIZE + TILE_SIZE - padding,
                    position.x * TILE_SIZE + TILE_SIZE/2
                };
                int[] yPoints = {
                    position.y * TILE_SIZE + padding,
                    position.y * TILE_SIZE + padding,
                    position.y * TILE_SIZE + TILE_SIZE - padding
                };
                g.fillPolygon(xPoints, yPoints, 3);
            }
            case BONUS -> {
                g.fillRect(
                    position.x * TILE_SIZE + padding,
                    position.y * TILE_SIZE + padding,
                    size, size);
                g.setColor(Color.BLACK);
                g.drawString("!", 
                    position.x * TILE_SIZE + TILE_SIZE/2 - 3,
                    position.y * TILE_SIZE + TILE_SIZE/2 + 5);
            }
        }
    }

    public Point getPosition() { return position; }
    public FoodType getType() { return type; }
    public int getScoreValue() { return type.getScoreValue(); }
    public String getSoundFile() { return type.getSoundFile(); }
    public boolean shouldRespawn() { return type.shouldRespawn(); }
}