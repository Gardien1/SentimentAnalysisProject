package com.sentimentanalysis.usq.sentimentanalysis;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A wrapper class for a list of subjects, and a lexicon used to analyse tweets.  Creates
 * on command or based on previously a saved file in which subjects can be saved.
 * @author 	Ben Garden
 * @version 1.0
 * @since	2018-09-12
 */
public class SubjectList {
    private Subject NOBODY = new Subject( "", "" );
    private Lexicon LEXICON;
    private TwitterApi TWITTER_API = new TwitterApi();
    private ArrayList<Subject> subjects = new ArrayList<Subject>(30);
    private Context applicationContext;

    public HashMap<String,Boolean> getSubjectData()
    {
        HashMap<String,Boolean> data = new HashMap<String,Boolean>();

        for(Subject sub : subjects)
        {
            data.put(sub.getTwitterHandle() , sub.getAlertStatus());
        }

        return data;
    }


    public SubjectList(Context appContext , Lexicon lexiconObj)
    {
        applicationContext = appContext;
        LEXICON = lexiconObj;
    }

    public void addSubject( String name,  String handle ) {
        subjects.add(new Subject( name,  handle ));
    }

    public void loadSubjects() {
        try {

            File currentSubjects = new File( applicationContext.getFilesDir(),"subjects.txt" );

            ObjectInputStream input =
                    new ObjectInputStream(new FileInputStream(currentSubjects));

            boolean isDone = false;
            while (!isDone) {
                try {

                    Subject subject = (Subject) input.readObject();
                    if (subject != NOBODY)
                    {
                        Log.i("LOAD SUBJECTS" , "Scanned tweet size: " + subject.getScannedTweets().size());
                        subjects.add(subject);
                    }
                    else
                    {
                        isDone = true;
                    }

                } catch (ClassNotFoundException exception) {
                    isDone = true;
                    exception.printStackTrace();
                }
            }

            input.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void saveSubjects()  {
        File newSubjects = new File( applicationContext.getFilesDir() ,"subjects_new.txt" );
        File currentSubjects = new File( applicationContext.getFilesDir(),"subjects.txt" );
        File subjectBackup = new File( applicationContext.getFilesDir(),"subjects.bak" );

        try {
            newSubjects.createNewFile();

            ObjectOutputStream output =
                new ObjectOutputStream(new FileOutputStream( newSubjects ));

            for ( Subject subject : subjects ) {
                output.writeObject( subject );
            }

            subjectBackup.delete();
            currentSubjects.renameTo( subjectBackup );
            newSubjects.renameTo( currentSubjects );

            output.close();

        } catch (IOException exception) {

            exception.printStackTrace();
        }
    }

    public void analyseTweets() {

        for(int i=0; i < subjects.size() ; i++)
        {
            Log.i("ANALYSE CALL" , subjects.get(i).getTwitterHandle());
            subjects.get(i).analyseTweets(LEXICON, TWITTER_API );
            Log.i("ANALYSE CALL" , subjects.get(i).getTwitterHandle() + " saving tweets");
        }
    }

    public void testDailyTrends(Context context, NotificationManager notificationManager ) {
        for ( Subject subject :  subjects ) {
            subject.testDailyTrend( context, notificationManager );
        }
    }

    public boolean removeSubject(String twitterHandle)
    {
        for(Subject sub : subjects)
        {
            if(sub.getTwitterHandle().toLowerCase() == twitterHandle.toLowerCase())
            {
                subjects.remove(sub);
                return true;
            }
        }

        return false;
    }
}
