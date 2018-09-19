package com.sentimentanalysis.usq.sentimentanalysis;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;

/**
 *
 * @Description: Gathers data from various views.
 * @author: Bryce Woods
 * @version: 1.0
 * @LastUpdated: 19/09/2018
 *
 */
public abstract class ViewDataGatherer {


    /**
     *
     * @Description: Retrieves data from the settings page
     * @input1: LinearLayout - Layout that holds the edit texts.
     *
     * */
    public static HashMap<String,String> getSettingsData(LinearLayout settingsOutput)
    {
        HashMap<String,String> settingsData = new HashMap<String,String>();

        for(int i =0; i<settingsOutput.getChildCount(); i++)
        {
            if(settingsOutput.getChildAt(i).getClass() == EditText.class)
            {
                EditText tmpChild = (EditText) settingsOutput.getChildAt(i);
                settingsData.put(Integer.toString(i) , tmpChild.getText().toString());
            }
        }

        return settingsData;
    }


}
