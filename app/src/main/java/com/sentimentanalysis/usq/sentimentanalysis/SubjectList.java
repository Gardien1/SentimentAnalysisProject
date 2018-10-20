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
import java.util.Calendar;
import java.util.HashMap;

/**
 * A wrapper class for a list of subjects, and a lexicon used to analyse tweets.  Creates
 * on command or based on previously a saved file in which subjects can be saved.
 * @author 	Ben Garden
 * @version 1.0
 * @since	2018-09-12
 */
public class SubjectList {
    private Subject NOBODY = new Subject( "" );
    private Lexicon LEXICON;
    private TwitterApi TWITTER_API = new TwitterApi();
    private ScanHistory scanHistory;
    private ArrayList<Subject> subjects = new ArrayList<Subject>(30);
    private Context applicationContext;
    private NotificationManager notificationManager;

    public HashMap<String,Boolean> getSubjectData()
    {
        HashMap<String,Boolean> data = new HashMap<String,Boolean>();

        for(Subject sub : subjects)
        {
            data.put(sub.getTwitterHandle() , sub.getAlertStatus());
        }

        return data;
    }


    public SubjectList(Context appContext , Lexicon lexiconObj, ScanHistory history, NotificationManager notificationManager)
    {
        applicationContext = appContext;
        LEXICON = lexiconObj;
        scanHistory = history;
        this.notificationManager = notificationManager;
    }

    public void addSubject( String handle ) {
        subjects.add(new Subject( handle ));
    }

    public void loadSubjects() {
        try {

            File currentSubjects = new File( applicationContext.getFilesDir(),"subjects.txt" );

            ObjectInputStream input =
                    new ObjectInputStream(new FileInputStream(currentSubjects));
            Log.i("File Length: ", currentSubjects.getAbsolutePath());

            boolean isDone = !(currentSubjects.exists());
            while (!isDone) {
                try {
                    Object object = input.readObject();

                    Log.i("LOADING TEST", object.toString());
                    if ( !object.equals( " " ) )
                    {
                        Subject subject = (Subject) object;
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
                catch(Exception e)
                {
                    Log.e("LOAD SUBJECT ERROR","SUBJECTS ERROR: " + e.toString());
                    isDone = true;
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
            Log.i("SAVING TEST", "CREATING FILE");
            newSubjects.createNewFile();

            ObjectOutputStream output =
                new ObjectOutputStream(new FileOutputStream( newSubjects ));

            for ( Subject subject : subjects ) {
                output.writeObject( subject );
                Log.i("WRITING TEST", "Writing" + subject.toString());
            }
            output.writeObject( " " );

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
            subjects.get(i).analyseTweets( LEXICON, TWITTER_API, scanHistory, applicationContext, notificationManager );
            Log.i("ANALYSE CALL" , subjects.get(i).getTwitterHandle() + " saving tweets");

        }

        saveSubjects();
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

    public void emptyList()
    {
        subjects.clear();
    }

    public ScanHistory getScanHistory() {
        return scanHistory;
    }

    public void setScanHistory(ScanHistory input){
        scanHistory = input;
    }
}
