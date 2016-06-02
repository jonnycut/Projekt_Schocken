package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by U.F.O on 23.05.2016.
 * Diese Klasse ist für das Spielfeld um dem Spieler Informationen mitzuteilen.
 *
 * @author DFleuren
 */
public class Infobereich extends JPanel {

    /**
     * Das Spielfeld, zu dem der Infobereich gehört
     */
    private Spielfeld spielfeld;

    /**
     * Das JPanel in dem das JLabel mit der Information steckt.
     */
    private JPanel jPInfo;

    /**
     * Das JLabel mit der Information.
     */
    private JLabel info;

    /**<pre>
     * Constructor des Infobereiches.
     * Erwartet ein Spielfeld, damit er weis, zu wem er gehört
     * </pre>
     * @param spielfeld
     * @see Spielfeld
     */
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
     * Erzeugt ein JLabel um allen Spielern Informationen zukommen zu lassen.
     * @param infos String beinhaltet den Informationstext der angezeigt werden soll.<br></br>
     * Es wird zum Schluss wieder in den Infobereich eingefügt.
     */
    public void setInfos(String infos) {
        //ToDo: brauchen wir das auskommentierte noch? wenn nicht, wurd nämlich nichts neu erzeugt und die Beschreibung muss angepasst werden. KNA
        info.setText(infos);
        JPanel jPCenter = new JPanel(new FlowLayout());
        jPCenter.setBackground(Color.BLACK);
        jPCenter.add(info);
        jPInfo.add(jPCenter, BorderLayout.NORTH);
        jPInfo.add(info, BorderLayout.NORTH);

        add(jPInfo);
    }
}
