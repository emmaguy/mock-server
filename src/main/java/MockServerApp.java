import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import java.util.concurrent.TimeUnit;

class MockServerApp {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_JSON = "application/json; charset=utf-8";

    public static void main(final String[] args) throws Exception {
        final boolean enqueueResponses = args.length > 0 && args[0].equals("e");

        final MockWebServer server = new MockWebServer();

        if (enqueueResponses) {
            System.out.println("Queuing up responses");
            enqueueResponses(server);
        } else {
            System.out.println("Using dispatcher");
            useDispatcher(server);
        }

        server.start();

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
                if (request.getPath().equals("/v1")) {
                    return new MockResponse().setResponseCode(404);
                }
                return new MockResponse().setResponseCode(200).setBody("Hello, world");
            }
        };
        server.setDispatcher(dispatcher);
    }
}