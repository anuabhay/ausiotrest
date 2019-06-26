package auto.ausiot.ausiotrest.model;

public enum Days {
    Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday;

    public static Days get(int index){
        return Days.values()[index];
    }
}