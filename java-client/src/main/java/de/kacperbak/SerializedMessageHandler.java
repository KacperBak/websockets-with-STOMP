package de.kacperbak;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;

/**
 * Created by bakka on 30.04.16.
 */
public class SerializedMessageHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializedMessageHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.debug("Connection established");
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        LOGGER.debug("Payload type: '{}'", headers.getContentType().toString());
        return byte[].class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        byte[] serializedPayload = (byte[]) payload;
        try{
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedPayload);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            String type = object.getClass().getName();
            objectInputStream.close();
            byteArrayInputStream.close();
            switch (type){
                case "de.kacperbak.SerializedMessage":
                    SerializedMessage serializedMessage = (SerializedMessage) object;
                    LOGGER.debug("message.content: {}", serializedMessage.getContent());
                    break;
                default:
                    throw new NotImplementedException();
            }
        } catch (Exception e){
            LOGGER.debug("Failed to handle frame.");
            e.printStackTrace();
        }
    }

}
