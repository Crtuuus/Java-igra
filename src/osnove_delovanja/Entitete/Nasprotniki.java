package osnove_delovanja.Entitete;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Point2D;
import osnove_delovanja.Razno.Konstante;

public class Nasprotniki extends Entitete {
    private Igralec player;

    public Nasprotniki(Point2D.Double startPos, Igralec player) {
        super(startPos);
        this.player = player;
    }

    @Override
    public void update() {
        double dx = player.getPos().x - pos.x;
        double dy = player.getPos().y - pos.y;
        double dist = Math.hypot(dx, dy);
        if (dist > 0) {
            pos.x += (dx/dist) * Konstante.SOVRAZNIKIHITROST;
            pos.y += (dy/dist) * Konstante.SOVRAZNIKIHITROST;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        int s = 20;
        g.fillRect((int)pos.x - s/2, (int)pos.y - s/2, s, s);
    }

    @Override protected double getWidth()  { return 20; }
    @Override protected double getHeight() { return 20; }
}