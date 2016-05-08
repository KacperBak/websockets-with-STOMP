package de.kacperbak;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import de.kacperbak.messages.JsonMessage;
import de.kacperbak.messages.SerializedMessage;
import de.kacperbak.messages.ZippedMessage;

@Controller
public class StompController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StompController.class);

    @MessageMapping("/json")
    public JsonMessage json(JsonMessage message) throws Exception {
        LOGGER.debug("Message arrived on endpoint: '/app/json'.");
        LOGGER.debug("message.content: {}", message.getContent());

        //change content
        message.setContent(message.getContent() + "456");

        //send message back
        return message;
    }

    @MessageMapping("/ser")
    public byte[] ser(byte[] message) throws Exception {
        LOGGER.debug("Message arrived on endpoint: '/app/ser'.");

        //deserialize
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        SerializedMessage serializedMessage = (SerializedMessage) objectInputStream.readObject();
        LOGGER.debug("message.content: {}", serializedMessage.getContent());
        objectInputStream.close();
        byteArrayInputStream.close();

        //change content
        serializedMessage.setContent(serializedMessage.getContent() + "789");

        //serialize
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(serializedMessage);
        byte[] serializedBytes = byteArrayOutputStream.toByteArray();
        objectOutputStream.close();
        byteArrayOutputStream.close();

        //send message back
        return serializedBytes;
    }

    @MessageMapping("/zip")
    public byte[] zip(byte[] message) throws Exception {
        LOGGER.debug("Message arrived on endpoint: '/app/zip'.");


        //GgzipBytes2Json
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message);
        GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);

        byte[] buffer = new byte[8192];
        int read = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((read = gzipInputStream.read(buffer)) != -1 ){
            out.write(buffer, 0, read);
        }
        String jsonPayload =  out.toString();
        gzipInputStream.close();
        byteArrayInputStream.close();

        //Json2Object
        ObjectMapper objectMapper = new ObjectMapper();
        ZippedMessage zippedMessage = objectMapper.readValue(jsonPayload, ZippedMessage.class);

        //change content
        zippedMessage.setContent(zippedMessage.getContent() + "012");

        //Object2Json
        String changedJsonPayload = objectMapper.writeValueAsString(zippedMessage);

        //Json2Bytes
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(jsonPayload.getBytes());
        byte[] zippedBytes = byteArrayOutputStream.toByteArray();
        gzipOutputStream.close();
        byteArrayOutputStream.close();

        return zippedBytes;
    }
}
