package com.sentimentanalysis.usq.sentimentanalysis;

import android.util.Log;

import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/*
 *   Description: Verifies if twitter account details.
 * */
public abstract class TwitterVerifier {

    public static boolean verifyTwitterHandle(String twitterHandle)
    {
        // Attempt to open url.
        Document twitterPage = HttpRetriever.getData("https://mobile.twitter.com/" + twitterHandle );

        if(twitterPage != null)
        {
            // Check if an error div exists.
            if(twitterPage.getElementsByClass("errorpage-body-content").size() > 0)
            {
                Log.d("VerifyTwitterHandle", "HTTP failed - handle not found");
                return false;
            }

            Log.d("DEBUG", "HTTP passed - Found twitter handle");
            return true;
        }
        Log.d("VerifyTwitterHandle", "HTTP failed - Page could not be found");
        return false;
    }



    public static boolean verifyTwitterHandles(HashMap<String,String> twitterHandles)
    {


        for(String key : twitterHandles.keySet())
        {
            if(!verifyTwitterHandle(twitterHandles.get(key)))
            {
                return false;
            }
        }

        return true;
    }

}
