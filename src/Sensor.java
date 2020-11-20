public class Sensor {

    private int sensorID;
    private int lectura;
    private String timeStamp;

    public Sensor(int sensorID, int lectura, String timeStamp) {
        super();
        this.sensorID = sensorID;
        this.lectura = lectura;
        this.timeStamp = timeStamp;
    }

    public int getSensorID() {
        return sensorID;
    }

    public void setSensorID(int sensorID) {
        this.sensorID = sensorID;
    }

    public int getLectura() {
        return lectura;
    }

    public void setLectura(int lectura) {
        this.lectura = lectura;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}