package auto.ausiot.ausiotrest.util;

public class Util {
    public static String getTopic1(String schduleID){
        String topic = schduleID.substring(0,schduleID.lastIndexOf("_"));
        return topic;
    }

    public static String getSensorID1(String schduleID){
        String ID = schduleID.substring(schduleID.lastIndexOf("_") + 1,schduleID.length());
        return ID;
    }
}
