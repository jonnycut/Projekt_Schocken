-- Die Realation t_spieler wird angelegt
--Kennung --> der Name des Spielers limitiert auf 30 Zeichen da das JAVA Programm in der Eingabe nicht mehr Zeichen zulässt
--Passwort --> das Passwort des Spielers limitiert auf 30 Zeichen da das JAVA Programm in der Eingabe nicht mehr Zeichen zulässt
-- das Passwort muss immer gestzt werden --> NOT NULL
-- --IP --> IP Adresse des Spielers wird bei jedem Anmeldevorgang geupdatet um sicherzustellen das die aktuelle IP des Nutzers in
-- der Datenbank vorgehalten wird
--Profilbild wird als Stream in die DB geschrieben deshalb der Datentyp BYTEA
--Aktiv ist ein Bollean da wir hier festhalten welcher Spieler im Siel drann ist und Aktionen durchführen darf
-- Abrüche Anzahl der Spielabrüche initial auf Null
--Strafpunkte gibt die Anzahl der erhaltenen Chips an wird nach jedem Spiel wieder auf 0 gesetzt
--Startwurf ist ein 3 stelliger INT WERT und danach wird die Sitzreihenfolge im Spiel generiert
--der Wurf wird abgefragt von allen Soielern die sich im selben Spiel befinden und absteigen sortiert ausgegeben
-- PK ist die Kennung des Spilers somit kann kein Benutzername doppelt vorkommen
CREATE TABLE t_Spieler(
  Kennung VARCHAR(30),
  Passwort VARCHAR (15)NOT NULL ,
  IP VARCHAR(15) ,
  Profilbild BYTEA,
  Aktiv BOOLEAN DEFAULT FALSE ,
  Abbrueche INT DEFAULT 0,
  Strafpunkte INT DEFAULT 0,
  Startwurf INT,
  CONSTRAINT PK_t_spieler PRIMARY KEY (Kennung)
);
--Die Relation t_spielleiter wird angelegt
-- ist eine Spezialisierung der Spieler deshalb ist der PrimaryKey = der Spielerkennung kann kaskadierend gelöscht und geupdatet werden

CREATE TABLE t_Spielleiter(
  fk_t_Spieler_Kennung VARCHAR(30),
  CONSTRAINT PK_t_Spielerleiter_Kennung Primary KEY(fk_t_Spieler_Kennung),
  CONSTRAINT FK_t_Spielerleiter_Kennung FOREIGN KEY (fk_t_Spieler_Kennung) REFERENCES t_Spieler(Kennung)ON UPDATE CASCADE ON DELETE CASCADE
);

--Die Relation t_spiel wird angelegt
--Spiel_id wurde als SERIAL gewählt um sicherzustellen das es die Spielkennung automatisch hochgestzt wird
--fk_t_spielleiter _kennung um nachzuvollziehen welcher Spieler welches Spiel geleitet hat
--Status des Spiels 1 für offen --> Spieler können diesem Spiel noch joinen
--                  2 für geschlossen --> das Spiel hat begonnen , es kann sich keiner mehr joinen
--                  3 für beendet --> das Spiel ist fertig
--verlierer repräsentiert den Verlierer des Spiels wird erst gestzt wenn das Spiel den Status 3 hat
--Zeit ist der Zeitstempel des Spiels zu dem Zeitpunkt wann es angelegt wurde mit current _timestamp
-- PK ist die SpielID kein Spiel mit der Selben kennung
-- FK die Kennung des Spielleiters
CREATE TABLE t_Spiel(
  Spiel_ID SERIAL,
  fk_t_Spielleiter_Kennung VARCHAR(30),
  Status INT  DEFAULT 1,
  verlierer VARCHAR(30),
  Zeit TIMESTAMP NOT NULL DEFAULT current_timestamp,
  CONSTRAINT PK_t_Spiel PRIMARY KEY (Spiel_ID)  ,
  CONSTRAINT FK_t_Spiel FOREIGN KEY (fk_t_Spielleiter_Kennung) REFERENCES t_Spielleiter(fk_t_Spieler_Kennung)
);
-- Relation t_ist client
-- repräsentiert die Verbindungstabellle zwischen t_spieler und t_spiel es kann nachvollzogen werden welcher Spiler an welchem Spiel teilgenommen hat
CREATE TABLE t_ist_client(
  fk_t_Spieler_Kennung VARCHAR(30),
  fk_t_Spiel_Spiel_ID INT,
  CONSTRAINT PK_t_ist_client PRIMARY KEY (fk_t_Spiel_Spiel_ID,fk_t_Spieler_Kennung)
);
--Die Relation t_hälfte wird angelegt
-- ART gibt an in welcher Hälfte wir uns befinden 1 für die erste Hälfte 2 für die zweite Hälfte und 3 für das Finale
-- Der Verlierer ist initial leer und wird nachdem die Hälfte durchgespielt ist geupdatet Hier wird dan die Kennung des Hälftenverlierers eingefügt
-- Der Stock ist der Speicher für die Strafpunkte die die Spieler anhand iherer Rundenergebnisse und gewürfelten Bilder erhalten beim Update werden diese vom Stock abgezogen
-- PK setz sich aus der Spiel_ID und der Hälftenart zusammen um sicherzustellen das es meherer Hälften in ein und dem Selben Spiel geben kann
-- FK der Fremdschlüssel aus der Relation t_Spiel kaskadierendes Löschen und Updaten
CREATE TABLE t_Haelfte(
  Art INT,
  fk_t_Spiel_Spiel_ID INT ,
  Verlierer VARCHAR(30),
  Stock INT DEFAULT 13,
  CONSTRAINT PK_t_Haelfte PRIMARY KEY(Art,fk_t_Spiel_Spiel_ID) ,
  CONSTRAINT FK_t_Spiel_Spiel_ID FOREIGN KEY  (fk_t_Spiel_Spiel_ID)REFERENCES t_Spiel(Spiel_ID) ON UPDATE CASCADE ON DELETE CASCADE
);
--Relation t_Runde wird angelegt
-- Rundennummer
-- Beginner Verlierer und Gewinner wurden auf 30 zeichen limitiert da die Kennung der Spieler ebenfalls nicht länger ist
-- Da es in einer Hälfte immer nur einmal die selbe Runde gebn kann wurde hier die Schlüsselbeziehung so gewählt das die Spiel iD und Hälften Art als Fremdschlüssel fungieren
--der Primärschlüssel ist hier die Rundennummer da diese weiter geschaltet werden muss
CREATE TABLE t_Runde(
  RundenNr INT,
  fk_t_Spiel_Spiel_ID INT,
  fk_t_Haelfte_Art INT,
  Beginner VARCHAR(30),
  Verlierer VARCHAR(30),
  Gewinner VARCHAR(30),

  CONSTRAINT PK_t_Runde PRIMARY KEY (RundenNr,fk_t_Spiel_Spiel_ID,fk_t_Haelfte_Art),
  CONSTRAINT FK_t_Spiel_Spiel_ID FOREIGN KEY (fk_t_Spiel_Spiel_ID,fk_t_Haelfte_Art)REFERENCES t_haelfte(fk_t_spiel_spiel_ID,Art) ON UPDATE CASCADE ON DELETE CASCADE
);
--die Relation t_Durchgang wird angelegt
--In dieser Tabelle haben wir eine Schlüsselkonstellation die sich durch die Abhängigkeiten der übergordneten Relationen
-- aufadiert hat und alle bisherigen Fremdschlüssel der übergeordneten Relationen wurden weiter gereicht
--Die Würfel werden als BYTEA gespeichert da diese Objekte Eigenschaften haben die erhalten werden müssen und es nur
-- mit extrem großen Aufwand möglich gewesen wäre diese zu rekonstuieren
-- Der Zähler die als schaltungselement und ist deshalb der Primary KEY
CREATE TABLE t_Durchgang(
  fk_t_Spieler_Kennung VARCHAR(30),
  fk_t_Spiel_Spiel_ID INT,
  fk_t_Haelfte_Art INT,
  fk_t_Runde_RundenNr INT,
  Wuerfel1 BYTEA,
  Wuerfel2 BYTEA,
  Wuerfel3 BYTEA,
  zaehler INT DEFAULT 1,

  CONSTRAINT PK_t_Durchgang PRIMARY KEY (zaehler,fk_t_Spieler_Kennung,fk_t_Spiel_Spiel_ID,fk_t_Haelfte_Art,fk_t_Runde_RundenNr),
  CONSTRAINT FK_t_Runde FOREIGN KEY (fk_t_Spiel_Spiel_ID,fk_t_Haelfte_Art,fk_t_Runde_RundenNr) REFERENCES t_runde(fk_t_Spiel_Spiel_ID,fk_t_Haelfte_Art,RundenNr)

) ;



