package com.longpoll.service.nothread.logic;

import javax.servlet.AsyncContext;

public class ContextInfo {

    public AsyncContext context;
    public String name;

    public ContextInfo(AsyncContext context, String name) {
        this.context = context;
        this.name = name;
    }
}