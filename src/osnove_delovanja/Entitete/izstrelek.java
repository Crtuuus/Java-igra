package osnove_delovanja.Entitete;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Point2D;
import osnove_delovanja.Razno.Konstante;

public class izstrelek extends Entitete {
    private Point2D.Double vel;

    public izstrelek(Point2D.Double startPos, Point2D.Double target) {
        super(calcSpawn(startPos, target));
        double dx = target.x - startPos.x;
        double dy = target.y - startPos.y;
        double dist = Math.hypot(dx, dy);
        this.vel = new Point2D.Double(
            dx/dist * Konstante.IZSTRELKIHITROST,
            dy/dist * Konstante.IZSTRELKIHITROST
        );
    }

    private static Point2D.Double calcSpawn(Point2D.Double s, Point2D.Double t) {
        double dx = t.x - s.x;
        double dy = t.y - s.y;
        double dist = Math.hypot(dx, dy);
        double off = 12;
        return new Point2D.Double(s.x + dx/dist*off, s.y + dy/dist*off);
    }

    @Override
    public void update() {
        pos.x += vel.x; pos.y += vel.y;
        if (pos.x<0||pos.x>Konstante.WIDTH||pos.y<0||pos.y>Konstante.HEIGHT)
            setAlive(false);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.YELLOW);
        int s = 20;
        g.fillOval((int)pos.x-s/2, (int)pos.y-s/2, s, s);
    }

    @Override protected double getWidth()  { return 6; }
    @Override protected double getHeight() { return 6; }
}