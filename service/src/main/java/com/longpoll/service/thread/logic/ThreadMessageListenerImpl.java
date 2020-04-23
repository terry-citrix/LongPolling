package com.longpoll.service.thread.logic;

import com.longpoll.service.common.MessageProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThreadMessageListenerImpl implements ThreadMessageListener {

    private ThreadMessageQueue messageQueue;

    @Autowired
    public ThreadMessageListenerImpl(ThreadMessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    public String getMessage(String groupId) {
        Object lock = new Object();
        messageQueue.storeLock(MessageProducer.GROUP1, lock);
        System.out.println("Waiting on a new message in thread " + Thread.currentThread().getName());

        synchronized(lock) {
            try {
                lock.wait();      // We're going to wait until we get a NEW message.
            } catch (InterruptedException ex) {
                System.out.println("Error during wait() -- Details: " + ex);
            }
        }

        return messageQueue.getMessage(MessageProducer.GROUP1);
    }

}