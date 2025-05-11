import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snake implements GameConstants {
    private List<Point> body;
    private Direction direction;
    
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public Snake() {
        reset();
    }

    public void reset() {
        body = new ArrayList<>();
        int startX = GRID_WIDTH / 2;
        int startY = GRID_HEIGHT / 2;
        body.add(new Point(startX, startY));
        body.add(new Point(startX - 1, startY));
        body.add(new Point(startX - 2, startY));
        direction = Direction.RIGHT;
    }

    public void move() {
        Point head = getHead();
        Point newHead = new Point(head);
        
        switch(direction) {
            case UP -> newHead.y--;
            case DOWN -> newHead.y++;
            case LEFT -> newHead.x--;
            case RIGHT -> newHead.x++;
        }
        
        if (newHead.x < 0) newHead.x = GRID_WIDTH - 1;
        if (newHead.x >= GRID_WIDTH) newHead.x = 0;
        if (newHead.y < 0) newHead.y = GRID_HEIGHT - 1;
        if (newHead.y >= GRID_HEIGHT) newHead.y = 0;
        
        body.add(0, newHead);
        body.remove(body.size() - 1);
    }

    public void grow() {
        body.add(new Point(getTail()));
    }

    public boolean collidesWithSelf() {
        Point head = getHead();
        return body.subList(1, body.size()).contains(head);
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point segment : body) {
            g.fillRect(segment.x * TILE_SIZE, segment.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        
        // Draw eyes
        g.setColor(Color.BLACK);
        Point head = getHead();
        int eyeSize = TILE_SIZE / 5;
        
        switch(direction) {
            case UP, DOWN -> {
                g.fillRect(head.x * TILE_SIZE + 5, head.y * TILE_SIZE + 5, eyeSize, eyeSize);
                g.fillRect(head.x * TILE_SIZE + 15, head.y * TILE_SIZE + 5, eyeSize, eyeSize);
            }
            case LEFT, RIGHT -> {
                g.fillRect(head.x * TILE_SIZE + 5, head.y * TILE_SIZE + 5, eyeSize, eyeSize);
                g.fillRect(head.x * TILE_SIZE + 5, head.y * TILE_SIZE + 15, eyeSize, eyeSize);
            }
        }
    }

    public Point getHead() { return body.get(0); }
    public Point getTail() { return body.get(body.size() - 1); }
    public List<Point> getBody() { return body; }
    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }
}