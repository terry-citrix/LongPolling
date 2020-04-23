package com.longpoll.service.thread.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerImpl implements MessageListener {

    private MessageQueue messageQueue;

    @Autowired
    public MessageListenerImpl(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public String getMessage(String groupId) {
        Object lock = new Object();
        messageQueue.storeLock(MessageQueue.GROUP1, lock);
//        System.out.println("Waiting on a new message in thread " + Thread.currentThread().getName());

        synchronized(lock) {
            try {
                lock.wait();      // We're going to wait until we get a NEW message.
            } catch (InterruptedException ex) {
                System.out.println("Error during wait() -- Details: " + ex);
            }
        }

        return messageQueue.getMessage(MessageQueue.GROUP1);
    }

}