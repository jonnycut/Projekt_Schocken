package gui;

import Datenbank.Datenbank;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class Infobereich extends JPanel {

    private Spielfeld spielfeld;
    private JPanel jPInfo;
    private JLabel info;

    public Infobereich(Spielfeld spielfeld){
        super();
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(810, 150));
        setBackground(Color.BLACK);

        this.spielfeld = spielfeld;

        jPInfo = new JPanel(new BorderLayout());
        jPInfo.setBackground(Color.BLACK);
        info = new JLabel();
        info.setFont(new Font("Arial", Font.BOLD, 20));
        info.setForeground(Color.RED);
        info.setBackground(Color.BLACK);

        jPInfo.add(info, BorderLayout.NORTH);

        JPanel temp = new JPanel();
        temp.setBackground(Color.BLACK);
        temp.setPreferredSize(new Dimension(810, 50));
        jPInfo.add(temp,BorderLayout.CENTER);

        add(jPInfo);
    }


    public void setInfos(String infos) throws SQLException, ClassNotFoundException {

        info.setText(infos);
        JPanel jPCenter = new JPanel(new FlowLayout());
        jPCenter.setBackground(Color.BLACK);
        jPCenter.add(info);
        jPInfo.add(jPCenter,BorderLayout.NORTH);

        istSpielleiter();

        add(jPInfo);
    }

    public void istSpielleiter() throws SQLException, ClassNotFoundException {
            int spielID = Datenbank.getInstance().selectSpielID(spielfeld.getGui().getIch());
            JLabel spielleiter = new JLabel("Spielleiter ist:  " + Datenbank.getInstance().selectSpielleiterKennung(spielID));
            spielleiter.setForeground(Color.RED);
            spielleiter.setBackground(Color.BLACK);
            jPInfo.add(spielleiter,BorderLayout.SOUTH);

    }
}
