package osnove_delovanja.Entitete;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Point2D;
import osnove_delovanja.Razno.Konstante;

public class izstrelek extends Entitete{
    private Point2D.Double vel;

    public izstrelek(Point2D.Double startPos, Point2D.Double target) {
        super(calcSpawnPoint(startPos, target));
        double dx = target.x - startPos.x;
        double dy = target.y - startPos.y;
        double dist = Math.hypot(dx, dy);
        this.vel = new Point2D.Double(
            (dx / dist) * Konstante.BULLET_SPEED,
            (dy / dist) * Konstante.BULLET_SPEED
        );
    }

    private static Point2D.Double calcSpawnPoint(Point2D.Double startPos, Point2D.Double target) {
        double dx = target.x - startPos.x;
        double dy = target.y - startPos.y;
        double dist = Math.hypot(dx, dy);
        double offset = 12; // spawn bullet just outside player radius
        double x = startPos.x + (dx / dist) * offset;
        double y = startPos.y + (dy / dist) * offset;
        return new Point2D.Double(x, y);
    }

    @Override
    public void update() {
        pos.x += vel.x;
        pos.y += vel.y;
        if (pos.x < 0 || pos.x > Konstante.WIDTH
         || pos.y < 0 || pos.y > Konstante.HEIGHT) {
            setAlive(false);
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.YELLOW);
        int size = 6;
        g.fillOval((int)pos.x - size/2, (int)pos.y - size/2, size, size);
    }

    @Override protected double getWidth()  { return 6;  }
    @Override protected double getHeight() { return 6;  }
}