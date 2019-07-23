package mqtt;

/**
 * Created by anu on 22/06/19.
 */

public class Constants {
    public static String ACTION_TOPIC = "test";
    public static String STATUS_TOPIC = "status_topic";


    public static String ACTION_R1_OPEN = "R10N";
    public static String ACTION_R1_CLOSE = "R1OFF";

    public static String ACTION_R2_OPEN = "R20N";
    public static String ACTION_R2_CLOSE = "R2OFF";

    //MQTT Login
    public static String MQTT_HOST = "tcp://m12.cloudmqtt.com:13727";
    public static String MQTT_USER = "sbpmtfqc";
    public static String MQTT_PASSWD = "GGGMHoXnNMz-";

    public static int ALARM_FREQUENCY = 15;
    public static int ALARM_REQUEST_CODE=101;

    // QR Code value per user
    public static String QR_CODE_FOR_USER = "USER_1";

    //Connect URL to rest service
    public static final String BASE_URL = "http://10.0.2.2:8080";


    public static String RUN_STATUS_RUNNING = "RUNNING";
    public static String RUN_STATUS_CLOSED = "CLOSED";




}
