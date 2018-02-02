package connection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Request {
    /**
     * gets the result of a "GET" request * @param url * @return lines received * @throws IOException
     */
    public static ObservableList<String> getFromURL(String url) throws IOException {

        final HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();

        connection.setRequestMethod("GET");

        connection.connect();

        final ObservableList<String> lines = FXCollections.observableList(new ArrayList<>());
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = rd.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

}
