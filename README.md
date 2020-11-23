# tarea06-sdistribuidos
# Integrantes:
* Kempis Guerrero
* Mathias Loor
* David Torres

# Instrucciones para compilar y ejecutar la aplicación en Ubuntu
1. Ejecutar el comando `sudo ifconfig enp0s8 10.10.10.X`(x es el numero del nodo) para configurar la red interna de la máquina virtual, la máquina que hará de servidor deberá tener la ip 10.10.10.1 y el colector 10.10.10.2
2. Instalar el jdk de java utilizando el comando `sudo apt install default-jdk`.
3. Instalar My SQL Server utilizando el comando `sudo apt install mysql-server`.
4. Luego ejecute el script de seguridad: `sudo mysql_secure_installation`.
5. Tambien hay que ajustar la autentificacion y los privilegios del usuario para eso usamos el comando para abrir `sudo mysql` sql server. Para configurar la cuenta root para autenticarse usando una contraseña, ejecute el siguiente comando ALTER USER. Asegúrese de cambiar password (contraseña) a una contraseña segura de su elección y sepa que este comando cambiará la contraseña de root que estableció:`ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';`
6. Despues se crea la base de datos `CREATE DATABASE servidor;` y tambien las tablas `CREATE TABLE lecturas (ID_sensor varchar(20) NOT NULL, Timestamp varchar(20) DEFAULT NULL,lecturas varchar(45) DEFAULT NULL);`.
7. Para comprobar que java está instalado ejecutar el comando `javac`.
8. Una vez ubicados en la carpeta del proyecto en ambas máquina virtuales nos movemos a la carpeta src y se necesita agregar al classpath la libreria mysql-connector-java-5.1.49.jar, para esto se utilizará el comando `export CLASSPATH=mysql-connector-java-5.1.49.jar:$CLASSPATH`.
9. Luego procedemos a compilar en ambas maquinas los modulos correspondientes para ello ejecutamos el comando `javac Servidor.java`y `javac Colector.java`, luego de verificar que se compilo correctamente ejecutamos el comando 'java Servidor' Y 'java Colector'.
10. Una vez realizado los pasos se podrá visualizar en el Servidor como empieza a enviar al colector la conexion y el colector enviar las lecturas y se intenta conectar al Servidor, en caso de no poder conectarse al este, mandará un mensaje de error Timeout y seguirá haciendo lecturas. Una vez se conecte al reportero empezará a enviarle información y ademas se guardara en la base de datos y los resultados de OK y Duplicados se guardaran en log.txt.
11. El 	Colector irá generando reportes de las lecturas y además irá llenando el historial de lecturas donde almacena timestamps de cada lectura realizada y el esatdo del envio.
12. El Servidor irá recibiendo las lecturas del los sensores e irá generando las lecturas en la base de datos creadas y actualizando de las lecturas de cada sensor. Además se generan un log.txt con los resultados del envio.

# Referencias
* https://youtu.be/gc6szh6kp8Q
* https://stackoverflow.com/questions/6735186/how-to-send-array-list-through-a-udp-server-client
* https://stackoverflow.com/questions/27797451/i-cant-catch-sockettimeoutexception
* https://youtu.be/pmAVLO7ZvG4
* https://stackoverflow.com/questions/27797451/i-cant-catch-sockettimeoutexception
* https://es.stackoverflow.com/questions/45846/c%C3%B3mo-separar-un-string-en-java-c%C3%B3mo-utilizar-split
* https://stackoverflow.com/questions/3944320/maximum-length-of-byte/53967254
* https://www.chikalov.dp.ua/java_tutorial/networking/datagrams/broadcasting.html
* https://stackoverflow.com/questions/19446366/java-socket-udp-loop
