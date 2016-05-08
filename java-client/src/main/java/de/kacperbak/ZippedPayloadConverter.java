package de.kacperbak;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.kacperbak.messages.ZippedMessage;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by bakka on 08.05.16.
 */
public class ZippedPayloadConverter {

    public static byte[] ObjectToGzip(ZippedMessage zippedMessage) throws Exception{
        return jsonToGzip(objectToJson(zippedMessage));
    }

    public static ZippedMessage gzipToObject(byte[] zippedBytes) throws Exception {
        return jsonToObject(gzipToJson(zippedBytes));
    }

    public static byte[] jsonToGzip(String jsonPayload) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);
        objectOutputStream.writeObject(jsonPayload);
        objectOutputStream.close();
        gzipOutputStream.close();
        byte[] result = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return result;
    }

    public static String gzipToJson(byte[] zippedBytes) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(zippedBytes);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
        ObjectInputStream objectInputStream = new ObjectInputStream(gzipInputStream);
        String plainJsonPayload = (String) objectInputStream.readObject();
        objectInputStream.close();
        gzipInputStream.close();
        byteArrayInputStream.close();
        return plainJsonPayload;
    }

    public static String objectToJson(ZippedMessage zippedMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(zippedMessage);
    }

    public static ZippedMessage jsonToObject(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, ZippedMessage.class);
    }
}
