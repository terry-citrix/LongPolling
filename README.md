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

Once that works let's test the actual long-polling call.  Make a request to 'http://localhost:8083/api/listen'

After a while (not immediately), you should get back a text response.
