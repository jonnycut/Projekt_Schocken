package spiel;

/**
 * Created by KNapret on 23.05.2016.
 * <pre>
 *     Becherklasse
 *     Enthaelt wuerfel
 *     kenn die Anzahl seiner Wuerfe
 * </pre>
 */
public class Becher implements Comparable <Becher> {
    private  Wuerfel[] wuerfel = new Wuerfel[3];
    private String bild;
    private int wurf;

    /**
     * <pre>
     * Erstellt einen neuen Becher:
     * Das Wuerfelarray wird mit 3 Wuerfeln gefuellt
     * Bild wird initial auf null gesetzt
     * Anzahl Wuerfe wird mit 0 initialisiert</pre>
     */
    public Becher(){

        for(int i =0; i<3;i++){
            wuerfel[i] = new Wuerfel();
        }

        this.bild = null;
        this.wurf = 0;
    }

    /**
     * Getter fuer das aktuelle Bild des Bechers
     * @return String : {Schock | General | Strasse | Zahl}
     */
    public String getBild() {
        return bild;
    }

    /**
     * Getter fuer die Anzahl der Wuerfe des Bechers
     *
     * @return Int - 0 - 3 (Da 3 Max)
     */
    public int getWurf(){
        return this.wurf;
    }

    /**
     * Getter fuer das Wuerfelarray
     * @return Wuerfel[3]
     */
    public Wuerfel[] getWuerfel(){
        return  this.wuerfel;
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
     *
     */
    public void wuerfeln() {

        //wuerfel[] vielleicht besser als ArrayList? --> Collections.sort...?

        this.wurf++;

        for(Wuerfel w : this.wuerfel){
            w.wuerfeln();
        }

        Wuerfel pufferWuerfel;
        for(int i=0; i<1;i++){
            if(wuerfel[i].compareTo(wuerfel[i+1])<0){
                pufferWuerfel = wuerfel[i];
                wuerfel[i] = wuerfel[i+1];
                wuerfel[i+1] = pufferWuerfel;

            }
        }

        if(wuerfel[0].getWert() ==1 && wuerfel[1].getWert()==1 && wuerfel[2].getWert()==1){
            bild = "Schock aus";
        }else if(wuerfel[1].getWert() ==1 && wuerfel[2].getWert()==1){
            bild = "Schock";
        }else if(wuerfel[0].getWert() == wuerfel[1].getWert() && wuerfel[1].getWert()== wuerfel[2].getWert()){
            bild = "General";
        }else if(wuerfel[0].getWert() == wuerfel[1].getWert()+1 && wuerfel[1].getWert()== wuerfel[2].getWert()+1){
            bild = "Straße";
        } else {
            bild = "Zahl";
        }

    }


    /**<pre>
     * Setzt alle Wuerfel auf draussen.
     * Nutzbar, wenn Wurf in Gaenze stehen gelassen wird.
     *
     * </pre>
     *
     */
    public void aufdecken(){

        for(Wuerfel w : this.wuerfel){
            w.setDraussen(true);

        }
    }

    /**
     * <pre>CompareTo(ein anderer Becher)
     *  Vergleicht einen Becher mit einem anderen Becher (b2)
     *  Wertigkeit:
     *  Schock > General > Strasse > Zahl
     *  Sind beide Bilder gleich (z.B. beide Schock)
     *  wird anhand der hoechsten zaehlbaren Zahl entschieden:
     *  Schock 6 > Schock 3 | General 3 > General 2 | Strasse 456 > Strasse 345 | Zahl 521 > Zahl 421
     * </pre>
     * @param b2 Object Becher, mit dem verglichen wird
     * @return Int - negative Zahl: Becher < b2 <br></br>
     *               positive Zahl: Becher > b2
     *               0 :            Becher = b2
     *               ToDo: Anzahl Wuerfe auswerten
     */
    @Override
    public int compareTo(Becher b2) {

        Wuerfel[] w1 = this.wuerfel;
        Wuerfel[] w2 = b2.getWuerfel();

        if(w1[1].getWert()==1 && w1[2].getWert() ==1){
            if(w2[1].getWert()==1 && w2[2].getWert() ==1){
                return w1[0].getWert()-w2[0].getWert();
            }else {
                return w1[0].getWert();
            }


        }else if(w1[0].getWert()==w1[1].getWert() && w1[1].getWert()==w1[2].getWert()){
            if(w2[0].getWert()==w2[1].getWert() && w2[1].getWert()==w2[2].getWert()){
                return w1[0].getWert()-w2[0].getWert();
            }else{
                return w1[0].getWert();
            }


        } else if(w1[0].getWert()==w1[1].getWert()-1 && w1[1].getWert()==w1[2].getWert()-1){
            if(w2[0].getWert()==w2[1].getWert()+1 && w2[1].getWert()==w2[2].getWert()+1){
                return w1[0].getWert()-w2[0].getWert();
            }else{
                return w1[0].getWert();
            }
        } else if (w2[1].getWert() == 1 && w2[2].getWert()==1){
            return 0-w2[0].getWert();
        } else if (w2[0].getWert()== w2[1].getWert() && w2[1].getWert()==w2[2].getWert()){
            return -30;
        } else if (w2[0].getWert() == w2[1].getWert()+1 && w2[1].getWert() == w2[2].getWert()+1 ){
            return -20;
        } else {
            return w1[0].getWert()-w2[0].getWert();
        }
    }
}
