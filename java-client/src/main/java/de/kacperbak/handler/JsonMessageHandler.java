package de.kacperbak.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import de.kacperbak.messages.JsonMessage;

/**
 * Created by bakka on 30.04.16.
 */
public class JsonMessageHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMessageHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.debug("Connection established");
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        LOGGER.debug("Payload type: '{}'", headers.getContentType().toString());
        return Object.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            Collection<JsonMessage> collection = ((LinkedHashMap) payload).values();
            List<JsonMessage> greetings = mapper.convertValue(collection, new TypeReference<List<JsonMessage>>(){});
            for (JsonMessage greeting : greetings) {
                LOGGER.debug("Server message: '{}'", greeting.getContent());
                System.out.println("Server message: " + greeting.getContent());
            }
        } catch(Exception e){
            LOGGER.debug("Failed to handle frame.");
            e.printStackTrace();
        }
    }

}
