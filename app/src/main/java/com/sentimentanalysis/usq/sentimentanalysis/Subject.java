package com.sentimentanalysis.usq.sentimentanalysis;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
	private String name;
	private String twitterHandle;
	private ArrayList<Integer> dailyScore = new ArrayList<Integer>(30);
	private int dailyTrend = 0;
	private Boolean isNegative = false;
	private ArrayList<String> scannedTweets = new ArrayList<String>();

	
	/**
	 * @param n			name of the subject
	 * @param handle	twitter handle, after the {@literal @} symbol
	 */
	public Subject( String n, String handle ) {
		name = n;
		twitterHandle = handle;
	}
	
	public String getName() { return name; };
	public String getTwitterHandle() { return twitterHandle; };
	public ArrayList<Integer> getDailyScore()  { return dailyScore; };
	public Boolean getAlertStatus(){return isNegative; };
	public ArrayList<String> getScannedTweets(){return scannedTweets;};

	public void ResetStatus()
	{
		isNegative = false;
	}

	public void notifyOfNegativeTrend( Context context, NotificationManager notificationManager) {

		NotificationCompat.Builder trendNotification =
				new NotificationCompat.Builder(context, "NT")
						.setContentTitle("Sentiment Analysis")
						.setContentText(name + "has had a trend of more negative days.")
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
	// TODO: Will need to test code ove 3 days.
	public void testDailyTrend( Context context, NotificationManager notificationManager ) {
			if( dailyScore.get(0) < dailyScore.get(1) ) {
				dailyTrend++;
				if(dailyTrend >= 3) {
                    notifyOfNegativeTrend( context, notificationManager );
                    isNegative = true;
					dailyTrend = 0;
				}
			}
			else
				dailyTrend = 0;

			if( dailyScore.size() > 29 )
			    dailyScore.remove( 29 );
			dailyScore.add(0,0);
	}
	
	public void analyseTweets( Lexicon lexicon, TwitterApi twitterApi) {
        HashMap<String,String> tweetList = twitterApi.GetTweetsSingleUser( this );
        //Integer score = dailyScore.get(0);
		Integer score = 0;
        for ( String tweetMapKey : tweetList.keySet() ) {

            score += lexicon.scoreTweet( tweetList.get(tweetMapKey) );
            Log.i("ANALYSE TWEET","Adding tweet id to list: " + tweetMapKey);
            scannedTweets.add(tweetMapKey);

        }

        Log.i("SCORE TEST" , "Handle: " + twitterHandle + " Score: " + score.toString());

        //dailyScore.set( 0,score );
    }

    public void addScannedTweet(String tweetId)
	{
		scannedTweets.add(tweetId);
	}

    @Override
    public String toString() {
		return "Name:" + name +   "\nTwitter Handle:" + twitterHandle + "\nDaily Scores:"
				+ dailyScore.toString();
	}

/*    public void saveData(File newSubjects) {


	    try {
            newSubjects.createNewFile();
            FileWriter writer = new FileWriter(newSubjects);

            writer.write( name + ":" + twitterHandle + "\n" );

            for ( Integer score : dailyScore ) {
                writer.write(score.toString() + " "  );
            }

            writer.write("\n");
            writer.close();

        } catch (IOException e){
	        e.printStackTrace();
        }
    }
    */

}