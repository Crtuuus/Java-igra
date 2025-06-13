package osnove_delovanja.Entitete;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import osnove_delovanja.Game_panel;
import osnove_delovanja.Razno.Konstante;

public class Strelec extends Nasprotnik {
    private long lastShotTime = 0;
    private Igralec target;

    public Strelec(Point2D.Double startPos, Igralec target) {
        super(startPos, Konstante.STRELEC_VELIKOST, Konstante.STRELEC_VELIKOST, OblikaTip.KROG);
        this.target = target;
    }

    public long getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(long time) {
        this.lastShotTime = time;
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastShotTime >= Konstante.STRELEC_FIRE_RATE) {
            streljaj();
            lastShotTime = now;
        }
    }

    private void streljaj() {
        if (target == null) return;

        Point2D.Double origin = new Point2D.Double(
            getPos().x + getWidth() / 2,
            getPos().y + getHeight() / 2
        );
        Izstrelek izstrelek = new Izstrelek(origin, target.getPos(), false);
        Game_panel.addEntityFromStaticContext(izstrelek); // metoda mora biti definirana
    }
    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(pos.x, pos.y, getWidth(), getHeight());
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.RED);
        var b = getBounds();
        g2.fillOval((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
    }
}
