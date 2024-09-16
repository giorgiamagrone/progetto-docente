INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Atalanta', 1907, 'Via Roma');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Bologna', 1909, 'Viale della Libertà');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Cagliari', 1920, 'Corso Italia');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Como', 1907, 'Via Dante Alighieri');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Empoli', 1920, 'Via Giuseppe Verdi');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Fiorentina', 1926, 'Piazza del Popolo');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Genoa', 1926, 'Via Garibaldi');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Hellas Verona', 1903, 'Via Vittorio Veneto');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Inter', 1908, 'Via Leonardo da Vinci');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Juventus', 1897, 'Via delle Rose');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Lazio', 1900, 'Via San Francesco');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Lecce', 1908, 'Via dei Fiori');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Milan', 1899, 'Via del Sole');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Monza', 1912, 'Via dell Argento');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Napoli', 1926,' Corso dei Mille');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Parma', 1913, 'Via Marconi');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Roma', 1927, 'Via della Pace');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Torino', 1906, 'Via del Mare');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Udinese', 1911, 'Viale dei Pini');
INSERT INTO team (id, name, year, address) VALUES(nextval('team_seq'), 'Venezia', 1907, 'Via Michelangelo');

INSERT INTO credentials (id, username, password, role) VALUES (1, 'giorgia', '$2a$10$I5dLpFmb6g3LjYccMbLm1e1QNFCt0L0.XHDfEKxiMGzTHLQiPTOgy', 'ADMIN');

--La password criptata qui è 'password'

