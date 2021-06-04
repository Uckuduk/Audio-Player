package NetworkUtils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils {

    private static final String Deezer_BASE_URL = "https://api.deezer.com/";
    private static final String Deezer_GET = "search";
    private static final String Deezer_GET_BY_ID = "track";
    private static final String SONG_NAME = "q";

    public static ArrayList<URL> findTrackById(ArrayList<Integer> IDs){
        ArrayList<URL> URLs = new ArrayList<URL>();

        for (int id: IDs) {
            Uri builtUri = Uri.parse(Deezer_BASE_URL + Deezer_GET_BY_ID + '/' + String.valueOf(id));
            URL url = null;

            try {
                url = new URL(builtUri.toString());
                URLs.add(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return URLs;
    }

    public static URL generateSearchURL(String songName) {
        Uri builtUri = Uri.parse(Deezer_BASE_URL + Deezer_GET)
                .buildUpon()
                .appendQueryParameter(SONG_NAME, songName)
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
