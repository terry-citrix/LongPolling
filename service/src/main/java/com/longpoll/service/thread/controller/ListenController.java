package com.longpoll.service.thread.controller;

import java.util.concurrent.Callable;

import com.longpoll.service.thread.logic.MessageListener;
import com.longpoll.service.thread.logic.MessageQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("thread")
public class ListenController {

    MessageListener messageListener = null;

    @Autowired
    public ListenController(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @GetMapping("/listen")
    public Callable<String> listen() {
        System.out.println("Accepting '/thread/listen' request in thread " + Thread.currentThread().getName());

        Callable<String> callable = () -> { 
            String response = messageListener.getMessage(MessageQueue.GROUP1); 
            System.out.println("Returning value '" + response + "' to caller on thread " + Thread.currentThread().getName());
            return response;
        };

        System.out.println("Ending thread " + Thread.currentThread().getName());

        return callable;
    }

}
