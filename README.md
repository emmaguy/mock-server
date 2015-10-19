mock server
==============

Trying out Square's [Mock Web Server](https://github.com/square/okhttp/tree/master/mockwebserver) as a static mock server.

Run with your favourite IDE directly, or by making a jar.

Make the jar with the included gradle task:

`./gradlew jar`

Then start the mock server like this:

`java -jar build/libs/mock-server.jar -i 10.1.34.165 -p 50444`

Run with the following command line options:

`
Option                                 Description
------                                 -----------
-i <ip address to bind to and start
  the mock server on>
-p <port to start the mock server on>
-q [Boolean: (default: false) use queued responses, if false uses dispatcher]
`

`Ctrl+C` to stop it

The mock server has two modes of operation, chosen at runtime with a command line flag:

 - pre-enqueued responses
    - responses don't depend on the path requested, the responses come in the queued order
 - dispatcher
    - responses are based on path

Note that any response can be delayed by a given time period, to simulate slow server.