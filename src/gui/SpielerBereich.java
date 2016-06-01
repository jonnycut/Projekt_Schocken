package gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by dfleuren on 01.06.2016.
 */
public class SpielerBereich extends JPanel {


    public SpielerBereich(List<SpielerPanel> teilnehmer){
        super();
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(1020, 530));
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createMatteBorder(8, 0, 0, 0, Color.BLACK));

        for(SpielerPanel s : teilnehmer){
            add(s);
        }


    }

}
