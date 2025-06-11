package osnove_delovanja.Entitete;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class Entitete {
    protected Point2D.Double pos;
    private boolean alive = true;

    public Entitete(Point2D.Double startPos) {
        this.pos = startPos;
    }

    public abstract void update();
    public abstract void draw(Graphics2D g);

    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(pos.x, pos.y, getWidth(), getHeight());
    }

    protected abstract double getWidth();
    protected abstract double getHeight();

    public boolean isAlive() { return alive; }
    public void setAlive(boolean a) { this.alive = a; }
}