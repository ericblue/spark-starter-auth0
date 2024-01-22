package com.ericblue.spark.auth0.service;

import com.ericblue.spark.auth0.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleAPIService {

    /**
     * Logger for this class
     */
    protected static final Logger logger = LoggerFactory.getLogger(SampleAPIService.class);

    /**
     * This method calls the sample API client to send a message
     *
     * @param message Message sent by the user
     * @return String response from the sample API client
     */
    public Message sendMessage(Message message) {

        Message responseMessage = new Message("Hello! The message you sent was: " + message.getText());

        return responseMessage;
    }
}
