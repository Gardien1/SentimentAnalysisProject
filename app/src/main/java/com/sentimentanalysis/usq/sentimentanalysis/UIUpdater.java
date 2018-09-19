package com.sentimentanalysis.usq.sentimentanalysis;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 *
 * @Description: UI manager that specifically updates certain views.
 * @author  Bryce Woods
 * @version 1.0
 * @LastUpdated: 19/09/2018
 *
 */
public abstract class UIUpdater {


    /**
     *
     * @Description: Updates the scan time on the main page.
     * @input1: AppCompatActivity - View context of the application.
     * @input2: String - Desired text to update textview.
     *
     * */
    public static void UpdateMainScanTime(AppCompatActivity context , String text)
    {
        TextView updateText = (TextView) context.findViewById(R.id.last_updated_txt);

        // Ensure element exists.
        if(updateText != null)
        {
            updateText.setText(text);

            if(updateText.getVisibility() == View.INVISIBLE)
            {
                updateText.setVisibility(View.VISIBLE);
            }
        }

    }

}
