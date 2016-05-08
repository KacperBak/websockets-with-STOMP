package de.kacperbak;

/**
 * Created by bakka on 01.05.16.
 */
public enum  MessageType {
    json, ser, zip;

    public static boolean isJson(String value){
        return json.toString().equalsIgnoreCase(value);
    }

    public static boolean isSerialized(String value){
        return ser.toString().equalsIgnoreCase(value);
    }

    public static boolean isZip(String value){
        return zip.toString().equalsIgnoreCase(value);
    }
}