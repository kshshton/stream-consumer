
public class Main {
    public static void main(String[] args) {
        new Consumer("localhost:9092", "mqtt.sensors.raw", "mqtt-consumer-group").start();
    }
}