package com.sentimentanalysis.usq.sentimentanalysis;

/**
 *
 * @Description: Generic interface that dynamically adds content to layouts.
 * @author: Bryce Woods
 * @version: 1.0
 * @LastUpdated: 18/09/2018
 *
 */
public interface UIInflater {
    /**
     *
     * @Description: Generic function to dynamically load content on the settings page.
     *
     * */
    void inflateSettingsPage();

    /**
     *
     * @Description: Generic function to dynamically load content on the setup page;
     *
     * */
    void inflateSetupPage();

    /**
     *
     * @Description: Generic function to dynamically load content on the main page;
     *
     * */
    void inflateMainPage();

}
