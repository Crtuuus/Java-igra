package osnove_delovanja;

import javax.swing.*;
import java.awt.*;
import osnove_delovanja.Razno.Konstante;
import osnove_delovanja.ui.MenuPanel;
import osnove_delovanja.ui.StatusPanel;

public class Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Space Shooter");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            CardLayout cl=new CardLayout();
            JPanel cont=new JPanel(cl);
            Game_panel gp=new Game_panel();
            StatusPanel sp=new StatusPanel(); gp.setStatusPanel(sp);
            cont.add(new MenuPanel(e->cl.show(cont,"G")),"M");
            JPanel pc=new JPanel(new BorderLayout());
            pc.add(gp,BorderLayout.CENTER);
            pc.add(sp,BorderLayout.SOUTH);
            cont.add(pc,"G");

            frame.add(cont);
            frame.pack();frame.setLocationRelativeTo(null);frame.setVisible(true);
            cl.show(cont,"M");
        });
    }
}