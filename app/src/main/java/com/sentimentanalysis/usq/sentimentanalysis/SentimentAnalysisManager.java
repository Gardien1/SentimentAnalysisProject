package com.sentimentanalysis.usq.sentimentanalysis;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private ScanHistory scanHistoryObj;


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


    public SentimentAnalysisManager(AppCompatActivity appContext , AssetManager inputAssetManager)
    {
        // Instantiate variables.
        assetManager = inputAssetManager;
        context = appContext;


        File scanHistory = new File(context.getFilesDir(), "scan_history.txt");
        // Debug will be fully removed in full version.
        scanHistory.delete();
        if(scanHistory.exists())
        {
            loadScanHistory();
        }
        else
        {
            scanHistoryObj = new ScanHistory();
        }


        subjectList = new SubjectList(appContext , new Lexicon(GetLexicon()),scanHistoryObj);
        listener = null;
        time = new ScanTime();
        final ScanHistory scanHistoryObj = new ScanHistory("none");
        final GraphManager graphManager = new GraphManager(context);

        // Setup interfaces.

        // Functions can be overridden externally.
        // May need to relook to allow for extra functionality.
        listener = new SentimentAnalysisListener() {
            @Override
            public void onFinishAnalysis() {

                UIUpdater.UpdateMainScanTime(context , "Last Scanned: " + time.getCurrentTime());
                scanHistoryObj.setLastScanTime(time.getCurrentTime());
                // Save scan history.
                saveScanHistory(subjectList.getScanHistory());
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
                    userName.setTextSize(23);
                    userName.setPadding(0,15,0,15);
                    userName.setTextColor(Color.parseColor("#ffffff"));
                    output.addView(userName);

                    final Button removeButton = new Button(context);

                    removeButton.setBackgroundResource(R.drawable.button_remove);
                    removeButton.setLayoutParams(new LinearLayout.LayoutParams(400,100));
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
                // TODO: Background processing for setup (MAY REMOVE THIS FUNCTION).

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

                    tmpText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    tmpText.setTextSize(20);
                    tmpText.setPadding(0,12,0,0);
                    tmpText.setText(key + ":" + status );
                    //tmpText.setBackgroundResource(R.drawable.input_background);

                    dataOutput.addView(tmpText);
                }
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

                // Update reset scan time if it exists.
                if(scanHistoryObj.getLastScanTime() != "none")
                {
                    UIUpdater.UpdateMainScanTime(context,scanHistoryObj.getLastScanTimeFormatted());
                }
            }

            @Override
            public void onSettingsViewLoaded() {
                dynamicContentManager.inflateSettingsPage();
                OnClickManager.attachOnHandlersOnSettings(context,viewManager,dataAnalyser);
            }

            @Override
            public void onSetupViewLoaded() {
                OnClickManager.attachHandlersOnSetup(context,viewManager,dataAnalyser);
            }

            @Override
            public void onGraphViewLoaded()
            {
                OnClickManager.attachHandlersOnGraphView(context,viewManager,dataAnalyser);

                // Get graph view.
                GraphView graphView = (GraphView) context.findViewById(R.id.statsGraph);
                graphView.setBackgroundColor(Color.parseColor("#ffffff"));
                graphView.setTitle("Daily Scores");
                // Load Graph Data
                graphManager.setGraph(graphView);

                // TODO: May need to run this on a seperate thread.
                // Test
                ArrayList<Date> x_axis = new ArrayList<Date>();
                ArrayList<Integer> y_axis = new ArrayList<Integer>();


                Calendar testInstance = time.getScanTimeInstance();

                x_axis.add(testInstance.getTime());
                testInstance.add(Calendar.DATE , 1);
                x_axis.add(testInstance.getTime());
                testInstance.add(Calendar.DATE , 1);
                x_axis.add(testInstance.getTime());

                y_axis.add(5);
                y_axis.add(10);
                y_axis.add(7);

                Log.i("ScanHistory" , Integer.toString(subjectList.getScanHistory().getScanHistory().size()));

                // Subject list continually updates the scan list until it is loaded from disk.
                graphManager.updateGraph(subjectList.getScanHistory().getScanHistory(),context);

            }
        });

        dataAnalyser = new DataAnalyser() {
            @Override
            public void processSettingsData(final HashMap<String, String> settingsData) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        Boolean failed = false;
                        for(final String key : settingsData.keySet())
                        {
                            if(!TwitterVerifier.verifyTwitterHandle(settingsData.get(key)))
                            {
                                Log.i("ERROR KEY" , key);
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LinearLayout twitterOuputs = (LinearLayout) context.findViewById(R.id.setup_data_ouput_layout);
                                        TextView errorDiv = twitterOuputs.findViewWithTag("errorElement-"+key);
                                        errorDiv.setVisibility(View.VISIBLE);
                                    }
                                });

                                failed = true;
                            }
                            else
                            {
                                subjectList.addSubject("NONE" , settingsData.get(key));
                            }
                        }

                        if(!failed)
                        {
                            subjectList.saveSubjects();
                            onSubjectSaved();
                        }
                    }
                }).start();
            }
        };
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

        // DEBUG -- WILL NEED TO DELETE
        settingsFile.delete();
        //-------------------------


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
                        listener.onFinishAnalysis();



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
    // Maybe put this function to a history manager.
    private void saveScanHistory(ScanHistory scanHistory)
    {
        File scanHistoryFile = new File(context.getFilesDir(),"scan_history.txt");
        try
        {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(scanHistoryFile));

            output.writeObject(scanHistory);

            output.close();

        }
        catch(Exception e)
        {
            Log.e("Save Scan History" , "Failed to save scan history:" + e.toString() );
        }
    }

    private void loadScanHistory()
    {
        // Bad coding... not reusing.
        // TODO: Rework this code to be reused.
        File scanHistoryFile = new File(context.getFilesDir(),"scan_history.txt");

        try
        {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(scanHistoryFile));
            scanHistoryObj = (ScanHistory) input.readObject();

            input.close();
        }
        catch(Exception e)
        {
            Log.e("Load Scan History" , "Failed to load scan history " + e.toString());
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

    private void onSubjectSaved()
    {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewManager.changeToMainView(context);
            }
        });

    }

}
