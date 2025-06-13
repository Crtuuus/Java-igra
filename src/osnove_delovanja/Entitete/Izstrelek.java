package osnove_delovanja.Entitete;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import osnove_delovanja.Razno.Konstante; 

public class Izstrelek extends Entitete {
    private Point2D.Double vel;
    private final boolean friendly;

    // Konstruktor za izstrelke igralca
    public Izstrelek(Point2D.Double start, Point2D.Double target) {
        this(start, target, true);
    }

    // Polni konstruktor
    public Izstrelek(Point2D.Double start, Point2D.Double target, boolean friendly) {
        super(start);
        this.friendly = friendly;
        double dx = target.x - start.x;
        double dy = target.y - start.y;
        double d = Math.hypot(dx, dy);
        vel = new Point2D.Double((dx / d) * Konstante.IZSTRELKIHITROST,
                                 (dy / d) * Konstante.IZSTRELKIHITROST);
    }

    @Override
    public Shape getOblika() {
    	return new Ellipse2D.Double(pos.x, pos.y, getWidth(), getHeight());
    	}

    @Override
    public void update() {
        pos.x += vel.x;
        pos.y += vel.y;

        // Ubij izstrelek, če zapusti zaslon
        if (pos.x < -10 || pos.x > Konstante.WIDTH + 10 ||
            pos.y < -10 || pos.y > Konstante.HEIGHT + 10) {
            setAlive(false);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(friendly ? Color.YELLOW : Color.RED);
        var b = getBounds();
        g2.fillOval((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
    }

    public boolean isFriendly() {
        return friendly;
    }

    @Override
    public double getWidth() {
        return Konstante.IZSTRELEK_SIZE;
    }

    @Override
    public double getHeight() {
        return Konstante.IZSTRELEK_SIZE;
    }
}
