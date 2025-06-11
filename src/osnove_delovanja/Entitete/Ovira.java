package osnove_delovanja.Entitete;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import osnove_delovanja.Razno.Konstante;
import java.awt.Color;

public class Ovira extends Entitete {
    public Ovira(Point2D.Double startPos) {
        super(startPos);
    }

    @Override
    public void update() {
        pos.y += Konstante.HITROSTOVIRE;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Konstante.BARVAOVIRE);
        int s = Konstante.OVIRASTEVILO;
        g.fillRect((int)pos.x, (int)pos.y, s, s);
    }

    @Override protected double getWidth()  { return Konstante.OVIRASTEVILO; }
    @Override protected double getHeight() { return Konstante.OVIRASTEVILO; }
}