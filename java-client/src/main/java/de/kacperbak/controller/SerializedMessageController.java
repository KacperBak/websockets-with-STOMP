package de.kacperbak.controller;

import de.kacperbak.messages.SerializedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * Created by bakka on 01.05.16.
 */
public class SerializedMessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerializedMessageController.class);
    private static final Scanner SCANNER =  new Scanner(System.in);

    private StompSession stompSession;
    private StompSession.Subscription subscription;

    public SerializedMessageController(StompSession stompSession, StompSession.Subscription subscription) {
        this.stompSession = stompSession;
        this.subscription = subscription;
    }

    public void run() throws Exception {
        boolean loop = true;
        String stdIn = SCANNER.nextLine();

        while (loop){
            switch (stdIn){
                case "send":
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
