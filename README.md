# LongPolling
Proof of Concept for doing long-polling in a Spring-based Tomcat-hosted service.

NOTE: This is the sole creation of Thierry Duchastel, in his own free time and developed at home on his own hardware and software. This is *not* affiliated with any corporation, and is solely a proof-of-concept. No support of any kind is implied or provided.


## Compile

Run *.\gradlew.bat clean build*

## Service
To start the service run *java -jar .\service\build\libs\service-0.0.1-SNAPSHOT.war*

## Test the Service
Open a web browser and navigate to http://localhost:8083/api/ping

NOTE: That is port 8083, not the usual port 8080.

You should get back a response that says "pong".

## Thread-Per-Request
Once that works let's test the actual long-polling call. We'll start with a simple implementation that uses a thead-per-request (but not a Tomcat HTTP thread!).

Make a request to http://localhost:8083/thread/listen in 5+ browser tabs simultaneously.

After a while (not immediately), you should get back a text response. Check the server logs and you'll see something like this:

```
Returning value '2020/04/23 12:31:57' to caller on thread task-1
Accepting '/api/listen' request in thread http-nio-8083-exec-2
Ending thread http-nio-8083-exec-2
Waiting on a new message in thread task-2
Accepting '/api/listen' request in thread http-nio-8083-exec-4
Ending thread http-nio-8083-exec-4
Waiting on a new message in thread task-3
Accepting '/api/listen' request in thread http-nio-8083-exec-5
Ending thread http-nio-8083-exec-5
Waiting on a new message in thread task-4
Accepting '/api/listen' request in thread http-nio-8083-exec-6
Ending thread http-nio-8083-exec-6
Waiting on a new message in thread task-5
Produced a new message with value: '2020/04/23 12:32:17'
Returning value '2020/04/23 12:32:17' to caller on thread task-5
Returning value '2020/04/23 12:32:17' to caller on thread task-4
Returning value '2020/04/23 12:32:17' to caller on thread task-3
Returning value '2020/04/23 12:32:17' to caller on thread task-2
```

You can see that the HTTP threads (those that start with "http-nio-") terminate quite quickly. However, we do instantiate a new Java thread to do the actual waiting on the message producer.

So this is much better than consuming Tomcat HTTP threads, but it still won't scale as much as we'd like.

## AsyncContext-Per-Thread

In order to avoid holding onto a Thread per request we can use HttpServlet's support for AsyncContext.

Make a request to http://localhost:8083/nothread/listen in 5+ browser tabs simultaneously.

After a while (not immediately), you should get back a text response. Check the server logs and you'll see something like this:

```
Accepting '/nothread/listen' request in thread http-nio-8083-exec-1
Freeing up thread http-nio-8083-exec-1
Accepting '/nothread/listen' request in thread http-nio-8083-exec-3
Freeing up thread http-nio-8083-exec-3
Accepting '/nothread/listen' request in thread http-nio-8083-exec-1
Freeing up thread http-nio-8083-exec-1
Accepting '/nothread/listen' request in thread http-nio-8083-exec-2
Freeing up thread http-nio-8083-exec-2
Accepting '/nothread/listen' request in thread http-nio-8083-exec-3
Freeing up thread http-nio-8083-exec-3
Accepting '/nothread/listen' request in thread http-nio-8083-exec-1
Freeing up thread http-nio-8083-exec-1
Produced a new message with value: '2020/04/23 18:52:30'
Creating response for asyncContext from original thread http-nio-8083-exec-1 in NEW thread http-nio-8083-exec-2
Creating response for asyncContext from original thread http-nio-8083-exec-1 in NEW thread http-nio-8083-exec-1
Creating response for asyncContext from original thread http-nio-8083-exec-3 in NEW thread http-nio-8083-exec-3
Creating response for asyncContext from original thread http-nio-8083-exec-3 in NEW thread http-nio-8083-exec-1
Creating response for asyncContext from original thread http-nio-8083-exec-2 in NEW thread http-nio-8083-exec-2
Creating response for asyncContext from original thread http-nio-8083-exec-1 in NEW thread http-nio-8083-exec-3
```

In this environment I only have 3 Tomcat HTTP Threads in my request thread pool. You can see that I made 6 requests, and each one is handled.  That's because after each invocation the HTTP thread is freed up and is available to handle a new HTTP request.

Then a new message is produced, and we see that all 6 requests are quickly responded to.

To use a different thread pool see https://stackoverflow.com/questions/17287686/how-to-run-a-different-thread-in-a-servlet
