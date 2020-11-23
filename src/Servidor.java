import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Servidor {

    public static void main(String[] args) throws InterruptedException {

        final int PUERTO = 5000;
        String resultado = "";

        try {
            File myObj = new File("log.txt");
            if (myObj.createNewFile()) {
                System.out.println("Archivo creado: " + myObj.getName());
            } else {
                System.out.println("El archivo ya existe.");
            }
        } catch (IOException e) {
            System.out.println("Ocurrio un error.");
            e.printStackTrace();
        }

        try {
            System.out.println("Iniciado el servidor UDP");
            // Creacion del socket
            DatagramSocket socketUDP = new DatagramSocket(PUERTO);

            // Siempre atendera peticiones
            while (true) {

                byte[] buffer = new byte[1024];
                int contador = 0;

                // Preparo la respuesta
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);


                // Recibo el datagrama
                socketUDP.receive(peticion);
                System.out.println("Recibo la informacion del colector");


                // Convierto lo recibido y mostrar el mensaje
                String mensaje = new String(peticion.getData());

                String[] mensajes = mensaje.split("/");

                for (int i = 0; i < mensajes.length - 1; i++) {

                    String[] arrOfStr = mensajes[i].split("-", 3);

                    String idSensor = arrOfStr[0];
                    String lecturaSensor = arrOfStr[1];
                    String timeStampSensor = arrOfStr[2].trim();

                    int puertoCliente = peticion.getPort();
                    InetAddress direccion = peticion.getAddress();

                    // Conexion base de datos
                    String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false", "localhost", 3306, "servidor");
                    String user = "root";
                    String password = "root";

                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con = DriverManager.getConnection(url, user, password);
                        // here servidor is database name, root is username and password
                        Statement stmt = con.createStatement();

                        if (stmt.executeQuery("SELECT * FROM lecturas WHERE ID_sensor = " + idSensor + " AND Timestamp = " + "'" +
                                timeStampSensor + "'" + " AND lecturas = " + lecturaSensor + ";").next()) {

                            // se envia mensaje DUPLICADO

                            try (PrintWriter output = new PrintWriter(new FileWriter("log.txt", true))) {
                                output.printf("%s\r\n", "DUPLICADO");
                            } catch (IOException e) {
                                System.out.println("An error occurred.");
                                e.printStackTrace();
                            }
                            resultado = "DUPLICADO";
                        } else {
                            // se hace insert
                            try {
                                // here servidor is database name, root is username and password
                                int rs = stmt.executeUpdate("INSERT INTO lecturas (ID_sensor, Timestamp, lecturas) VALUES(" + idSensor + "," +
                                    "'" + timeStampSensor + "'" + "," + lecturaSensor + ");");

                                try (PrintWriter output = new PrintWriter(new FileWriter("log.txt", true))) {
                                    output.printf("%s\r\n", "OK");
                                } catch (IOException e) {
                                    System.out.println("Ocurrio un error.");
                                    e.printStackTrace();
                                }

                                con.close();
                            } catch (Exception e) {
                                System.out.println(e);
                            }

                            resultado = "OK";
                        }

                        contador = 0;

                        con.close();
                    } catch (Exception e) {
                        System.out.println(e);
                    }




                    buffer = resultado.getBytes();

                    // creo el datagrama
                    DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length, direccion, puertoCliente);

                    // Envio la informacion
                    System.out.println("Envio mensaje de confirmacion al colector\n");
                    socketUDP.send(respuesta);



                }

                // for (String a : arrOfStr)
                // System.out.println(a);

                // Obtengo el puerto y la direccion de origen
                // Sino se quiere responder, no es necesario

            }

        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}