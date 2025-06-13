package osnove_delovanja.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(Runnable onStart) {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("Moja Igra", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));

        JButton startButton = new JButton("Začni igro");
        startButton.addActionListener(e -> onStart.run());

        add(title, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
    }
}
