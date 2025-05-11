import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystem {
    private List<Particle> particles = new ArrayList<>();
    private Random random = new Random();

    public void createExplosion(Point center, Color color) {
        for (int i = 0; i < 20; i++) {
            particles.add(new Particle(
                center.x * GameConstants.TILE_SIZE + GameConstants.TILE_SIZE/2,
                center.y * GameConstants.TILE_SIZE + GameConstants.TILE_SIZE/2,
                random.nextFloat() * 4 - 2,
                random.nextFloat() * 4 - 2,
                color
            ));
        }
    }

    public void update() {
        particles.removeIf(p -> p.lifetime <= 0);
        particles.forEach(Particle::update);
    }

    public void draw(Graphics g) {
        particles.forEach(p -> p.draw(g));
    }

    public void clear() {
        particles.clear();
    }

    private static class Particle {
        float x, y, vx, vy;
        int lifetime = 60;
        Color color;

        Particle(float x, float y, float vx, float vy, Color color) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.color = color;
        }

        void update() {
            x += vx;
            y += vy;
            lifetime--;
            vx *= 0.95;
            vy *= 0.95;
        }

        void draw(Graphics g) {
            float alpha = (float) lifetime / 60;
            g.setColor(new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                (int)(alpha * 255)
            ));
            int size = 2 + lifetime / 20;
            g.fillOval((int)x, (int)y, size, size);
        }
    }
}