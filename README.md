# LongPolling
Proof of Concept for doing long-polling in a Spring-based Tomcat-hosted service. Comes with a simple client as well.

## Compile

Run '.\gradlew.bat clean build'

## Service
To start the service run 'java -jar .\service\build\libs\service-0.0.1-SNAPSHOT.war'

## Test the Service
Open a web browser and navigate to 'http://localhost:8083/api/ping'

NOTE: That is port 8083, not the usual port 8080.

You should get back a response that says "pong".

## Thread-Per-Request
Once that works let's test the actual long-polling call. We'll start with a simple implementation that uses a thead-per-request (but not a Tomcat HTTP thread!).

Make a request to 'http://localhost:8083/thread/listen' in 5+ browser tabs simulateously.

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

You can see that the HTTP threads (those that start with "http-nio-") terminate quite quickly. However, we do instantiate a new java thread to do the actual waiting on the message producer.

So this is much better than consuming Tomcat HTTP threads, but it still won't scale as much as we'd like.


