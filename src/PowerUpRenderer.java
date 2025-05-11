import java.awt.*;

public class PowerUpRenderer implements GameConstants {
    public static void draw(PowerUp powerUp, Graphics g) {
        if (!powerUp.isActive()) return;

        Point pos = powerUp.getPosition();
        int x = pos.x * TILE_SIZE;
        int y = pos.y * TILE_SIZE;
        int size = TILE_SIZE - 4;

        switch (powerUp.getType()) {
            case SPEED_BOOST -> {
                g.setColor(new Color(0, 200, 255));
                g.fillOval(x + 2, y + 2, size, size);
                g.setColor(Color.WHITE);
                g.drawString("⚡", x + TILE_SIZE/2 - 5, y + TILE_SIZE/2 + 5);
            }
            case DOUBLE_POINTS -> {
                g.setColor(new Color(255, 215, 0));
                g.fillRect(x + 2, y + 2, size, size);
                g.setColor(Color.BLACK);
                g.drawString("2X", x + TILE_SIZE/2 - 7, y + TILE_SIZE/2 + 5);
            }
            case INVINCIBILITY -> {
                g.setColor(new Color(150, 0, 255));
                int[] xPoints = {x + TILE_SIZE/2, x + 2, x + TILE_SIZE - 2};
                int[] yPoints = {y + 2, y + TILE_SIZE - 2, y + TILE_SIZE - 2};
                g.fillPolygon(xPoints, yPoints, 3);
                g.setColor(Color.WHITE);
                g.drawString("✧", x + TILE_SIZE/2 - 5, y + TILE_SIZE/2 + 5);
            }
        }

        if ((powerUp.getDuration() / 5) % 2 == 0) {
            g.setColor(new Color(255, 255, 255, 100));
            g.fillOval(x - 2, y - 2, TILE_SIZE + 4, TILE_SIZE + 4);
        }
    }
}