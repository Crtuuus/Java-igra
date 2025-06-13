package osnove_delovanja.Entitete;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.Polygon;
import java.util.UUID;

import osnove_delovanja.Razno.Konstante;

public abstract class Entitete {
    public enum OblikaTip { KROG, PRAVOKOTNIK, TRIKOTNIK, SESTKOTNIK }

    protected Point2D.Double pos;
    protected double width, height;
    protected OblikaTip oblikaTip;
    protected final UUID id = UUID.randomUUID();
    protected boolean alive = true;
    protected double speed = 0;

    // Main constructor
    public Entitete(double x, double y, double width, double height, OblikaTip oblikaTip) {
        this.pos = new Point2D.Double(x, y);
        this.width = width;
        this.height = height;
        this.oblikaTip = oblikaTip;
    }

    // Overloaded: using Point2D
    public Entitete(Point2D.Double pos, double width, double height, OblikaTip oblikaTip) {
        this(pos.x, pos.y, width, height, oblikaTip);
    }

    // Convenience: for default player-like circle
    public Entitete(Point2D.Double pos) {
        this(pos.x, pos.y, Konstante.IGRALEC_VELIKOST, Konstante.IGRALEC_VELIKOST, OblikaTip.KROG);
    }

    public Shape getOblika() {
        switch (oblikaTip) {
            case KROG:
                return new Ellipse2D.Double(pos.x, pos.y, width, height);
            case PRAVOKOTNIK:
                return new Rectangle2D.Double(pos.x, pos.y, width, height);
            case TRIKOTNIK:
                int[] xt = {
                    (int)(pos.x + width / 2),
                    (int)pos.x,
                    (int)(pos.x + width)
                };
                int[] yt = {
                    (int)pos.y,
                    (int)(pos.y + height),
                    (int)(pos.y + height)
                };
                return new Polygon(xt, yt, 3);
            case SESTKOTNIK:
                int s = (int)(width / 2);
                int h = (int)(Math.sqrt(3) * s);
                int cx = (int)(pos.x + s);
                int cy = (int)(pos.y + h / 2);
                int[] xs = { cx - s, cx - s/2, cx + s/2, cx + s, cx + s/2, cx - s/2 };
                int[] ys = { cy, cy - h/2, cy - h/2, cy, cy + h/2, cy + h/2 };
                return new Polygon(xs, ys, 6);
            default:
                return new Rectangle2D.Double(pos.x, pos.y, width, height);
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(pos.x, pos.y, width, height);
    }
    
    // Getter / Setter methods
    public Point2D.Double getPos() { return pos; }

    public void setPos(Point2D.Double p) { this.pos = p; }

    public boolean isAlive() { return alive; }

    public void setAlive(boolean b) { this.alive = b; }

    public double getWidth() { return width; }

    public double getHeight() { return height; }

    public double getSpeed() { return speed; }

    public UUID getId() { return id; }

    public abstract void update();

    public abstract void draw(java.awt.Graphics2D g);
}
