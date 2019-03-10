package gdp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import solutions.api.rest.Caller;

import java.io.IOException;
import java.net.URISyntaxException;

/***
 * A class for making calls to FREDAPI. Here we limited the types of calls to only /fred/series/observations endpoint,
 * however, one can add more endpoints by defining more queryBuilders and passing the parameters necessary for making calls
 */
public class FredCaller implements Caller {
    private static final String OBS_START = "1600-01-01";
    private static final String OBS_END = "9999-12-31";
    private ResponseHandler<String> handler = new BasicResponseHandler();

    private final URIBuilder queryBuilder = new URIBuilder()
            .setScheme("https")
            .setHost("api.stlouisfed.org")
            .setPath("/fred/series/observations")
            .addParameter("series_id", "GDP")
            .addParameter("api_key", "ede656b35ff6181721fea581fc740b1e")
            .addParameter("file_type", "json");

    private HttpClient client = HttpClientBuilder.create().build();
    // Here we can define specific requests as well, however, we wish to hit the endpoint for gdp category
    //public List<GdpFredEntry>

    public HttpResponse call(HttpGet request) throws IOException {
        return client.execute(request);
    }

    private HttpGet requestBuilder(String start, String end) throws URISyntaxException {
        String observation_start = start != null ? start : OBS_START;
        String observation_end = end != null ? end : OBS_END;

        return new HttpGet(queryBuilder
                .addParameter("observation_start", observation_start)
                .addParameter("observation_end", observation_end)
                .build());
    }

    /**
     * Given start date and end date, this method will call the FRED API and return the result as a JSON string.
     * More arguments can be added
     * @param start
     * @param end
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public String collectGdpFromAPI(String start, String end) throws URISyntaxException, IOException {
        return handler.handleResponse(call(requestBuilder(start, end)));
    }

}
