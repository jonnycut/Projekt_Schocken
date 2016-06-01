package gui;

import Datenbank.Datenbank;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Created by dfleuren on 23.05.2016.
 * Diese Klasse ist für das Spielfeld um dem Spieler Informationen mitzuteilen.
 */
public class Infobereich extends JPanel {

    private Spielfeld spielfeld;
    private JPanel jPInfo;
    private JLabel info;

    public Infobereich(Spielfeld spielfeld){
        super();
        this.spielfeld = spielfeld;


        //-------------------------------------SUPER-PANEL-------------------------------------------------------------

        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(810, 150));
        setBackground(Color.BLACK);


        //-------------------------------------MITTE-PANEL-------------------------------------------------------------

        jPInfo = new JPanel(new BorderLayout());
        jPInfo.setBackground(Color.BLACK);
        info = new JLabel("TEST");
        info.setFont(new Font("Arial", Font.BOLD, 20));
        info.setForeground(Color.RED);
        info.setBackground(Color.BLACK);

        jPInfo.add(info, BorderLayout.NORTH);

        JPanel temp = new JPanel();
        temp.setBackground(Color.BLACK);
        temp.setPreferredSize(new Dimension(810, 50));
        jPInfo.add(temp,BorderLayout.CENTER);


        //-------------------------------------SUPER-PANEL-------------------------------------------------------------

        add(jPInfo);
    }


    //------------------------------------------METHODEN---------------------------------------------------------------

    /**
     * Erzeugt eine JLabel und allen Spielern Informationen zu kommen zu lassen.
     * @param infos String beinhaltet den Informationstext der angezeigt werden soll.
     * Es wird zum Schluss wieder in den Infobereich eingefügt.
     */
    public void setInfos(String infos) {

        info.setText(infos);
//        JPanel jPCenter = new JPanel(new FlowLayout());
//        jPCenter.setBackground(Color.BLACK);
//        jPCenter.add(info);
//        jPInfo.add(jPCenter, BorderLayout.NORTH);
        jPInfo.add(info, BorderLayout.NORTH);

        istSpielleiter();

        add(jPInfo);

        System.out.println("Flurry:  " + infos);
    }


    /**
     * Erzeugt ein JLabel, welches anzeigt wer der Spielleiter ist.
     * Es wird die SpielID aus der Datenbank abgerufen und mit dieser ID
     * wird in der Datenbank geprüft wer der Spielleiter des Spiels ist.
     * Dann wird es in das JPanel jPInfo eingefügt.
     */
    public void istSpielleiter() {
        int spielID = 0;
        try {
            spielID = Datenbank.getInstance().selectSpielID(spielfeld.getGui().getIch());
            System.out.println(spielID);
            JLabel spielleiter = new JLabel("Spielleiter:  " + Datenbank.getInstance().selectSpielleiterKennung(spielID));
            spielleiter.setForeground(Color.RED);
            spielleiter.setBackground(Color.BLACK);
            jPInfo.add(spielleiter,BorderLayout.SOUTH);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
