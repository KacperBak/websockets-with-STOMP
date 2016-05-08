package de.kacperbak.controller;

import de.kacperbak.messages.ZippedMessage;
import de.kacperbak.payload.ZippedPayloadConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.Scanner;

/**
 * Created by bakka on 01.05.16.
 */
public class ZippedMessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZippedMessageController.class);
    private static final Scanner SCANNER =  new Scanner(System.in);

    private StompSession stompSession;
    private StompSession.Subscription subscription;

    public ZippedMessageController(StompSession stompSession, StompSession.Subscription subscription) {
        this.stompSession = stompSession;
        this.subscription = subscription;
    }

    public void run() throws Exception {
        boolean loop = true;
        String stdIn = SCANNER.nextLine();

        while (loop){
            switch (stdIn){
                case "send":
                    //create message
                    ZippedMessage zippedMessage = new ZippedMessage("789");

                    //compress payload
                    byte[] zippedBytes = ZippedPayloadConverter.objectToGzip(zippedMessage);

                    //create headers
                    StompHeaders binaryStompHeaders = new StompHeaders();
                    binaryStompHeaders.setDestination("/app/zip");
                    binaryStompHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

                    //send message
                    stompSession.send(binaryStompHeaders, zippedBytes);

                    LOGGER.debug("SEND zipped payload: '{}'", zippedMessage.getContent());
                    System.out.println("STOMP zipped message send: '" + zippedMessage.getContent() + "'");
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
