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
        setBackground(Color.BLACK);
        this.spielfeld = spielfeld;

        info = new JLabel();
        info.setForeground(Color.RED);
        info.setBackground(Color.BLACK);

        add(info);
    }


    public void zeigeInfos(String infos){

        info.setText(infos);
        this.add(info);
        spielfeld.setAnzeige(this);
    }
}
