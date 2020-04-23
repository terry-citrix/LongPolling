package com.longpoll.service.nothread.logic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.DisposableBean;

public class CompleteResponse implements DisposableBean, Runnable {

    private ContextInfo contextInfo = null;
    private NoThreadMessageQueue messageQueue = null;
    private String groupId = null;

    public CompleteResponse(ContextInfo contextInfo, NoThreadMessageQueue messageQueue, String groupId) {
        this.contextInfo = contextInfo;
        this.messageQueue = messageQueue;
        this.groupId = groupId;
    }

    @Override
    public void run() {
        System.out.println("Creating response for asyncContext from original thread " + contextInfo.name + " in NEW thread " + Thread.currentThread().getName());

        HttpServletResponse response = (HttpServletResponse) contextInfo.context.getResponse();

        response.setStatus(200);
        try {
            PrintWriter writer = response.getWriter();
            writer.println(messageQueue.getMessage(groupId));
            writer.flush();
        } catch (IOException ex) {
            System.err.println("IOExcepion in CompleteResponse::run() -- Details: " + ex);
        }
        contextInfo.context.complete();
    }

    @Override
    public void destroy() {
    }
}