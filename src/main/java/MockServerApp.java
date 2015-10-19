import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

class MockServerApp {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_JSON = "application/json; charset=utf-8";

    private static final String PARAM_IP_ADDRESS = "i";
    private static final String PARAM_PORT = "p";
    private static final String PARAM_QUEUED_RESPONSES = "q";

    public static void main(final String[] args) throws Exception {
        final OptionParser parser = new OptionParser() {
            {
                accepts(PARAM_IP_ADDRESS).withRequiredArg().ofType(String.class)
                        .describedAs("ip address to bind to and start the mock server on");
                accepts(PARAM_PORT).withRequiredArg().ofType(Integer.class)
                        .describedAs("port to start the mock server on");
                accepts(PARAM_QUEUED_RESPONSES).withOptionalArg().ofType(Boolean.class)
                        .describedAs("use queued responses, if false uses dispatcher").defaultsTo(false);
            }
        };

        final OptionSet commandLineOptions = parser.parse(args);
        if (!commandLineOptions.hasOptions()) {
            parser.printHelpOn(System.out);
            return;
        }

        final String ipAddress = (String) commandLineOptions.valueOf(PARAM_IP_ADDRESS);
        final Integer port = (Integer) commandLineOptions.valueOf(PARAM_PORT);
        final Boolean useQueuedResponses = (Boolean) commandLineOptions.valueOf(PARAM_QUEUED_RESPONSES);

        final MockWebServer server = new MockWebServer();
        if (useQueuedResponses) {
            System.out.println("Queuing up responses");
            enqueueResponses(server);
        } else {
            System.out.println("Using dispatcher");
            useDispatcher(server);
        }

        server.start(InetAddress.getByAddress(new IpParser(ipAddress).toByteArray()), port);

        System.out.println("Mock server running at: " + server.url(""));
    }

    private static void enqueueResponses(final MockWebServer server) {
        server.enqueue(buildResponse(200, "{}", 10));
    }

    private static MockResponse buildResponse(final int responseCode, final String body, final int delayInSeconds) {
        return new MockResponse()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_JSON)
                .setResponseCode(responseCode)
                .setBody(body)
                .setBodyDelay(delayInSeconds, TimeUnit.SECONDS);
    }

    private static void useDispatcher(MockWebServer server) {
        final Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(final RecordedRequest request) throws InterruptedException {
                System.out.println("Request: " + request.getPath() + " '" + request + "'");

                if (request.getPath().equals("/v1")) {
                    return new MockResponse().setResponseCode(404);
                }
                return new MockResponse().setResponseCode(200).setBody("Hello, world");
            }
        };
        server.setDispatcher(dispatcher);
    }
}