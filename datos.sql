#   Inserto conductores
INSERT INTO conductores(dni, nombre, apellido, direccion, telefono, registro) VALUES (45121,'Jorge','Lanata','Laprida 800','15404040',0001);
INSERT INTO conductores(dni, nombre, apellido, direccion, telefono, registro) VALUES (12412,'Julian','Castel','Alem 200','15322332',0002);
INSERT INTO conductores(dni, nombre, apellido, direccion, telefono, registro) VALUES (22421,'Leandro','Rivera','Laprida 250','15402211',0003);
INSERT INTO conductores(dni, nombre, apellido, direccion, telefono, registro) VALUES (12345,'Jorge','Lanata','Laprida 800','15404040',0004);

#   Inserto automoviles
INSERT INTO automoviles(patente, marca, modelo, color, dni) VALUES ('ABC109','Chevrolet','Corsa','Negro',45121);
INSERT INTO automoviles(patente, marca, modelo, color, dni) VALUES ('DSC241','Chevrolet','Astra','Azul',12412);
INSERT INTO automoviles(patente, marca, modelo, color, dni) VALUES ('AAA132','Ford','Ka','Blanco',22421);
INSERT INTO automoviles(patente, marca, modelo, color, dni) VALUES ('BCD349','Fiar','Uno','Rojo',12345);

#   Inserto Tipos tajeta
INSERT INTO tipos_tarjeta(tipo, descuento) VALUES ('Normal',0.20);
INSERT INTO tipos_tarjeta(tipo, descuento) VALUES ('Premium',0.50);

#   Inserto tarjetas
INSERT INTO tarjetas(id_tarjeta, saldo, tipo, patente) VALUES (001,100.00,'Premium','ABC109');
INSERT INTO tarjetas(id_tarjeta, saldo, tipo, patente) VALUES (002,550.25,'Normal','DSC241');
INSERT INTO tarjetas(id_tarjeta, saldo, tipo, patente) VALUES (003,200.50,'Premium','AAA132');
INSERT INTO tarjetas(id_tarjeta, saldo, tipo, patente) VALUES (004,320.00,'Normal','BCD349');

#   Inserto ubicaciones
INSERT INTO ubicaciones(calle, altura, tarifa) VALUES ('Alem',1000,15.50);
INSERT INTO ubicaciones(calle, altura, tarifa) VALUES ('Chiclana',400,17.00);
INSERT INTO ubicaciones(calle, altura, tarifa) VALUES ('Mitre',700,20.00);

#   Inserto parquimetros
INSERT INTO parquimetros(id_parq, numero, calle, altura) VALUES (10001,100,'Alem',1000);
INSERT INTO parquimetros(id_parq, numero, calle, altura) VALUES (10002,101,'Chiclana',400);
INSERT INTO parquimetros(id_parq, numero, calle, altura) VALUES (10003,102,'Mitre',700);

#   Inserto relacion estacionamientos
INSERT INTO estacionamientos(id_tarjeta, id_parq, fecha_ent, hora_ent, fecha_sal, hora_sal) VALUES (001,10001,'2020-05-28','16:15:00',null,null);
INSERT INTO estacionamientos(id_tarjeta, id_parq, fecha_ent, hora_ent, fecha_sal, hora_sal) VALUES (004,10002,'2020-09-30','20:00:00','2020-09-30','22:00:00');

#   Inserto inspectores
INSERT INTO inspectores(legajo, dni, nombre, apellido, password) VALUES (01,99211,'Raul','Gimenez','holahola');
INSERT INTO inspectores(legajo, dni, nombre, apellido, password) VALUES (02,97001,'Trevor','Moran','chauchau');
INSERT INTO inspectores(legajo, dni, nombre, apellido, password) VALUES (03,90321,'Martin','Diez','holachau');

#   Inserto asociado_con
INSERT INTO asociado_con(id_asociado_con, legajo, calle, altura, dia, turno) VALUES (5555,01,'Alem',1000,'ma','m');
INSERT INTO asociado_con(id_asociado_con, legajo, calle, altura, dia, turno) VALUES (5556,02,'Chiclana',400,'mi','t');
INSERT INTO asociado_con(id_asociado_con, legajo, calle, altura, dia, turno) VALUES (5557,03,'Mitre',700,'vi','m');

#   Inserto relacion accede
INSERT INTO accede(legajo, id_parq, fecha, hora) VALUES (01,10001,'2020-09-30','10:30:00');

#   Inserto relacion multa
INSERT INTO multa(numero, fecha, hora, patente, id_asociado_con) VALUES (444441,'2020-09-30','23:30:00','BCD349',5556);
