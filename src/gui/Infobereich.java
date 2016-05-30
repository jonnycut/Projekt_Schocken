package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class Infobereich extends JPanel {

    Spielfeld spielfeld;
    JLabel info;

    public Infobereich(Spielfeld spielfeld){
        super();
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(760, 150));
        setBackground(Color.BLACK);
        this.spielfeld = spielfeld;

        info = new JLabel("Willkommen beim SCHOKEN");
        info.setFont(new Font("Arial", Font.BOLD, 30));
        info.setForeground(Color.RED);
        info.setBackground(Color.BLACK);

        add(info);
    }


    public void zeigeInfos(String infos){

        info.setText(infos);
        this.add(info);
        //spielfeld.setAnzeige(this);
    }
}
