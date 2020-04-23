package com.longpoll.service.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.longpoll.service.nothread.logic.NoThreadMessageQueue;
import com.longpoll.service.thread.logic.ThreadMessageQueue;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This feeds new messages into both ThreadMessageQueue as well as NoThreadMessageQueue.
 */
@Component
public class MessageProducer implements DisposableBean, Runnable {

    public final static String GROUP1 = "group1";

    private Thread thread;
    private ThreadMessageQueue threadMessageQueue;
    private NoThreadMessageQueue nothreadMessageQueue;

    @Autowired
    public MessageProducer(ThreadMessageQueue threadMessageQueue, NoThreadMessageQueue nothreadMessageQueue) {
        this.threadMessageQueue = threadMessageQueue;
        this.nothreadMessageQueue = nothreadMessageQueue;

        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * Every 20 seconds this will produce a new timestamp and push it to the ThreadMessageQueue.
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

            String message = dateFormat.format(date);
            System.out.println("Produced a new message with value: '" + message + "'");
            
            threadMessageQueue.pushMessage(GROUP1, message);
            nothreadMessageQueue.pushMessage(GROUP1, message);
        }
    }

    @Override
    public void destroy() {
    }

}
