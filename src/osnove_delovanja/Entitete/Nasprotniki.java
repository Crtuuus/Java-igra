package osnove_delovanja.Entitete;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Point2D;
import osnove_delovanja.Razno.Konstante;

public class Nasprotniki extends Entitete {
    public Nasprotniki(Point2D.Double startPos) {
        super(startPos);
    }

    @Override
    public void update() {
        pos.y += Konstante.ENEMY_SPEED;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        int size = 20;
        g.fillRect((int)pos.x - size/2, (int)pos.y - size/2, size, size);
    }

    @Override protected double getWidth()  { return 20; }
    @Override protected double getHeight() { return 20; }
}