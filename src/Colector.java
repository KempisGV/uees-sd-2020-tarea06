import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Colector {

    public static void main(String[] args) throws InterruptedException {

        int sensoresID[];
        sensoresID = new int[5];

        int iteracion = 1;

        while (true) {

            Random r = new Random();

            Sensor sensor1 = new Sensor(1, r.nextInt(30) + 1,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            Sensor sensor2 = new Sensor(2, r.nextInt(30) + 1,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            Sensor sensor3 = new Sensor(3, r.nextInt(30) + 1,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            Sensor sensor4 = new Sensor(4, r.nextInt(30) + 1,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            Sensor sensor5 = new Sensor(5, r.nextInt(30) + 1,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            // puerto del servidor
            final int PUERTO_SERVIDOR = 5000;
            // buffer donde se almacenara los mensajes
            byte[] buffer = new byte[1024];
            byte[] ipAddr = new byte[] {10,10,10,1};

            try {
                // Obtengo la localizacion de localhost
                InetAddress direccionServidor = InetAddress.getByAddress(ipAddr);

                // Creo el socket de UDP
                DatagramSocket socketUDP = new DatagramSocket();

                String mensaje = "";

                switch (iteracion) {
                    case 1:
                        mensaje = sensor1.getSensorID() + "-" + sensor1.getLectura() + "-" + sensor1.getTimeStamp();
                        break;
                    case 2:
                        mensaje = sensor2.getSensorID() + "-" + sensor2.getLectura() + "-" + sensor2.getTimeStamp();
                        break;
                    case 3:
                        mensaje = sensor3.getSensorID() + "-" + sensor3.getLectura() + "-" + sensor3.getTimeStamp();
                        break;
                    case 4:
                        mensaje = sensor4.getSensorID() + "-" + sensor4.getLectura() + "-" + sensor4.getTimeStamp();
                        break;
                    case 5:
                        mensaje = sensor5.getSensorID() + "-" + sensor5.getLectura() + "-" + sensor5.getTimeStamp();
                        break;
                    default:
                        System.out.println("Ocurrio un error");
                }

                // Convierto el mensaje a bytes
                buffer = mensaje.getBytes();

                // Creo un datagrama
                DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor, PUERTO_SERVIDOR);

                // Lo envio con send
                System.out.println("Envio datos al servidor");
                socketUDP.send(pregunta);

                // Preparo la respuesta
                Arrays.fill(buffer, (byte) 0);
                DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

                
                //TimeOut
                socketUDP.setSoTimeout(5000);
                
                // Recibo la respuesta
                socketUDP.receive(peticion);
                System.out.println("Recibo confirmacion");

                // Cojo los datos y lo muestro
                mensaje = new String(peticion.getData());
                System.out.println(mensaje);

                System.out.println(iteracion);
               
                socketUDP.close();

            } catch (SocketException ex) {
                Logger.getLogger(Colector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Colector.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Colector.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (iteracion >= 5) {
                iteracion = 1;
                TimeUnit.SECONDS.sleep(5);
            } else {
                iteracion++;
            }

        }

    }

}