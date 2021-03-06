package spiel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * <pre>
 *     Becherklasse
 *     Enthaelt wuerfel
 *     kenn die Anzahl seiner Wuerfe
 * </pre>
 * @author KNapret
 */
public class Becher implements Comparable<Becher> {
    /**
     * Liste der Würfel, die zum Ausweten genutzt wird.
     * durch Wuerfel.sortiere(), wird diese Liste immer absteoigend sortiert
     * w1 w2 w3 sind daduch immer an unterschiedlichen Positionen.
     */
    private List<Wuerfel> wuerfel = new ArrayList<>();
    /**
     * Die eigentliche Datenstruktur der Würfel,
     * in der die Würfel feste Positionen haben
     */
    private Wuerfel[] wuerfelArray = new Wuerfel[3];
    /**
     * Enthält das Bild des Wurfes
     * {Schock aus | Schock | General | Straße | Zahl}
     */
    private String bild;

    /**
     * Anzahl der durchgeführten Würfe, wird bei Becher.wuerfeln() hochgezählt
     */
    private int anzahlWuerfe;

    /**
     * <pre>
     * Erstellt einen neuen Becher:
     * Das Wuerfelarray wird mit 3 Wuerfeln gefuellt
     * Bild wird initial auf null gesetzt
     * Anzahl Wuerfe wird mit 0 initialisiert</pre>
     */
    public Becher() {

        for (int i = 0; i < 3; i++) {
            wuerfel.add(new Wuerfel());
            wuerfelArray[i]=wuerfel.get(i);
        }

        wuerfeln();

        this.anzahlWuerfe = 0;
    }

    /**
     * Getter fuer das aktuelle Bild des Bechers
     *
     * @return String : {Schock | General | Strasse | Zahl}
     */
    public String getBild() {
        return bild;
    }

    /**
     * <pre>
     *     Liefert das unsortierte WuerfelArray des Bechers,
     *     Die Würfel sind immer an der selben Position.
     * </pre>
     * @return Wuerfel {w1,w2,w3}
     */
    public Wuerfel[] getWuerfelArray(){
        return this.wuerfelArray;
    }

    /**
     * Getter fuer die Anzahl der Wuerfe des Bechers
     *
     * @return Int - 0 - 3 (Da 3 Max)
     */
    public int getAnzahlWuerfe() {
        return this.anzahlWuerfe;
    }


    /**
     * Setzt die Anzahl der getätigten Würfe zurück
     * Wird beim Auswürfeln benötigt
     * @see Spieler#beginnerWuerfeln()
     */
    public void resetWurf() {
        this.anzahlWuerfe = 0;
    }

    /**
     * Getter fuer das sortierte Wuerfelarray
     * Nur für Vergleich der Würfe geeignet, da Würfel immer
     * unterschiedliche Positionen haben!!
     *
     * @return Wuerfel[3]
     */
    public Wuerfel[] getSortierteWuerfel() {
        sortiere();
        return wuerfel.toArray(new Wuerfel[3]);
    }

    /**
     * <pre>
     * Nutzt die Wuerfel.wuerfeln() und weist damit jedem Wuerfel eine Zufallszahl
     * zwischen 1 und 6 zu.
     * Das Wuerfelarray wird absteigend sortiert.
     * Bild wird, je nach Wuerfelkombination auf
     *
     * {Schock | General | Strasse | Zahl}
     *
     * gesetzt</pre>
     */
    public void wuerfeln() {



        this.anzahlWuerfe++;

        for (Wuerfel w : this.wuerfelArray) {
            w.wuerfeln();
        }
        System.out.println("kai: Habe gewürfelt");

        sortiere();

    }

    /**<pre>
     * Setzt das wuerfelArray auf das übergebene Array,
     * Ersetzt die alte Würfelliste durch neue, welche die neuen Würfel enthält.
     * ruft sortiere() auf umd die Wuerfelliste zu sortieren
     * und das aktuelle Bild zu bestimmen.
     *
     * Wird in Spieler.setWurf(Wuerfel[]) genutzt.
     *
     *
     * </pre>
     * @param wuerfelArray Wuerfel[] Das übergebene Array, das als Würfel eingefügt wird.
     * @see Spieler#setWurf(Wuerfel[])

     */
    public void setWurf(Wuerfel[] wuerfelArray) {
        this.wuerfelArray=wuerfelArray;
        this.wuerfel = new ArrayList<Wuerfel>();
        for(Wuerfel w : wuerfelArray){
            wuerfel.add(w);
        }

        sortiere();



    }

    /**
     * Setzt die Anzahl der gemachten Würfe
     * wird beim Neuerstellen eines Spielers durch ein Netzwerkupdate benötigt
     *
     * @param anzahlWuerfe Int - Anzahl der getätigten Würfe.
     * @see Datenbank.Datenbank#selectSpieler(String)
     */

    public void setAnzahlWuerfe(int anzahlWuerfe){
        this.anzahlWuerfe=anzahlWuerfe;
    }

    /**
     * <pre>
     *     Sortiert die WuerfelListe absteigend
     *     und setzt das Aktuelle Bild auf
     *     {Schock aus | Schock | General | Straße | Zahl}
     * </pre>
     */
    public void sortiere() {
        Collections.sort(wuerfel);

        if (wuerfel.get(0).getWert() == 1 && wuerfel.get(1).getWert() == 1 && wuerfel.get(2).getWert() == 1) {
            bild = "Schock aus";

        } else if (wuerfel.get(1).getWert() == 1 && wuerfel.get(2).getWert() == 1) {
            bild = "Schock";
        } else if (wuerfel.get(0).getWert() == wuerfel.get(1).getWert() && wuerfel.get(1).getWert() == wuerfel.get(2).getWert()) {
            bild = "General";
        } else if (wuerfel.get(0).getWert() == wuerfel.get(1).getWert() + 1 && wuerfel.get(1).getWert() == wuerfel.get(2).getWert() + 1) {
            bild = "Straße";
        } else {
            bild = "Zahl";
        }


    }


    /**
     * <pre>
     * Setzt alle Wuerfel auf draussen.
     * Nutzbar, wenn Wurf in Gaenze stehen gelassen wird.
     *
     * </pre>
     */
    public void aufdecken() {

        for (Wuerfel w : this.wuerfel) {
            w.setDraussen(true);

        }
    }

    /**
     * <pre>CompareTo(ein anderer Becher)
     *  Vergleicht einen Becher mit einem anderen Becher (b2)
     *  Wertigkeit:
     *  Schock - General - Strasse - Zahl
     *  Sind beide Bilder gleich (z.B. beide Schock)
     *  wird anhand der hoechsten zaehlbaren Zahl entschieden:
     *  Schock 6 - Schock 3 | General 3 - General 2 | Strasse 456 - Strasse 345 | Zahl 521 - Zahl 421
     * </pre>
     *
     * @param b2 Object Becher, mit dem verglichen wird
     * @return Int - negative Zahl: Becher -gt- b2
     * positive Zahl: Becher -gt- b2
     * 0 :            Becher = b2
     * ToDo: Anzahl Wuerfe auswerten
     */
    @Override
    public int compareTo(Becher b2) {
        sortiere();
        Wuerfel[] w1 = this.wuerfel.toArray(new Wuerfel[3]);
        Wuerfel[] w2 = b2.getSortierteWuerfel();
        String vergleich = bild + "-" + b2.getBild();

        switch (vergleich) {
            case "Schock aus-Schock aus":
                if (anzahlWuerfe <= b2.getAnzahlWuerfe())
                    return 1;
                else
                    return -1;

            case "Schock-Schock":
                if (w1[0].getWert() == w2[0].getWert())
                    if (anzahlWuerfe < b2.getAnzahlWuerfe())
                        return 1;
                    else if (anzahlWuerfe > b2.getAnzahlWuerfe())
                        return -1;
                    else
                        return w1[0].getWert() - w2[0].getWert();
                else
                    return w1[0].getWert() - w2[0].getWert();


            case "General-General":
                if (w1[0].getWert() == w2[0].getWert())
                    if (anzahlWuerfe < b2.getAnzahlWuerfe())
                        return 1;
                    else if (anzahlWuerfe > b2.getAnzahlWuerfe())
                        return -1;
                    else
                        return w1[0].getWert() - w2[0].getWert();
                else
                    return w1[0].getWert() - w2[0].getWert();

            case "Straße-Straße":
                if (w1[0].getWert() == w2[0].getWert()) {
                    if (anzahlWuerfe < b2.getAnzahlWuerfe())
                        return 1;
                    else if (anzahlWuerfe > b2.getAnzahlWuerfe())
                        return -1;
                    else
                        return w1[0].getWert() - w2[0].getWert();
                } else {
                    return w1[0].getWert() - w2[0].getWert();
                }


            case "Zahl-Zahl":
                int ergebnis1 = (int) (w1[0].getWert() * Math.pow(10, 2)) + w1[1].getWert() * 10 + w1[2].getWert();
                int ergebnis2 = (int) (w2[0].getWert() * Math.pow(10, 2)) + w2[1].getWert() * 10 + w2[2].getWert();

                if (ergebnis1 == ergebnis2) {
                    if (anzahlWuerfe < b2.getAnzahlWuerfe())
                        return 1;
                    else if (anzahlWuerfe > b2.getAnzahlWuerfe())
                        return -1;
                    else
                        return 0;
                } else {
                    return ergebnis1 - ergebnis2;
                }

            case "Zahl-Schock aus":
                return -1;
            case "Zahl-Schock":
                return -1;
            case "Zahl-General":
                return -1;
            case "Zahl-Straße":
                return -1;

            case "Straße-Schock aus":
                return -1;
            case "Straße-Schock":
                return -1;
            case "Straße-General":
                return -1;

            case "General-Schock":
                return -1;

            case "Schock-General":
                return 1;


            case "Schock-Schock aus":
                return -1;

            default:
                return 1;


        }
    }


}
