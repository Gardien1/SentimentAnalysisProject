package com.sentimentanalysis.usq.sentimentanalysis;

import java.util.HashMap;

/**
 *
 * @Description: Generic interface that handles processing of data on various views.
 * @author: Bryce Woods
 * @version: 1.0
 * @LastUpdated: 18/09/2018
 *
 */
public interface DataAnalyser {

    /**
     *
     * @Description: Process data on settings page.
     * @input: HashMap - Settings data.
     *
     * */
    public void processSettingsData(HashMap<String,String> settingsData);


}
