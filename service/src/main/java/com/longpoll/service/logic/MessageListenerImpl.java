package com.longpoll.service.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerImpl implements MessageListener {

    private Object lockObject;
    private MessageQueue messageQueue;

    @Autowired
    public MessageListenerImpl(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public String getMessage(String groupId) {
        lockObject = messageQueue.getLock(MessageQueue.GROUP1);
        synchronized(lockObject) {
            try {
                lockObject.wait();      // We're going to wait until we get a NEW message.
            } catch (InterruptedException ex) {
                System.out.println("Error during wait() -- Details: " + ex);
            }
        }

        return messageQueue.getMessage(MessageQueue.GROUP1);
    }

}