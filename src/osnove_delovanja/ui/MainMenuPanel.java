package osnove_delovanja.ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    public MainMenuPanel(Runnable onStart) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        JLabel title = new JLabel("Moja Igra");
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Začni igro");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(e -> onStart.run());

        add(Box.createVerticalGlue());
        add(title);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(startButton);
        add(Box.createVerticalGlue());
    }
}
