package NetworkUtils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String AUTH = "http://www.last.fm/api/auth/?api_key";
    private static final String LFM_BASE_URL = "https://api.deezer.com/";
    private static final String LFM_GET = "search";
    private static final String SONG_NAME = "q";
    private static final String API_KEY = "api_key";
    private static final String FORMAT = "format";


    public static URL generateSearchURL(String songName) {
        Uri builtUri = Uri.parse(LFM_BASE_URL + LFM_GET)
                .buildUpon()
                .appendQueryParameter(SONG_NAME, songName)
                /*.appendQueryParameter(API_KEY, "75b1099b6c157b456a9ca15bbc59a743")
                .appendQueryParameter(FORMAT, "json")*/
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromURL(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }


    }
}
