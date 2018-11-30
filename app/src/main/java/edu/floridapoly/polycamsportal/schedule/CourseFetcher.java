package edu.floridapoly.polycamsportal.schedule;

import android.content.res.Resources;
import android.net.Uri;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.floridapoly.polycamsportal.R;
import edu.floridapoly.polycamsportal.util.PolyAuthenticator;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public final class CourseFetcher {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Fetches a mapping between term names and their IDs for use in other
     * API methods.
     *
     * This does not require authentication to be set up.
     *
     * @return A mapping from term names to integer IDs.
     * @throws IOException If there is an error retrieving or parsing term
     * information.
     */
    public Map<String, Integer> fetchTerms() throws IOException {
        URL url = new URL(new Uri.Builder()
            .scheme("https")
            .authority(PolyAuthenticator.API_DOMAIN)
            .path("terms")
            .build()
            .toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        Map<String, Integer> result = mapper.readValue(conn.getInputStream(),
            new TypeReference<Map<String, Integer>>() {});

        conn.disconnect();
        return result;
    }

    /**
     * Fetches a list of courses for a specified term.
     *
     * This requires that valid authentication information was set up by
     * {@link edu.floridapoly.polycamsportal.util.PolyAuthenticator}
     * In order to work.
     *
     * @param term Term identifier. @see fetchTerms
     * @return A List of the courses for the term.
     * @throws IOException If there is an error retrieving or parsing course
     * information.
     */
    public  List<Course> fetchCourses(int term) throws IOException {
        URL url = new URL(new Uri.Builder()
            .scheme("https")
            .authority(PolyAuthenticator.API_DOMAIN)
            .path("courses")
            .appendQueryParameter("term", String.valueOf(term))
            .build()
            .toString());

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        List<Course> result = mapper.readValue(conn.getInputStream(),
            new TypeReference<List<Course>>() {});

        conn.disconnect();
        return result;
    }

}
