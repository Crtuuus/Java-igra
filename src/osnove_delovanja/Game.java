package osnove_delovanja;

import java.awt.BorderLayout;

import javax.swing.*;
import osnove_delovanja.Razno.Konstante;
import osnove_delovanja.ui.StatusPanel;

public class Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Moja Igra");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout()); // Dodaj to vrstico!

            StatusPanel statusPanel = new StatusPanel();
            Game_panel gamePanel = new Game_panel();
            gamePanel.setStatusPanel(statusPanel);

            frame.add(gamePanel, BorderLayout.CENTER); // Pravilno dodajanje
            frame.add(statusPanel, BorderLayout.SOUTH); // Pravilno dodajanje

            frame.setSize(Konstante.WIDTH, Konstante.HEIGHT + 60);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
