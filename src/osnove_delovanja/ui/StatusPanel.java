package osnove_delovanja.ui;
import javax.swing.*;
import java.awt.*;
public class StatusPanel extends JPanel{
    JLabel sc=new JLabel("Score:0"),lv=new JLabel("Lives:3");
    public StatusPanel(){setLayout(new FlowLayout(FlowLayout.LEFT));add(sc);add(Box.createHorizontalStrut(20));add(lv);}
    public void update(int s,int l){sc.setText("Score:"+s);lv.setText("Lives:"+l);}    
}