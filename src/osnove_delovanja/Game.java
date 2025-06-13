package osnove_delovanja;

import osnove_delovanja.ui.MainMenuPanel;
import osnove_delovanja.ui.StatusPanel;
import osnove_delovanja.ui.GameOverPanel;

import javax.swing.*;
import java.awt.*;

public class Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Moja Igra");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Glavni panel z CardLayout
            CardLayout cl = new CardLayout();
            JPanel mainPanel = new JPanel(cl);

            StatusPanel statusPanel = new StatusPanel();

            // Glavni meni
            Game_panel[] gamePanelRef = new Game_panel[1]; // hack za dostop znotraj lambda

            MainMenuPanel menu = new MainMenuPanel(() -> {
                if (gamePanelRef[0] != null) {
                    gamePanelRef[0].resetGame();
                    cl.show(mainPanel, "game");
                }
            });

            // Game Over Panel
            GameOverPanel gameOverPanel = new GameOverPanel(e -> cl.show(mainPanel, "menu"));

            // Igra Panel
            Game_panel gamePanel = new Game_panel(() -> {
                cl.show(mainPanel, "gameover");
            }, statusPanel);
            gamePanelRef[0] = gamePanel;

            // Dodaj panele v mainPanel
            mainPanel.add(menu, "menu");
            mainPanel.add(gamePanel, "game");
            mainPanel.add(gameOverPanel, "gameover");

            // Postavitev
            frame.setLayout(new BorderLayout());
            frame.add(mainPanel, BorderLayout.CENTER);
            frame.add(statusPanel, BorderLayout.SOUTH);
            frame.setSize(1200, 1260);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // Prikaz začetnega menija
            cl.show(mainPanel, "menu");
        });
    }
}
