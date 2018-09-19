package com.sentimentanalysis.usq.sentimentanalysis;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SentimentAnalysisManager {

    private SubjectList subjectList;
    private AssetManager assetManager;
    private AppCompatActivity context;
    private SentimentAnalysisListener listener;
    private boolean hasStartedAnalysis = false;
    private UIInflater dynamicContentManager;
    private ViewManager viewManager;
    private OnClickManager clickEventManager;
    private DataAnalyser dataAnalyser;
    private ViewDataGatherer viewDataGatherer;
    private ScanTime time;



    public boolean getStartedAnalysis()
    {
        return hasStartedAnalysis;
    }
    public HashMap<String,Boolean> getSubjects()
    {
        return subjectList.getSubjectData();
    }

    public void setEventListener(SentimentAnalysisListener inputListener)
    {
        listener = inputListener;
    }


    public HashMap<String,Boolean> getTwitterHandlerDetails()
    {
        return subjectList.getSubjectData();
    }


    // Event listener interface
    public interface  SentimentAnalysisListener
    {
        public void onFinishAnalysis();

        public void onDataLoaded();
    }


    // TODO: Graph backend module and frontend.
    // TODO: Twitter handle verification.
    public SentimentAnalysisManager(AppCompatActivity appContext , AssetManager inputAssetManager)
    {
        // Setup interfaces.

        // Functions can be overridden externally.
        // May need to relook to allow for extra functionality.
        listener = new SentimentAnalysisListener() {
            @Override
            public void onFinishAnalysis() {

                UIUpdater.UpdateMainScanTime(context , "Last Scanned: " + "[ADD TIME STUFF]");
            }

            @Override
            public void onDataLoaded() {

            }
        };


        dynamicContentManager = new UIInflater() {
            @Override
            public void inflateSettingsPage() {
                final LinearLayout output = (LinearLayout) context.findViewById(R.id.setup_data_ouput_layout);

                for(final String key : getTwitterHandlerDetails().keySet())
                {
                    final TextView userName = new TextView(context);
                    userName.setText(key);
                    output.addView(userName);

                    final Button removeButton = new Button(context);

                    removeButton.setText("Remove");

                    removeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            subjectList.removeSubject(key);
                            output.removeView(removeButton);
                            output.removeView(userName);
                        }
                    });

                    output.addView(removeButton);
                }
            }

            @Override
            public void inflateSetupPage() {
                // TODO: Background processing for setup.

            }

            @Override
            public void inflateMainPage() {
                LinearLayout dataOutput = (LinearLayout) context.findViewById(R.id.twitterData_layout);

                for(String key : getTwitterHandlerDetails().keySet())
                {
                    String status = "Safe";

                    if(getTwitterHandlerDetails().get(key))
                    {
                        status = "At risk!";
                    }

                    TextView tmpText = new TextView(context);

                    tmpText.setText(key + ":" + status );

                    dataOutput.addView(tmpText);
                }
            }
        };

        dataAnalyser = new DataAnalyser() {
            @Override
            public void processSettingsData(HashMap<String, String> settingsData) {
                for(String key : settingsData.keySet())
                {
                    subjectList.addSubject("NONE" , settingsData.get(key));
                }

                subjectList.saveSubjects();
            }
        };

        viewManager = new ViewManager(new ViewManager.EventListener() {
            @Override
            public void onMainViewLoaded() {
                // Attach button handlers.
                OnClickManager.attachHandlersOnMain(context,viewManager);
                dynamicContentManager.inflateMainPage();

                // Start the scan.
                // Ensure analysis is only started once.
                if(!hasStartedAnalysis)
                {
                    beginAnalysis();
                    hasStartedAnalysis = true;
                }
            }

            @Override
            public void onSettingsViewLoaded() {
                // TODO: Run settings background processes.
                // TODO: Handle errors.

                dynamicContentManager.inflateSettingsPage();
                OnClickManager.attachOnHandlersOnSettings(context,viewManager,dataAnalyser);
            }

            @Override
            public void onSetupViewLoaded() {
                // TODO: Handle errors.
                OnClickManager.attachHandlersOnSetup(context,viewManager,dataAnalyser);
            }
        });

        // Instantiate variables.
        assetManager = inputAssetManager;
        context = appContext;
        subjectList = new SubjectList(appContext , new Lexicon(GetLexicon()));
        listener = null;
        time = new ScanTime();

    }

    /**
     *
     * @Description: Starts the manager to begin handling views,handlers and processors.
     *
     * */
    public void start()
    {
        // Check for first time use.
        File settingsFile = new File(context.getFilesDir() , "subjects.txt");

        //settingsFile.delete();

        if(settingsFile.exists())
        {
            // Load Data.
            loadData();
            viewManager.changeToMainView(context);
        }
        else
        {
            viewManager.changeToSetupView(context);
        }
    }

    /*
    // Performs setup of twitter handles.
    public void performSetup(ArrayList<String> twitterHandles)
    {
        // Create a subject for each handle.
        for(int i=0; i < twitterHandles.size(); i++)
        {
            Log.i("Adding Subject" , twitterHandles.get(i));
            subjectList.addSubject("NONAME" , twitterHandles.get(i));
        }

        // Need to edit save file.
        subjectList.saveSubjects();
        Log.i("Adding Subject" , "Subjects Saved");

        listener.onDataLoaded();
    }
    */
    // Loads required data from file.
    public void loadData()
    {
        // Need to add load scanned list per subject
        subjectList.loadSubjects();
    }

    public void saveReloadData()
    {
        subjectList.saveSubjects();
        subjectList.loadSubjects();
    }


    public void removeSubject(String twitterHandle)
    {
        if(subjectList.removeSubject(twitterHandle))
        {
            Log.i("REMOVE SUBJECT" , "SUBJECT REMOVED");
        }
    }

    // Performs analysing of twitter handles.
    // Will need to apply a loop.
    public void beginAnalysis()
    {
        try
        {
            hasStartedAnalysis = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                try
                {
                    while(true)
                    {
                        Log.i("MANAGER CALL" , "TWEETS ANALYSIS STARTING....");
                        subjectList.analyseTweets();
                        Log.i("MANAGER CALL" , "TWEETS ANALYSIS FINISHED....");
                        subjectList.saveSubjects();

                        // If behaviour is overidden
                        // May need to relook at this.
                        if(listener != null)
                        {
                            listener.onFinishAnalysis();
                        }
                        else
                        {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UIUpdater.UpdateMainScanTime(context , "Last Scanned: " + time.getCurrentTime());
                                }
                            });

                        }


                        Thread.sleep(60000);
                    }
                }
                catch(Exception e)
                {
                    Log.i("ANALYSIS" , "LOOPING ERROR: " + e.toString());
                }

                }
            }).start();
        }
        catch(Exception e)
        {
            Log.i("ANALYSE ERROR" , e.toString());
        }

    }

    private ArrayList<String> GetLexicon()
    {
        Scanner reader;
        ArrayList<String> lexiconData = new ArrayList<String>();

        try
        {
            // Read lexicon.
            reader = new Scanner(assetManager.open("OpinionFinder_Lexicon.tff") );
            while ( reader.hasNextLine() )
                lexiconData.add(reader.nextLine());
            reader.close();
        }
        catch(Exception e)
        {
            Log.e("LEXICON ERROR", "Failed to read lexicon!: " + e.toString());
        }

        return lexiconData;
    }

}
