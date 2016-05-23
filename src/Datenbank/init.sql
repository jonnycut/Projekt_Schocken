CREATE TABLE t_Spieler(
  Kennung VARCHAR(30),
  Passwort VARCHAR (15)NOT NULL ,
  IP VARCHAR(15) ,
  Profilbild BYTEA,
  Aktiv BOOLEAN,
  Abbr�che INT,
  Strafpunkte INT,
  Startwurf INT,
  CONSTRAINT PK_t_spieler PRIMARY KEY (Kennung)
);

CREATE TABLE t_Spielleiter(
  fk_t_spieler_Kennung VARCHAR(30),
  CONSTRAINT FK_t_SPIELER_KENNUNG FOREIGN KEY(fk_t_spieler_Kennung) REFERENCES t_spieler(Kennung)ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE t_Spiel(
  Spiel_ID SERIAL,
  fk_t_spieler_Kennung VARCHAR(30),
  fk_t_spielleiter_Kennung VARCHAR(30)NOT NULL ,
  Status INT NOT NULL ,
  Zeit TIMESTAMP NOT NULL DEFAULT current_timestamp,
  CONSTRAINT PK_t_Spiel PRIMARY KEY (Spiel_ID)
);

CREATE TABLE t_H�lfte(
  Art INT,
  fk_t_Spiel_Spiel_ID INT,
  Verlierer VARCHAR(30),
  Stock INT DEFAULT 13,
  CONSTRAINT PK_t_H�lfte PRIMARY KEY(Art),
  CONSTRAINT FK_t_Spiel_Spiel_ID FOREIGN KEY (fk_t_Spiel_Spiel_ID)REFERENCES t_Spiel(Spiel_ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE t_Runde(
  RundenNr INT,
  fk_t_Spiel_Spiel_ID INT,
  fk_t_H�lfte_Art INT,
  Beginner VARCHAR(30),
  Verlierer VARCHAR(30),
  Gewinner VARCHAR(30),

  CONSTRAINT PK_t_Runde PRIMARY KEY (RundenNr),
  CONSTRAINT FK_t_Spiel_Spiel_ID FOREIGN KEY (fk_t_Spiel_Spiel_ID)REFERENCES t_Spiel(Spiel_ID) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT FK_t_H�lfte_Art FOREIGN KEY (fk_t_H�lfte_Art)REFERENCES t_H�lfte(Art) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE t_Durchgang(
  fk_t_Spieler_Kennung VARCHAR(30),
  fk_t_Spiel_Spiel_ID INT,
  fk_t_H�lfte_Art INT,
  fk_t_Runde_RundenNr INT,
  W�rfel_1 INT,
  W�rfel_2 INT,
  W�rfel_3 INT,
  Z�hler INT,

  CONSTRAINT PK_t_Durchgang PRIMARY KEY (fk_t_Spieler_Kennung,fk_t_Spiel_Spiel_ID,fk_t_H�lfte_Art,fk_t_Runde_RundenNr)
) ;




CREATE TRIGGER zeit_24_Stunden_zur�cksetzen
BEFORE INSERT OR UPDATE
ON t_Spiel
FOR EACH ROW
EXECUTE PROCEDURE aktuelle_zeit_setzen();