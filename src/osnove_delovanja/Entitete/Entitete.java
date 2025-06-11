package osnove_delovanja.Entitete;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;

public abstract class Entitete {
    protected Point2D.Double pos;
    protected boolean alive = true;

    public Entitete(Point2D.Double startPos) {
        this.pos = startPos;
    }

    /**
     * Update this entity's state (movement, AI, etc.)
     */
    public abstract void update();

    /**
     * Draw this entity to the screen.
     */
    public abstract void draw(Graphics2D g);

    /**
     * Axis-aligned bounding box for collision.
     */
    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(pos.x, pos.y, getWidth(), getHeight());
    }

    protected abstract double getWidth();
    protected abstract double getHeight();

    public boolean isAlive() {
        return alive;
    }
}