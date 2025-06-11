package osnove_delovanja.Entitete;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Point2D;
import osnove_delovanja.Razno.Konstante;

public class Igralec extends Entitete {
    private Point2D.Double target;
    private Point2D.Double prevPos;

    public Igralec(Point2D.Double startPos) {
        super(startPos);
        this.target = new Point2D.Double(startPos.x, startPos.y);
        this.prevPos = new Point2D.Double(startPos.x, startPos.y);
    }

    @Override
    public void update() {
        prevPos.x = pos.x; prevPos.y = pos.y;
        double dx = target.x - pos.x;
        double dy = target.y - pos.y;
        double dist = Math.hypot(dx, dy);
        if (dist > 1) {
            pos.x += (dx/dist) * Konstante.IGRALCIHITROST;
            pos.y += (dy/dist) * Konstante.IGRALCIHITROST;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        int s = 20;
        g.fillOval((int)pos.x - s/2, (int)pos.y - s/2, s, s);
    }

    @Override protected double getWidth()  { return 20; }
    @Override protected double getHeight() { return 20; }

    public void setTarget(Point2D.Double t) { this.target = t; }
    public Point2D.Double getPos() { return pos; }
    public void revertLastMove() { pos.setLocation(prevPos); }
}