package de.kacperbak.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Scanner;
import de.kacperbak.messages.JsonMessage;

/**
 * Created by bakka on 01.05.16.
 */
public class JsonMessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMessageController.class);
    private static final Scanner SCANNER =  new Scanner(System.in);

    private StompSession stompSession;
    private StompSession.Subscription subscription;

    public JsonMessageController(StompSession stompSession, StompSession.Subscription subscription) {
        this.stompSession = stompSession;
        this.subscription = subscription;
    }

    public void run() throws Exception {
        boolean loop = true;
        String stdIn = SCANNER.nextLine();

        while (loop){
            switch (stdIn){
                case "send":
                    //create headers
                    StompHeaders jsonStompHeaders = new StompHeaders();
                    jsonStompHeaders.setDestination("/app/json");
                    jsonStompHeaders.setContentType(MediaType.APPLICATION_JSON);

                    //create and send json message
                    JsonMessage jsonMessage = new JsonMessage("123");
                    stompSession.send(jsonStompHeaders, jsonMessage);

                    LOGGER.debug("SEND json message with payload: '{}'", jsonMessage.getContent());
                    System.out.println("SEND json message with payload: '" + jsonMessage.getContent() + "'");
                    stdIn = SCANNER.nextLine();
                    break;

                case "exit":
                    subscription.unsubscribe();
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
}
