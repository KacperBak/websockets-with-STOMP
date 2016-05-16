package de.kacperbak.controller;

import de.kacperbak.messages.JsonMessage;
import de.kacperbak.messages.SerializedMessage;
import de.kacperbak.messages.ZippedMessage;
import de.kacperbak.payload.ZippedPayloadConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

        //zipped byte array to object
        ZippedMessage zippedMessage = ZippedPayloadConverter.gzipToObject(message);
        LOGGER.debug("message.content: {}", zippedMessage.getContent());

        //change content
        zippedMessage.setContent(zippedMessage.getContent() + "012");

        //object to zipped byte array
        return ZippedPayloadConverter.objectToGzip(zippedMessage);
    }
}
