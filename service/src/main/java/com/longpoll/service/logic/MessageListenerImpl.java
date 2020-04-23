package com.longpoll.service.logic;

import org.springframework.stereotype.Service;

@Service
public class MessageListenerImpl implements MessageListener {

    private Object lockObject = new Object();

    public String getMessage() {
        synchronized( lockObject ) {
            try {
                //lockObject.wait();
                Thread.sleep(5 * 1000);
            } catch (InterruptedException ex) {
                System.out.println("Error during wait() -- Details: " + ex);
            }
            return "this is data";
        }
    }

}