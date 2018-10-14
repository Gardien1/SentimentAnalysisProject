package com.sentimentanalysis.usq.sentimentanalysis;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @Description: Manager of attaching on click handlers to buttons.
 * @author: Bryce Woods
 * @version: 1.0
 * @LastUpdated: 19/09/2018
 *
 */
public abstract class OnClickManager {


    /**
     *
     * @Description: Attaches onclick listeners on the main page.
     * @input1: AppCompactActivity - Context of the application.
     * @input2: ViewManager - View manager to change layout.
     * @input3: UIInflater - Used to dynamically load content on page.
     *
     * */
    public static void attachHandlersOnMain(final AppCompatActivity context , final ViewManager vManager)
    {
        Button settings_btn = (Button) context.findViewById(R.id.settings_btn);
        Button stats_btn = (Button) context.findViewById(R.id.graph_btn);

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vManager.changeToSettingsView(context);
            }
        });

        stats_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vManager.changeToGraphView(context);
            }
        });


    }

    /**
     *
     * @Description: Attaches onclick listeners on the setup page.
     * @input1: AppCompactActivity - Context of the application.
     * @input2: ViewManager - View manager to change layout.
     * @input3: DataAnalyser - Manager used to process any data on click.
     *
     * */
    public static void attachHandlersOnSetup(final AppCompatActivity context , final ViewManager vManager , final DataAnalyser pManager)
    {
        Button save_btn = (Button) context.findViewById(R.id.finishSetup_btn);
        Button addExtraHandles = (Button) context.findViewById(R.id.addHandle_btn);
        final LinearLayout twitterInput = (LinearLayout) context.findViewById(R.id.setup_data_ouput_layout);
        final LinearLayout output = (LinearLayout) context.findViewById(R.id.setup_data_ouput_layout);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Reset error divs.
                ArrayList<TextView> errorViews = ViewRetriever.getAllTextViews(output);

                for(TextView errorView : errorViews)
                {
                    errorView.setVisibility(View.INVISIBLE);
                }

                HashMap<String,String> settingsData = ViewDataGatherer.getSettingsData(twitterInput);

                pManager.processSettingsData(settingsData);
                //vManager.changeToMainView(context);
            }
        });

        addExtraHandles.setOnClickListener(new View.OnClickListener() {

            // Error id counter.
            int errorCounter = 0;
            @Override
            public void onClick(View view) {

                final EditText twitterInput = new EditText(context);
                final TextView errorText = new TextView(context);
                errorText.setText("Twitter account does not exist!");
                errorText.setVisibility(View.INVISIBLE);
                errorText.setTextColor(Color.rgb(255, 0, 0));
                errorText.setTag("errorElement-" + errorCounter);
                errorCounter++;

                twitterInput.setText("@example");
                twitterInput.setTextSize(23);
                twitterInput.setTextColor(Color.parseColor("#ffffff"));

                final Button removeBtn = new Button(context);
                removeBtn.setBackgroundResource(R.drawable.button_remove);
                removeBtn.setLayoutParams(new LinearLayout.LayoutParams(400,100));
                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        output.removeView(errorText);
                        output.removeView(twitterInput);
                        output.removeView(removeBtn);
                        errorCounter--;
                    }
                });

                output.addView(errorText);
                output.addView(twitterInput);
                output.addView(removeBtn);
            }
        });
    }

    /**
     *
     * @Description: Attaches onclick listeners on the settings page.
     * @input1: AppCompactActivity - Context of the application.
     * @input2: ViewManager - View manager to change layout.
     * @input3: DataAnalyser - Manager used to process any data on click.
     *
     * */
    public static void attachOnHandlersOnSettings(final AppCompatActivity context , final ViewManager vManager , final DataAnalyser pManager)
    {
        Button save_btn = (Button) context.findViewById(R.id.finishSetup_btn);
        Button addExtraHandles = (Button) context.findViewById(R.id.addHandle_btn);
        final LinearLayout twitterInput = (LinearLayout) context.findViewById(R.id.setup_data_ouput_layout);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,String> settingsData = ViewDataGatherer.getSettingsData(twitterInput);

                pManager.processSettingsData(settingsData);
                vManager.changeToMainView(context);

            }
        });

        addExtraHandles.setOnClickListener(new View.OnClickListener() {

            LinearLayout output = (LinearLayout) context.findViewById(R.id.setup_data_ouput_layout);

            @Override
            public void onClick(View view) {
                final EditText twitterInput = new EditText(context);

                twitterInput.setText("@example");
                twitterInput.setTextSize(23);
                twitterInput.setTextColor(Color.parseColor("#ffffff"));
                final Button remove_btn = new Button(context);
                remove_btn.setBackgroundResource(R.drawable.button_remove);
                remove_btn.setLayoutParams(new LinearLayout.LayoutParams(400,100));
                remove_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        output.removeView(twitterInput);
                        output.removeView(remove_btn);
                    }
                });

                output.addView(twitterInput);
                output.addView(remove_btn);
            }
        });
    }


    public static void attachHandlersOnGraphView(final AppCompatActivity context , final ViewManager vManager , final DataAnalyser pManager)
    {
        Button back_btn = (Button) context.findViewById(R.id.content_back);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vManager.changeToMainView(context);
            }
        });
    }

}
