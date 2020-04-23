package com.longpoll.service.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.longpoll.service.thread.logic.MessageQueue;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class MessageProducer implements DisposableBean, Runnable {

    private Thread thread;
    private MessageQueue messageQueue;

    @Autowired
    public MessageProducer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;

        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * Every 20 seconds this will produce a new timestamp and push it to the MessageQueue.
     */
    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(20 * 1000);
            } catch (InterruptedException ex) {
                System.err.println("Error in MessageProducer:run() -- Details: " + ex);
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();

            System.out.println("Produced a new message with value: '" + dateFormat.format(date) + "'");
            messageQueue.pushMessage(MessageQueue.GROUP1, dateFormat.format(date));
        }
    }

    @Override
    public void destroy() {
    }

}
