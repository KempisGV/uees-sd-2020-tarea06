//Crear base de datos llamada servidor
CREATE TABLE lecturas (
  ID_sensor int NOT NULL,
  Timestamp varchar(20) DEFAULT NULL,
  lecturas varchar(45) DEFAULT NULL
)