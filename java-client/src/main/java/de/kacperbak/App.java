package de.kacperbak;

import de.kacperbak.controller.JsonMessageController;
import de.kacperbak.controller.SerializedMessageController;
import de.kacperbak.controller.ZippedMessageController;
import de.kacperbak.handler.JsonMessageHandler;
import de.kacperbak.handler.SerializedMessageHandler;
import de.kacperbak.handler.ZippedMessageHandler;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.Future;

/**
 * Run client application with one of these properties:
 * -Dmsg=json
 * -Dmsg=ser
 * -Dmsg=zip
 */
public class App
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "-----------------------------------" );
        System.out.println( "--- Start websocket java client ---" );
        System.out.println( "-----------------------------------" );
        String messageType = System.getProperty("msg");
        System.out.println( "Message Type:" + messageType);

        System.out.println( "-----------------------------------" );
        System.out.println( "(send) - send message");
        System.out.println( "(exit) - exit" );

        String url = "ws://127.0.0.1:8080/endpoint";
        StompSession.Subscription subscription = null;
        StompSession stompSession = null;
        WebSocketClient transport = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(transport);

        if(MessageType.isJson(messageType)){
            String subscribeUrl = "/topic/json";
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            JsonMessageHandler jsonMessageHandler = new JsonMessageHandler();
            Future<StompSession> stompSessionFuture = stompClient.connect(url, jsonMessageHandler);
            stompSession = stompSessionFuture.get();
            subscription = stompSession.subscribe(subscribeUrl, jsonMessageHandler);
            JsonMessageController runner = new JsonMessageController(stompSession, subscription);
            runner.run();

        } else if(MessageType.isSerialized(messageType)){
            String subscribeUrl = "/topic/ser";
            stompClient.setMessageConverter(new ByteArrayMessageConverter());
            SerializedMessageHandler serializedMessageHandler = new SerializedMessageHandler();
            Future<StompSession> stompSessionFuture = stompClient.connect(url, serializedMessageHandler);
            stompSession = stompSessionFuture.get();
            subscription = stompSession.subscribe(subscribeUrl, serializedMessageHandler);
            SerializedMessageController runner = new SerializedMessageController(stompSession, subscription);
            runner.run();
        } else if(MessageType.isZip(messageType)){
            String subscribeUrl = "/topic/zip";
            stompClient.setMessageConverter(new ByteArrayMessageConverter());
            ZippedMessageHandler zippedMessageHandler = new ZippedMessageHandler();
            Future<StompSession> stompSessionFuture = stompClient.connect(url, zippedMessageHandler);
            stompSession = stompSessionFuture.get();
            subscription = stompSession.subscribe(subscribeUrl, zippedMessageHandler);
            ZippedMessageController runner = new ZippedMessageController(stompSession, subscription);
            runner.run();
        }
    }
}
