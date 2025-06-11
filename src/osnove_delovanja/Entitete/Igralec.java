package osnove_delovanja.Entitete;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.Color;
import osnove_delovanja.Razno.Konstante;

public class Igralec extends Entitete {
    private Point2D.Double target;
    public Igralec(Point2D.Double startPos) {
        super(startPos);
        this.target = new Point2D.Double(startPos.x, startPos.y);
    }

    /**
     * Update position toward the current target.
     */
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

    /**
     * Draw the player as a circle.
     */
    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillOval((int)pos.x - 10, (int)pos.y - 10, 20, 20);
    }

    @Override
    protected double getWidth()  { return 20; }
    @Override
    protected double getHeight() { return 20; }

    /**
     * Set new movement target (e.g., mouse position).
     */
    public void setTarget(Point2D.Double t) {
        this.target = t;
    }
}