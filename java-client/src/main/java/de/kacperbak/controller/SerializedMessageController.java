package de.kacperbak.controller;

import de.kacperbak.handler.SerializedMessageHandler;
import de.kacperbak.messages.SerializedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.Base64Utils;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.concurrent.Future;

/**
 * Created by bakka on 01.05.16.
 */
public class SerializedMessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializedMessageController.class);
    private static final Scanner SCANNER =  new Scanner(System.in);

    private WebSocketStompClient stompClient;
    private StompSession stompSession;
    private StompSession.Subscription subscription;
    private int connectionCounter = 0;

    public SerializedMessageController(WebSocketStompClient stompClient) {
        this.stompClient = stompClient;
    }

    public void run() throws Exception {
        boolean loop = true;
        String stdIn = SCANNER.nextLine();

        while (loop){
            switch (stdIn){
                case "c":
                    String userName = "kacper" + connectionCounter;
                    connect(userName);
                    connectionCounter++;
                    stdIn = SCANNER.nextLine();
                    break;

                case "send":
                    if(this.stompSession != null){
                        //create and serialize bin message
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        SerializedMessage serializedMessage = new SerializedMessage("456");
                        objectOutputStream.writeObject(serializedMessage);
                        byte[] serializedBytes = byteArrayOutputStream.toByteArray();
                        objectOutputStream.close();
                        byteArrayOutputStream.close();

                        //create headers
                        StompHeaders binaryStompHeaders = new StompHeaders();
                        binaryStompHeaders.setDestination("/app/ser");
                        binaryStompHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                        //send message
                        stompSession.send(binaryStompHeaders, serializedBytes);

                        LOGGER.debug("SEND serialized payload: '{}'", serializedMessage.getContent());
                        System.out.println("STOMP serialized message send: '" + serializedMessage.getContent() + "'");
                    }
                    stdIn = SCANNER.nextLine();
                    break;

                case "exit":
                    if(subscription != null){
                        subscription.unsubscribe();
                    }
                    loop = false;
                    System.out.println("App exit.");
                    break;

                default:
                    String commandNotMapped = "Command '" + stdIn + "' not mapped!";
                    LOGGER.debug(commandNotMapped);
                    System.out.println(commandNotMapped);
                    stdIn = SCANNER.nextLine();
                    break;
            }
        }
    }

    private void connect(String userName) throws Exception {
        String url = "ws://127.0.0.1:8080/endpoint";
        stompClient.setMessageConverter(new ByteArrayMessageConverter());
        SerializedMessageHandler serializedMessageHandler = new SerializedMessageHandler();
        Future<StompSession> stompSessionFuture = stompClient.connect(url, createWebSocketHttpHeaders(userName), serializedMessageHandler);
        stompSession = stompSessionFuture.get();
        String subscribeUrl = "/topic/ser";
        this.subscription = stompSession.subscribe(subscribeUrl, serializedMessageHandler);
        LOGGER.debug("Connection established with count: '{}'", connectionCounter);
    }

    public static WebSocketHttpHeaders createWebSocketHttpHeaders(String userName){
        //basic authentication
        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        String usernameAndPassword = userName + ":password";
        String authorizationValue = "Basic " + Base64Utils.encodeToString(usernameAndPassword.getBytes());
        webSocketHttpHeaders.set(WebSocketHttpHeaders.AUTHORIZATION, authorizationValue);
        return webSocketHttpHeaders;
    }
}
