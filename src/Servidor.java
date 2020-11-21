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

        try {
            File myObj = new File("log.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            System.out.println("Iniciado el servidor UDP");
            // Creacion del socket
            DatagramSocket socketUDP = new DatagramSocket(PUERTO);

            // Siempre atendera peticiones
            while (true) {

                byte[] buffer = new byte[1024];
                String resultado = "";

                // Preparo la respuesta
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                // Recibo el datagrama
                socketUDP.receive(peticion);
                System.out.println("Recibo la informacion del colector");

                // Convierto lo recibido y mostrar el mensaje
                String mensaje = new String(peticion.getData());

                String[] arrOfStr = mensaje.split("-", 3);

                // for (String a : arrOfStr)
                // System.out.println(a);

                String idSensor = arrOfStr[0];
                String lecturaSensor = arrOfStr[1];
                String timeStampSensor = arrOfStr[2].trim();

                // Obtengo el puerto y la direccion de origen
                // Sino se quiere responder, no es necesario
                int puertoCliente = peticion.getPort();
                InetAddress direccion = peticion.getAddress();

                TimeUnit.SECONDS.sleep(3);
                // Conexion base de datos
                String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false", "localhost", 3306, "servidor");

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(url, "root", "diciembre17");
                    // here servidor is database name, root is username and password
                    Statement stmt = con.createStatement();

                    if (stmt.executeQuery("SELECT * FROM lecturas WHERE ID_sensor = " + idSensor + " AND Timestamp = "
                            + "'" + timeStampSensor + "'" + " AND lecturas = " + lecturaSensor + ";").next()) {

                        // se envia mensaje DUPLICADO

                        try (PrintWriter output = new PrintWriter(new FileWriter("log.txt", true))) {
                            output.printf("%s\r\n", "DUPLICADO");
                        } catch (IOException e) {
                            System.out.println("An error occurred.");
                            e.printStackTrace();
                        }
                        mensaje = "DUPLICADO";
                    } else {
                        // se hace insert
                        try {
                            // here servidor is database name, root is username and password
                            int rs = stmt.executeUpdate("INSERT INTO lecturas (ID_sensor, Timestamp, lecturas) VALUES("
                                    + idSensor + "," + "'" + timeStampSensor + "'" + "," + lecturaSensor + ");");

                            try (PrintWriter output = new PrintWriter(new FileWriter("log.txt", true))) {
                                output.printf("%s\r\n", "OK");
                            } catch (IOException e) {
                                System.out.println("An error occurred.");
                                e.printStackTrace();
                            }

                            con.close();
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        mensaje = "OK";
                    }

                    con.close();
                } catch (Exception e) {
                    System.out.println(e);
                }

                buffer = mensaje.getBytes();

                // creo el datagrama
                DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length, direccion, puertoCliente);

                // Envio la informacion
                System.out.println("Envio mensaje de confirmacion al colector\n");
                socketUDP.send(respuesta);

            }

        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}