package com.example.tarun.unmevents;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Tarun on 2/20/2016.
 */

class DownloadXmlTask extends AsyncTask<String, Void, String> {
    XMLPullParserHandler xmlPullParserHandler = new XMLPullParserHandler();
    InputStream stream;
    @Override
    protected String doInBackground(String... urls) {
        try {
            stream = downloadUrl(urls[0]);
            List<Event> events = xmlPullParserHandler.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    private InputStream downloadUrl(String urlString) throws IOException {
        java.net.URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
    @Override
    protected void onPostExecute(String result) {

    }

}
