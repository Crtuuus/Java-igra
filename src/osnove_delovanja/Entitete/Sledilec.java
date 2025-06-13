package osnove_delovanja.Entitete;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import osnove_delovanja.Razno.Konstante;

public class Sledilec extends Nasprotnik {
    private Igralec player;

    public Sledilec(Point2D.Double startPos, Igralec player) {
        super(startPos, Konstante.NASPROTNIK_SIZE, Konstante.NASPROTNIK_SIZE, OblikaTip.PRAVOKOTNIK);
        this.player = player;
        this.speed = Konstante.SLEDILEC_SPEED;
    }

    @Override
    public void update() {
        double dx = player.getPos().x - pos.x;
        double dy = player.getPos().y - pos.y;
        double dist = Math.hypot(dx, dy);
        if (dist > 1) {
            pos.x += dx / dist * speed;
            pos.y += dy / dist * speed;
        }
    }
    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D.Double(pos.x, pos.y, getWidth(), getHeight());
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        var b = getBounds();
        g.fillRect((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
    }
}