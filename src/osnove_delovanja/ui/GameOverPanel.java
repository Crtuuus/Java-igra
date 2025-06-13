package osnove_delovanja.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameOverPanel extends JPanel {
    public GameOverPanel(ActionListener onBackToMenu) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("GAME OVER", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 40));
        add(label, BorderLayout.CENTER);

        JButton backButton = new JButton("Nazaj v meni");
        backButton.addActionListener(onBackToMenu);
        add(backButton, BorderLayout.SOUTH);
    }
}
