package osnove_delovanja.ui;
import javax.swing.*;
import java.awt.event.*;
public class MenuPanel extends JPanel {
    public MenuPanel(ActionListener a){ JButton b=new JButton("Start");b.addActionListener(a);add(b);}    
}