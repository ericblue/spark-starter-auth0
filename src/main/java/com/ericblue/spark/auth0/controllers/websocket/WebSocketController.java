package com.ericblue.spark.auth0.controllers.websocket;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.ericblue.spark.auth0.controllers.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.ericblue.spark.auth0.exception.AppException;

import com.fasterxml.jackson.databind.ObjectMapper;


@WebSocket
public class WebSocketController extends BaseController {

    /**
     * Logger for this class
     */
    protected static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);
    private ObjectMapper mapper = new ObjectMapper();

    public WebSocketController() {
        logger.debug("Initializing Controller " + this.getClass().getName());

    }

    
    // Store sessions if you want to, for example, broadcast a message to all
    // users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();


    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);

    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        // Stop recording in case a page reload happens

        logger.debug("Web socket closed!");

        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws AppException, IOException, InterruptedException {
        logger.debug("Got: " + message);

        //session.getRemote().sendString(response);

    }


}
