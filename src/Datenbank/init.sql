CREATE TABLE t_spieler(
  Kennung VARCHAR(30),
  Passwort VARCHAR (15)NOT NULL ,
  IP VARCHAR(15)NOT NULL ,
  Profilbild VARCHAR(15),
  Aktiv BOOLEAN,
  Abbrüche INT,
  Strafpunkte INT,
  Startwurf INT,
  CONSTRAINT PK_t_spieler PRIMARY KEY (Kennung)
);

CREATE TABLE t_Spielleiter(
  fk_t_spieler_Kennung VARCHAR(30),
  CONSTRAINT FK_t_SPIELER_KENNUNG FOREIGN KEY(fk_t_spieler_Kennung) REFERENCES t_spieler(Kennung)
);

CREATE TABLE t_Spiel(
  Spiel_ID SERIAL,
  fk_t_spieler_Kennung VARCHAR(30),
  fk_t_spielleiter_Kennung VARCHAR(30)NOT NULL ,
  Status INT NOT NULL ,
  Zeit TIMESTAMP NOT NULL DEFAULT current_timestamp,
  CONSTRAINT PK_t_Spiel PRIMARY KEY (Spiel_ID)
);




CREATE TRIGGER zeit_24_Stunden_zurücksetzen
BEFORE INSERT OR UPDATE
ON t_Spiel
FOR EACH ROW
EXECUTE PROCEDURE aktuelle_zeit_setzen();