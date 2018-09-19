package com.sentimentanalysis.usq.sentimentanalysis;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vManager.changeToSettingsView(context);
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

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> settingsData = ViewDataGatherer.getSettingsData(twitterInput);

                // TODO: Check and handle errors.

                pManager.processSettingsData(settingsData);
                vManager.changeToMainView(context);
            }
        });

        addExtraHandles.setOnClickListener(new View.OnClickListener() {

            LinearLayout output = (LinearLayout) context.findViewById(R.id.setup_data_ouput_layout);

            @Override
            public void onClick(View view) {
                EditText twitterInput = new EditText(context);

                twitterInput.setText("@example");

                output.addView(twitterInput);
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

                final Button remove_btn = new Button(context);
                remove_btn.setText("Remove");

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

}
