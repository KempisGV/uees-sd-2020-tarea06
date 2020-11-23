//Crear base de datos llamada servidor
CREATE TABLE lecturas (
  ID_sensor varchar(20) NOT NULL,
  Timestamp varchar(20) DEFAULT NULL,
  lecturas varchar(45) DEFAULT NULL
)