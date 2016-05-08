package de.kacperbak;

import de.kacperbak.messages.ZippedMessage;
import de.kacperbak.payload.ZippedPayloadConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

/**
 * Created by bakka on 01.05.16.
 */
public class ZippedMessageHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZippedMessageHandler.class);

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
        byte[] zippedPayload = (byte[]) payload;
        try{
            ZippedMessage zippedMessage = ZippedPayloadConverter.gzipToObject(zippedPayload);
            LOGGER.debug("message.content: {}", zippedMessage.getContent());
        } catch(Exception e){
            LOGGER.debug("Failed to handle frame.");
            e.printStackTrace();
        }
    }
}
