CREATE TABLE t_Spieler(
  Kennung VARCHAR(30),
  Passwort VARCHAR (15)NOT NULL ,
  IP VARCHAR(15) ,
  Profilbild BYTEA,
  Aktiv BOOLEAN DEFAULT FALSE ,
  Abbrüche INT DEFAULT 0,
  Strafpunkte INT DEFAULT 0,
  Statistik BYTEA,
  Startwurf INT,
  CONSTRAINT PK_t_spieler PRIMARY KEY (Kennung)
);

CREATE TABLE t_Spielleiter(
  fk_t_Spieler_Kennung VARCHAR(30),
  Geleitete_Spiele INT DEFAULT 1,
  CONSTRAINT PK_t_Spielerleiter_Kennung Primary KEY(fk_t_Spieler_Kennung),
  CONSTRAINT FK_t_Spielerleiter_Kennung FOREIGN KEY (fk_t_Spieler_Kennung) REFERENCES t_Spieler(Kennung)ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE t_Spiel(
  Spiel_ID SERIAL,
  fk_t_Spielleiter_Kennung VARCHAR(30),
  Status INT  DEFAULT 1,
  Zeit TIMESTAMP NOT NULL DEFAULT current_timestamp,
  CONSTRAINT PK_t_Spiel PRIMARY KEY (Spiel_ID)  ,
  CONSTRAINT FK_t_Spiel FOREIGN KEY (fk_t_Spielleiter_Kennung) REFERENCES t_Spielleiter(fk_t_Spieler_Kennung)

);

CREATE TABLE t_ist_client(
  fk_t_Spieler_Kennung VARCHAR(30),
  fk_t_Spiel_Spiel_ID INT,
  CONSTRAINT PK_t_ist_client PRIMARY KEY (fk_t_Spiel_Spiel_ID,fk_t_Spieler_Kennung)
);

CREATE TABLE t_Hälfte(
  Art INT,
  fk_t_Spiel_Spiel_ID INT ,
  Verlierer VARCHAR(30),
  Stock INT DEFAULT 13,
  CONSTRAINT PK_t_Hälfte PRIMARY KEY(Art,fk_t_Spiel_Spiel_ID) ,
  CONSTRAINT FK_t_Spiel_Spiel_ID FOREIGN KEY  (fk_t_Spiel_Spiel_ID)REFERENCES t_Spiel(Spiel_ID) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE t_Runde(
  RundenNr INT,
  fk_t_Spiel_Spiel_ID INT,
  fk_t_Hälfte_Art INT,
  Beginner VARCHAR(30),
  Verlierer VARCHAR(30),
  Gewinner VARCHAR(30),

  CONSTRAINT PK_t_Runde PRIMARY KEY (RundenNr,fk_t_Spiel_Spiel_ID,fk_t_Hälfte_Art),
  CONSTRAINT FK_t_Spiel_Spiel_ID FOREIGN KEY (fk_t_Spiel_Spiel_ID,fk_t_Hälfte_Art)REFERENCES t_hälfte(fk_t_spiel_spiel_ID,Art) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE t_Durchgang(
  fk_t_Spieler_Kennung VARCHAR(30),
  fk_t_Spiel_Spiel_ID INT,
  fk_t_Hälfte_Art INT,
  fk_t_Runde_RundenNr INT,
  Würfel BYTEA,
  Zähler INT DEFAULT 1,

  CONSTRAINT PK_t_Durchgang PRIMARY KEY (fk_t_Spieler_Kennung,fk_t_Spiel_Spiel_ID,fk_t_Hälfte_Art,fk_t_Runde_RundenNr)
) ;



