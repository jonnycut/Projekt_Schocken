import Datenbank.Datenbank;
import gui.GUI;
import Grafik.Grafik;
import gui.SpielerPanel;
import spiel.Runde;
import spiel.Spieler;
import spiel.Stock;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dfleuren on 23.05.2016.
 */
public class Main {
    //testAttribute
    static List<Spieler> teilnehmer = new ArrayList<>();


    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
       /*new GUI();

        Datenbank.dbErstellen();*/


       /* Icon icon2 = Datenbank.getInstance().selectProfilBild("jochen");


        JOptionPane.showMessageDialog(
                null, "Tolles Bild aus DB",
                "Bildanzeige",
                JOptionPane.INFORMATION_MESSAGE,
                icon2
        );*/

        teilnehmer.add(Datenbank.getInstance().selectSpieler("jochen"));
        teilnehmer.add(Datenbank.getInstance().selectSpieler("Opa"));
        teilnehmer.add(new Spieler("Alex", Grafik.AVATAR_C3PO));


        JFrame outline = new JFrame();
        JPanel grundPanel = new JPanel(new GridLayout(1, 3));

        Runde runde = new Runde(new Stock(),null);
        grundPanel.add(new SpielerPanel(teilnehmer.get(0),runde));
        grundPanel.add(new SpielerPanel(teilnehmer.get(1),runde));
        grundPanel.add(new SpielerPanel(teilnehmer.get(2),runde));


        runde.setTeilnehmer(teilnehmer);



        outline.add(grundPanel);
        outline.setVisible(true);
        outline.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        outline.setPreferredSize(new Dimension(1024, 768));
        outline.pack();
    }

}
