package com.longpoll.service.nothread.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.longpoll.service.common.MessageProducer;
import com.longpoll.service.nothread.logic.ContextInfo;
import com.longpoll.service.nothread.logic.NoThreadMessageQueue;

@WebServlet(
    urlPatterns = { "/nothread/listen" },
    name = "NoThreadListener",
    description = "A sample message listener that uses no threads while waiting.",
    loadOnStartup = 1,
    asyncSupported = true)
public class ListenServlet extends HttpServlet {

    static final long serialVersionUID = 6111857423L;

    private NoThreadMessageQueue messageQueue;
   
    public ListenServlet(NoThreadMessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void init() throws ServletException {
        // Do required initialization
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Accepting '/nothread/listen' request in thread " + Thread.currentThread().getName());

        response.setContentType("text/html;charset=UTF-8");
        
        final AsyncContext context = request.startAsync();
        ContextInfo contextInfo = new ContextInfo(context, Thread.currentThread().getName());

        messageQueue.storeContext(MessageProducer.GROUP1, contextInfo);

        System.out.println("Freeing up thread " + Thread.currentThread().getName());
    }

}
