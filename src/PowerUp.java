import java.awt.Point;
import java.util.Random;

public class PowerUp implements GameConstants {
    private Point position;
    private PowerUpType type;
    private boolean active;
    private int duration;
    private Random random;
    
    public enum PowerUpType {
        SPEED_BOOST,
        DOUBLE_POINTS,
        INVINCIBILITY
    }

    public PowerUp() {
        this.random = new Random();
        this.active = false;
        respawn();
    }

    public void activate() {
        this.active = true;
        this.duration = 300;
        this.type = randomType();
    }

    public void deactivate() {
        this.active = false;
    }

    public void update() {
        if (active && --duration <= 0) {
            deactivate();
        }
    }

    public void respawn() {
        this.position = new Point(
            random.nextInt(GRID_WIDTH - 4) + 2,
            random.nextInt(GRID_HEIGHT - 4) + 2
        );
    }

    private PowerUpType randomType() {
        PowerUpType[] types = PowerUpType.values();
        return types[random.nextInt(types.length)];
    }

    public Point getPosition() { return position; }
    public PowerUpType getType() { return type; }
    public boolean isActive() { return active; }
    public int getDuration() { return duration; }
    public void setPosition(Point position) { this.position = position; }
}