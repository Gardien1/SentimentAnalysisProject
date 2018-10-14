package com.sentimentanalysis.usq.sentimentanalysis;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @Description: Retrieves data from specified urls.
 * @author: Bryce Woods
 * @version: 1.0
 * @LastUpdated: 13/10/2018
 *
 */
public abstract class HttpRetriever {

    /**
     *
     * @Description: Retrieve http document form url.
     * @Input: String - Url of document to access.
     * @Return: Document - Jsoup document representing the document at the input url.
     *
     * */
    public static Document getData(String url)
    {
        try
        {
            URL selectedUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) selectedUrl.openConnection();
            connection.setRequestMethod("GET");

            InputStream input = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            StringBuffer response = new StringBuffer();

            String line;

            while((line = reader.readLine()) != null)
            {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            return Jsoup.parse(response.toString());

        }
        catch(Exception e)
        {
            Log.e("HttpRetriever", e.toString() + " Error getting url data , Trying to get: " + url);
        }

        return null;
    }
}
