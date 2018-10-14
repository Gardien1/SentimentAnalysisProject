package com.sentimentanalysis.usq.sentimentanalysis;


import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/*
*   Description: Api that accesses twitter feeds.
* */
public class TwitterApi {

    // Fields
    private HashMap<String,String> m_accounts;

    public TwitterApi()
    {
        m_accounts = new HashMap<String,String>();
    }

    public TwitterApi(HashMap<String,String> accountHandles)
    {
        m_accounts = accountHandles;
    }


    /*
     *
     * Description: Retrieves tweets of a single user.
     * Input: Target handle to retrieve data from.
     * Return: ArrayList<String> representing the list of new tweets.
     *
     * */
    public HashMap<String,String> GetTweetsSingleUser(Subject user)
    {
        return RetrieveTweets(user);
    }

    /*
    public ArrayList<String> RetrieveData()
    {

        ArrayList<String> foundTweets = new ArrayList<String>();

        for(String userHandler : m_accounts.keySet())
        {
            foundTweets.addAll(RetrieveTweets(userHandler));
        }

        return foundTweets;
    }
    */

    public void AddAccount(String userHandle)
    {
        m_accounts.put(userHandle , "");
    }

    /*
    *
    * Description: Retrieves Tweets from a designated userHandle. Returns the time stamp e.g. 4h or 23Aug
    * if > then a day. Also returns the actual tweet.
    * Input: Target handle to retrieve data from.
    * Return: HashMap<String,String>.
    * */
    private HashMap<String,String> RetrieveTweets(Subject user)
    {
        HashMap<String,String> foundTweets = new HashMap<String, String>();
        Document parsedDoc = HttpRetriever.getData("https://mobile.twitter.com/" + user.getTwitterHandle());
        Elements tweets = parsedDoc.getElementsByClass("tweet-text");
        Elements timeStamps = parsedDoc.getElementsByClass("timestamp");
        Elements idElements = parsedDoc.getElementsByAttributeValueContaining("name" , "tweet_");
        for(int i = 0; i< idElements.size() ; i++)
        {
            if(!SearchForID(ParseID(idElements.get(i).attr("name")) , user))
            {
                String cleanedTweet = CleanTweet(tweets.get(i).text());
                Log.i("SEARCH TEST" , "NOT FOUND: SCANNING");
                Log.i("SEARCH TEST" , cleanedTweet);
                if(!cleanedTweet.equals(""))
                {
                    foundTweets.put(ParseID(idElements.get(i).attr("name")) , cleanedTweet); } } else {
                Log.i("SEARCH TEST" , "FOUND : IGNORING"); } }


        return foundTweets;

    }

    /*
    *
    * Description: Cleans tweet of all non-alphanumeric characters.
    * Input: Tweet to be cleaned.
    * Return: Cleaned Tweet.
    *
    * */
    private String CleanTweet(String input)
    {
        String cleaned = "";

        cleaned = input.replaceAll("[^A-Za-z0-9 ]" ,"");

        return cleaned;
    }

    /*
    *
    * Description: Cleans the id tags and returns clean ids.
    * Return: String representing id.
    *
    * */
    private String ParseID(String inputId)
    {
        String id = "";

        int idStart = inputId.indexOf('_');

        //Log.d("Start index" , Integer.toString(idStart));

        id = inputId.substring(idStart+1 , inputId.length());

        return id;

    }

    /*
    *
    * Description: Searches list for id.
    * Return: Boolean representing if id is found.
    *
    * */
    private boolean SearchForID(String inputID , Subject user)
    {
        for(String id: user.getScannedTweets())
        {
            if(inputID.equals(id))
            {
                return true;
            }
        }

        return false;
    }

}
