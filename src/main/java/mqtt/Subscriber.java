package mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A sample application that demonstrates how to use the Paho MQTT v3.1 Client blocking API.
 */


public class Subscriber implements MqttCallback {

    private static final int qos = 1;
    //private String topic = "test";
    private MqttClient client = null;
    //private Subscriber mqttsub = null;
    private MQTTCallBack act;

//    public Subscriber(String uri) throws MqttException, URISyntaxException {
//        this();
//    }

    public Subscriber() throws MqttException {

//        if (client == null) {
//            String host = Constants.MQTT_HOST;
//            String username = Constants.MQTT_USER;
//            String password = Constants.MQTT_PASSWD;
//            String clientId = MqttAsyncClient.generateClientId();
//
//
//            MqttConnectOptions conOpt = new MqttConnectOptions();
//            conOpt.setCleanSession(true);
//            conOpt.setUserName(username);
//            conOpt.setPassword(password.toCharArray());
//
//            this.client = new MqttClient(host, clientId, new MemoryPersistence());
//            this.client.setCallback(this);
//            this.client.connect(conOpt);
//        }
        //this.client.subscribe(this.topic, qos);
    }

    public void createConnection(String host, String username , String password) throws MqttException {
//        String host = Constants.MQTT_HOST;
//        String username = Constants.MQTT_USER;
//        String password = Constants.MQTT_PASSWD;
        String clientId = MqttAsyncClient.generateClientId();

        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setUserName(username);
        conOpt.setPassword(password.toCharArray());

        this.client = new MqttClient(host, clientId, new MemoryPersistence());
        this.client.setCallback(this);
        this.client.connect(conOpt);
    }

    private String[] getAuth(URI uri) {
        String a = uri.getAuthority();
        String[] first = a.split("@");
        return first[0].split(":");
    }

    public void sendMessage(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);
        this.client.publish(topic, message); // Blocking publish
    }


    /**
     * @see MqttCallback#connectionLost(Throwable)
     */
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost because: " + cause);
        System.exit(1);
    }

    /**
     * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        System.out.println(String.format("[%s] %s", topic, new String(message.getPayload())));
        //mqttsub.act.testcallback(new String(message.getPayload()));
        act.onCallBack(new String(message.getPayload()));
    }


    public void disconnect() throws MqttException {
        client.disconnect();;
    }

    public void sendMsg(String topic, String msg) throws MqttException, URISyntaxException{
        sendMessage(topic, msg);
    }

    public void subscribe(String topic,MQTTCallBack act) throws MqttException {
        client.subscribe(topic, qos);
        act = act;
    }

//    public static void connect() throws MqttException, URISyntaxException{
//        mqttsub = new Subscriber();
//    }
//
//    public static void disconnect() throws MqttException {
//        //mqttsub.client.disconnect();;
//    }
//
//    public static void sendMsg(String topic, String msg) throws MqttException, URISyntaxException{
//        mqttsub.sendMessage(topic, msg);
//    }
//
//    public static void subscribe(String topic,MQTTCallBack act) throws MqttException {
//        mqttsub.client.subscribe(topic, qos);
//        mqttsub.act = act;
//    }
}

