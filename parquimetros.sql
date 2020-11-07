#Archivo batch (parquimetros.sql) para la creacion de la
#Base de Datos del proyecto 2

# Creo la Base de Datos
CREATE DATABASE parquimetros;

USE parquimetros;


#--------------------------------------------------------
# Creacion Tablas para las entidades

CREATE TABLE conductores (
    dni INT UNSIGNED NOT NULL,
    nombre VARCHAR(45) NOT NULL,
    apellido VARCHAR(45) NOT NULL,
    direccion VARCHAR(45) NOT NULL,
    telefono VARCHAR(45),
    registro INT UNSIGNED NOT NULL,

    CONSTRAINT pk_conductores
    PRIMARY KEY (dni)

) ENGINE=InnoDB;


CREATE TABLE automoviles(
    patente VARCHAR(6) NOT NULL,
    marca VARCHAR(45) NOT NULL,
    modelo VARCHAR(45) NOT NULL,
    color VARCHAR(45) NOT NULL,
    dni INT UNSIGNED NOT NULL,

    CONSTRAINT pk_automoviles
    PRIMARY KEY (patente),

    CONSTRAINT FK_automoviles_conductores
    FOREIGN KEY (dni) REFERENCES conductores(dni)
        ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;

CREATE TABLE tipos_tarjeta(
    tipo VARCHAR(45) NOT NULL,
    descuento DECIMAL(3,2) UNSIGNED NOT NULL, # un digito entero y dos decimales

    CONSTRAINT check_tipos_tarjeta_descuento CHECK (descuento>=0 and descuento<=1),
    CONSTRAINT pk_tipos_tarjeta
    PRIMARY KEY (tipo)


) ENGINE=InnoDB;

CREATE TABLE tarjetas(
    id_tarjeta INT UNSIGNED NOT NULL AUTO_INCREMENT,
    saldo DECIMAL(5,2) NOT NULL, # Tres digitos enteros y dos decimales
    tipo VARCHAR(45) NOT NULL,
    patente VARCHAR(6) NOT NULL,

    CONSTRAINT pk_tarjetas
    PRIMARY KEY (id_tarjeta),

    CONSTRAINT FK_tarjetas_tipos_tarjeta
    FOREIGN KEY (tipo) REFERENCES tipos_tarjeta(tipo)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT FK_tarjetas_automoviles
    FOREIGN KEY (patente) REFERENCES automoviles(patente)
        ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;



CREATE TABLE inspectores(
    legajo INT UNSIGNED NOT NULL,
    dni INT UNSIGNED NOT NULL,
    nombre VARCHAR(45) NOT NULL,
    apellido VARCHAR(45) NOT NULL,
    password VARCHAR(32) NOT NULL,

    CONSTRAINT pk_inspectores
    PRIMARY KEY (legajo)

) ENGINE=InnoDB;

CREATE TABLE ubicaciones(
    calle VARCHAR(45) NOT NULL,
    altura INT UNSIGNED NOT NULL,
    tarifa DECIMAL(5,2) UNSIGNED NOT NULL, # Tres digitos enteros y dos decimales

    CONSTRAINT pk_ubicaciones
    PRIMARY KEY (calle,altura)

) ENGINE=InnoDB;

CREATE TABLE parquimetros(
    id_parq INT UNSIGNED NOT NULL,
    numero INT UNSIGNED NOT NULL,
    calle VARCHAR(45) NOT NULL,
    altura INT UNSIGNED NOT NULL,

    CONSTRAINT pk_parquimetros
    PRIMARY KEY (id_parq),

    CONSTRAINT FK_parquimetros_ubicaciones
    FOREIGN KEY (calle,altura) REFERENCES ubicaciones(calle,altura)
        ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


#-------------------------------------------------------
#   Creacion Tablas para las relaciones

CREATE TABLE estacionamientos(
    id_tarjeta INT UNSIGNED NOT NULL,
    id_parq INT UNSIGNED NOT NULL,
    fecha_ent DATE NOT NULL,
    hora_ent TIME NOT NULL,
    fecha_sal DATE,
    hora_sal TIME,

    CONSTRAINT pk_estacionamientos
    PRIMARY KEY (id_parq,fecha_ent,hora_ent),

    CONSTRAINT FK_estacionamientos_parquimetro
    FOREIGN KEY (id_parq) REFERENCES parquimetros(id_parq)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT FK_estacionamientos_tarjetas
    FOREIGN KEY (id_tarjeta) REFERENCES tarjetas(id_tarjeta)
        ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;

CREATE TABLE accede(
    legajo INT UNSIGNED NOT NULL,
    id_parq INT UNSIGNED NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,

    CONSTRAINT pk_accede
    PRIMARY KEY (id_parq,fecha,hora),

    CONSTRAINT FK_accede_inspector
    FOREIGN KEY (legajo) REFERENCES inspectores(legajo)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT FK_accede_parquimetro
    FOREIGN KEY (id_parq) REFERENCES parquimetros(id_parq)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    KEY(fecha),
    KEY(hora)
) ENGINE=InnoDB;


CREATE TABLE asociado_con(
    id_asociado_con INT UNSIGNED NOT NULL AUTO_INCREMENT,
    legajo INT UNSIGNED NOT NULL,
    calle VARCHAR(45) NOT NULL,
    altura INT UNSIGNED NOT NULL,
    dia ENUM('do','lu','ma','mi','ju','vi','sa') NOT NULL,
    turno ENUM('m','t') NOT NULL,

    CONSTRAINT pk_asociado_con
    PRIMARY KEY (id_asociado_con),

    CONSTRAINT FK_asociado_con_inspectores
    FOREIGN KEY (legajo) REFERENCES inspectores(legajo)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT FK_asociado_con_ubicaciones
    FOREIGN KEY (calle,altura) REFERENCES ubicaciones(calle,altura)
        ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;


CREATE TABLE multa(
    numero INT UNSIGNED NOT NULL AUTO_INCREMENT,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    patente VARCHAR(6) NOT NULL,
    id_asociado_con INT UNSIGNED NOT NULL,

    CONSTRAINT pk_multa
    PRIMARY KEY (numero),

    CONSTRAINT FK_multa_automoviles
    FOREIGN KEY (patente) REFERENCES automoviles(patente)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT FK_multa_asociado_con
    FOREIGN KEY (id_asociado_con) REFERENCES asociado_con(id_asociado_con)
        ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;




#-----------------------------------------------------------------------
# Creacion de usuarios y otorgamiento de privilegios


#Este usuario se utilizar´a para administrar la base de datos “parquimetros” por lo
#tanto deber´a tener acceso total sobre todas las tablas, con la opci´on de crear usuarios y
#otorgar privilegios sobre las mismas. Para no comprometer la seguridad se restringir´a que el
#acceso de este usuario se realice s´olo desde la m´aquina local donde se encuentra el servidor
#MySQL. El password de este usuario deber´a ser: admin.
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON parquimetros.* TO 'admin'@'localhost' WITH GRANT OPTION; #Acceso total a todas las tablas


#Este usuario esta destinado a permitir el acceso de la aplicaci´on de venta de tarjetas
#y deber´a tener privilegios m´ınimos para cargar una nueva tarjeta con un saldo inicial y de
#cualquier tipo. El password de este usuario deber´a ser: venta.
CREATE USER 'venta'@'%' IDENTIFIED BY 'venta';
GRANT INSERT ON parquimetros.tarjetas TO 'venta'@'%'; #Cargar tarjeta
GRANT SELECT ON parquimetros.tipos_tarjeta TO 'venta'@'%'; # Poder ver los tipos de tarjeta

#Este usuario estara destinado a permitir el acceso de las unidades personales
#que utilizan los inspectores para controlar los estacionamientos
CREATE USER 'inspector'@'%' IDENTIFIED BY 'inspector';

# Creacion view de inspector
CREATE VIEW estacionados AS
    SELECT p.calle,p.altura,t.patente
    FROM (estacionamientos as e JOIN tarjetas as t ON e.id_tarjeta = t.id_tarjeta) #Aca tenemos id_tarjeta|patente|fecha_ent|fecha_sal|hora_ent|hora_sal
     JOIN parquimetros as p ON e.id_parq = p.id_parq
    WHERE e.hora_ent is NOT NULL and e.fecha_ent is NOT NULL and e.hora_sal is NULL and e.fecha_sal is NULL; #Verifica que sea estacionamiento abierto

GRANT SELECT ON parquimetros.inspectores TO 'inspector'@'%';    #validar numero de legajo y password de un inspector
GRANT SELECT ON parquimetros.estacionados TO 'inspector'@'%';
GRANT SELECT ON parquimetros.multa TO 'inspector'@'%';    # recuperar multas hechas
GRANT SELECT ON parquimetros.automoviles TO 'inspector'@'%';    #recuperar patentes validas
GRANT INSERT ON parquimetros.multa TO 'inspector'@'%';  #Cargar multas
GRANT INSERT ON parquimetros.accede TO 'inspector'@'%';         #Registrar accesos a parquimetros
GRANT SELECT ON parquimetros.parquimetros TO 'inspector'@'%'; # OBtener parquimetro
GRANT SELECT ON parquimetros.asociado_con TO 'inspector'@'%'; #recuperar el campo id_asociado_con y poder cargar una multa