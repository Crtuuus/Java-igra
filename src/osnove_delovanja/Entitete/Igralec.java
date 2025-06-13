package osnove_delovanja.Entitete;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import osnove_delovanja.Razno.Konstante;

public class Igralec extends Entitete {
    private Point2D.Double target;
    private Point2D.Double lastPos;  // remember last position
    private int lives = Konstante.ZIVLJENJA;

    // Constructor
    public Igralec(Point2D.Double startPos) {
        super(startPos, Konstante.IGRALEC_VELIKOST, Konstante.IGRALEC_VELIKOST, OblikaTip.KROG);
        this.target = new Point2D.Double(startPos.x, startPos.y);
        this.speed = Konstante.IGRALCIHITROST;
    }

    public void setTarget(Point2D.Double t) {
        this.target = t;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.CYAN);
        Rectangle2D b = getBounds();
        g2.fillOval((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
    }

    @Override
    public void update() {
        lastPos = new Point2D.Double(pos.x, pos.y);
        double dx = target.x - pos.x;
        double dy = target.y - pos.y;
        double dist = Math.hypot(dx, dy);
        if (dist > 1) {
            pos.x += (dx / dist) * speed;
            pos.y += (dy / dist) * speed;
        }
    }

    public void revertLastMove() {
        pos = new Point2D.Double(lastPos.x, lastPos.y);
    }

    public void loseLife() {
        lives--;
    }

    public int getLives() {
        return lives;
    }

    @Override
    public double getWidth() {
        return Konstante.IGRALEC_VELIKOST;
    }

    @Override
    public double getHeight() {
        return Konstante.IGRALEC_VELIKOST;
    }
}
