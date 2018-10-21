package com.sentimentanalysis.usq.sentimentanalysis;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.os.SystemClock.sleep;

/**
 * An instance of a subject being monitored in the app.  Contains
 * name and twitter handle of the subject, and a list of daily
 * scores from the past 30 days.
 * @author 	Ben Garden
 * @version 1.0
 * @since	2018-09-06
 */
public class Subject implements Serializable{
	private static final long serialVersionUID = 1L;
	private String twitterHandle;
	private ArrayList<Integer> dailyScore = new ArrayList<Integer>(30);
	private Integer dailyTrend = 0;
	private Boolean isNegative = false;
	private ArrayList<String> scannedTweets = new ArrayList<String>();
	private ScanTime lastScanned = new ScanTime();
	private Integer scoreCounter = 0;

	// DEBUG ONLY TO SIMULATE GRAPH
	private int dailyOffset = 0;
	
	/**
	 * @param handle	twitter handle, after the {@literal @} symbol
	 */
	public Subject( String handle ) {
		twitterHandle = handle;
	}

	public Subject( String handle, String scores, String counter) {
        twitterHandle = handle;
        scoreCounter = Integer.parseInt(counter);

        String[] splitScores = scores.replace("[", "")
                .replace("]", "").split(", ");

        for(String score : splitScores) { dailyScore.add(Integer.parseInt(score)); }
    }

	public String getTwitterHandle() { return twitterHandle; };
	public ArrayList<Integer> getDailyScore()  { return dailyScore; };
	public Boolean getAlertStatus(){return isNegative; };
	public ArrayList<String> getScannedTweets(){return scannedTweets;};




	public void notifyOfNegativeTrend( Context context, NotificationManager notificationManager) {
		NotificationCompat.Builder trendNotification =
				new NotificationCompat.Builder(context, "NT")
                        .setSmallIcon(R.drawable.social_alert_icon)
						.setContentTitle("Sentiment Analysis")
						.setContentText(twitterHandle + " has had a trend of more negative days.")
						.setPriority(NotificationCompat.PRIORITY_DEFAULT)
						.setAutoCancel(true);


		notificationManager.notify(1, trendNotification.build());

	}

	/**
	 * Tests whether today's daily score is lower than the previous
	 * day's daily score.  If so, increments the daily trend and
	 * tests whether the daily trend has passed the limit of 3
	 * days.  If passed, notifies the owner, and resets the 
	 * daily trend counter.  If the score is not lower, it resets
	 * the daily trend counter.
	 */
	public void testDailyTrend( Context context, NotificationManager notificationManager ) {
		if( dailyScore.size() > 2 && dailyScore.get(scoreCounter - 1) < dailyScore.get(scoreCounter - 2) ) {
			dailyTrend++;
			if(dailyTrend >= 3) {
				notifyOfNegativeTrend( context, notificationManager );
				isNegative = true;
				dailyTrend = 0;
			}
		}
		else
			dailyTrend = 0;
		    isNegative = false;
	}
	
	public void analyseTweets( Lexicon lexicon, TwitterApi twitterApi, ScanHistory history, Context context, NotificationManager notificationManager) {
        HashMap<String,String> tweetList = twitterApi.GetTweetsSingleUser( this );
        //Integer score = dailyScore.get(0);
		Integer score = 0;
        for ( String tweetMapKey : tweetList.keySet() ) {

            score += lexicon.scoreTweet( tweetList.get(tweetMapKey) );
            Log.i("ANALYSE TWEET","Adding tweet id to list: " + tweetMapKey);
            scannedTweets.add(tweetMapKey);

        }

        Log.i("SCORE TEST" , "Handle: " + twitterHandle + " Score: " + score.toString());

        // Check if it is a new day
		Calendar currentTime = Calendar.getInstance();
		currentTime.add(Calendar.DAY_OF_MONTH , dailyOffset);

		// TODO: Change after showing Daniel
		if(currentTime.get(Calendar.DAY_OF_MONTH) > lastScanned.getScanTimeInstance().get(Calendar.DAY_OF_MONTH) &&
				currentTime.get(Calendar.MONTH) >= lastScanned.getScanTimeInstance().get(Calendar.MONTH))
        //if(true)
        {
			scoreCounter++;
			dailyScore.add( score );
			Log.i("ANALYSE TWEETS" , "NEW DAY. SETTING A NEW SCORE: DAY COUNT: " + dailyScore.size());
            testDailyTrend(context, notificationManager);
		}
		else
		{
			// If score is still in same day. Just add to current one.
            Integer currentScore = score;

            if(dailyScore.size() >= scoreCounter && dailyScore.size() != 0)
            {
                currentScore += dailyScore.get(scoreCounter);
				dailyScore.set(scoreCounter,currentScore);
            }
            else
			{
				dailyScore.add(scoreCounter,currentScore);
			}



			Log.i("ANALYSE TWEETS" , "SAME DAY SAME SCORE");
		}

		history.addToHistory(currentTime.get(Calendar.DAY_OF_MONTH),score);

		// DEBUG WILL BE DELETED IN PRODUCTION.
		dailyOffset++;
    }

    public void addScannedTweet(String tweetId)
	{
		scannedTweets.add(tweetId);
	}

    @Override
    public String toString() {
		return (twitterHandle + ":" + dailyScore + ":" + scoreCounter + "\n");
	}

    public Boolean isValid() { return twitterHandle != ""; }
}
