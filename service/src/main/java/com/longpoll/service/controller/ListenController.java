package com.longpoll.service.controller;

import com.longpoll.service.logic.MessageListener;

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
        return messageListener.getMessage();
    }

}
