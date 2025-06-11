package osnove_delovanja.Entitete;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Point2D;
import osnove_delovanja.Razno.Konstante;

public class Igralec extends Entitete{
    private Point2D.Double target;

    public Igralec(Point2D.Double startPos) {
        super(startPos);
        this.target = new Point2D.Double(startPos.x, startPos.y);
    }

    @Override
    public void update() {
        double dx = target.x - pos.x;
        double dy = target.y - pos.y;
        double dist = Math.hypot(dx, dy);
        if (dist > 1) {
            pos.x += (dx / dist) * Konstante.PLAYER_SPEED;
            pos.y += (dy / dist) * Konstante.PLAYER_SPEED;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        int size = 20;
        g.fillOval((int)pos.x - size/2, (int)pos.y - size/2, size, size);
    }

    @Override protected double getWidth()  { return 20; }
    @Override protected double getHeight() { return 20; }

    public void setTarget(Point2D.Double t) {
        this.target = t;
    }

    public Point2D.Double getPos() {
        return pos;
    }
}