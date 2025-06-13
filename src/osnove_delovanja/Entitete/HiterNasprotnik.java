package osnove_delovanja.Entitete;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import osnove_delovanja.Razno.Konstante;

public class HiterNasprotnik extends Nasprotnik {
    private Igralec player; 

    public HiterNasprotnik(Point2D.Double startPos, Igralec player) {
        super(startPos, Konstante.NASPROTNIK_SIZE, Konstante.NASPROTNIK_SIZE, OblikaTip.SESTKOTNIK);
        this.player = player; //
        this.speed = 3.0; // 
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
        g.setColor(Color.MAGENTA);
        var b = getBounds();
        g.fill(getOblika()); // to nariše šestkotnik, krog, trikotnik ali pravokotnik, glede na oblikaTip

    }
}
