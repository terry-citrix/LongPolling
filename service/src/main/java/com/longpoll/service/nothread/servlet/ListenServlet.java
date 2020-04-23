package com.longpoll.service.nothread.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    urlPatterns = { "/nothread/listen" },
    name = "NoThreadListener",
    description = "A sample message listener that uses no threads while waiting.",
    loadOnStartup = 1,
    asyncSupported = true)
public class ListenServlet extends HttpServlet {

    static final long serialVersionUID = 5011857421L;
   
    public ListenServlet() {
        // TEMP FIXME TODO
        System.out.println("ListenServlet constructor");
    }

    @Override
    public void init() throws ServletException {
        // TEMP FIXME TODO
        System.out.println("Starting up the ListenServlet");

        // Do required initialization
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Accepting '/nothread/listen' request in thread " + Thread.currentThread().getName());

        response.setContentType("text/html;charset=UTF-8");
        final AsyncContext aContext = request.startAsync();

        aContext.start(new Runnable() {
            public void run() {
                String result = "Hi";
                HttpServletResponse response = (HttpServletResponse) aContext.getResponse();

                response.setStatus(200);
                try {
                    PrintWriter writer = response.getWriter();
                    writer.println(result);
                    writer.flush();
                } catch (IOException ex) {
                    System.err.println("IOExcepion in ListenServlet::doGet() -- Details: " + ex);
                }
                aContext.complete();
            }
        });
    }

}
