package com.longpoll.service.controller;

import com.longpoll.service.logic.MessageListener;
import com.longpoll.service.logic.MessageQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ListenController {

    MessageListener messageListener = null;

    @Autowired
    public ListenController(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @GetMapping("/listen")
    public String listen() {
        System.out.println("Accepting '/api/listen' request; waiting on new message; thread " + Thread.currentThread().getId());

        String response = messageListener.getMessage(MessageQueue.GROUP1);

        System.out.println("Returning value '" + response + "' to caller");
        return response;
    }

}
