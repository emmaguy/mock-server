mock server
==============

Trying out Square's [Mock Web Server](https://github.com/square/okhttp/tree/master/mockwebserver) as a static mock server.

Run with your favourite IDE directly, or by making a jar.

You can make the jar with an included gradle task, then start the mock server like this:

`java -jar build/libs/mock-server.jar`


`Ctrl+C` to stop it

The mock server has two modes of operation, chosen at runtime with a command line flag:

 - pre-enqueued responses
    - responses don't depend on the path requested, the responses come in the queued order
 - dispatcher
    - responses are based on path

Note that any response can be delayed by a given time period, to simulate slow server.