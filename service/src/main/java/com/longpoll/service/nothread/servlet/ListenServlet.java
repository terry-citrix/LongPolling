package com.longpoll.service.nothread.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/servletlisten" }, asyncSupported = true)
public class ListenServlet extends HttpServlet {

    static final long serialVersionUID = 1L;
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
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
                } catch (IOException ex) {
                    System.err.println("IOExcepion in ListenServlet::doGet() -- Details: " + ex);
                }
                aContext.complete();
            }
        });
    }

}
