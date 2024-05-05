
#drop schema viticultura;

create schema viticultura
use dam2;

CREATE TABLE Entrada (
	id int primary key auto_increment,
    instruccion varchar(100)
);

INSERT INTO Entrada (instruccion) VALUES ('B Bodega1');
INSERT INTO Entrada (instruccion) VALUES ('C');
INSERT INTO Entrada (instruccion) VALUES ('V BLANCA 3');
INSERT INTO Entrada (instruccion) VALUES ('V NEGRA 2');
INSERT INTO Entrada (instruccion) VALUES ('B Bodega2');
INSERT INTO Entrada (instruccion) VALUES ('C');
INSERT INTO Entrada (instruccion) VALUES ('V Negra 4');
INSERT INTO Entrada (instruccion) VALUES ('V Negra 1');
INSERT INTO Entrada (instruccion) VALUES ('V blanCA 2');
INSERT INTO Entrada (instruccion) VALUES ('#');
INSERT INTO Entrada (instruccion) VALUES ('B bodega3');
INSERT INTO Entrada (instruccion) VALUES ('B bodega4');
INSERT INTO Entrada (instruccion) VALUES ('C');
INSERT INTO Entrada (instruccion) VALUES ('B bodega5');
INSERT INTO Entrada (instruccion) VALUES ('V Negra 4');
INSERT INTO Entrada (instruccion) VALUES ('V blanCa 2');
INSERT INTO Entrada (instruccion) VALUES ('V neGra 4');
INSERT INTO Entrada (instruccion) VALUES ('#');
INSERT INTO Entrada (instruccion) VALUES ('V neGRA 2');
INSERT INTO Entrada (instruccion) VALUES ('V blanca 4');
INSERT INTO Entrada (instruccion) VALUES ('C');
INSERT INTO Entrada (instruccion) VALUES ('C');
INSERT INTO Entrada (instruccion) VALUES ('V blanca 4');
INSERT INTO Entrada (instruccion) VALUES ('#');
