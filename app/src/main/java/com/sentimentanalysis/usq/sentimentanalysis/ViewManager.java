package com.sentimentanalysis.usq.sentimentanalysis;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
*
* @Description: View manager that changes the layout of the input context
* @author  Bryce Woods
* @version 1.0
* @LastUpdated: 19/09/2018
*
*/
public class ViewManager {


    private EventListener broadcast;


    public ViewManager(EventListener inputBroadcast)
    {
        broadcast = inputBroadcast;
    }

    /**
    *
    * @Description: Event listener interface that is used to update any processing that
    * must take place after a particular view is loaded.
    *
    * */
    public interface EventListener{
        void  onMainViewLoaded();
        void onSettingsViewLoaded();
        void onSetupViewLoaded();
    }

    /**
     *
     * @Description: Changes context view to content_setup. Same layout but is handled differently.
     * @input: AppCompactActivity - context of the application.
     *
     * */
    public void changeToSetupView(AppCompatActivity viewContext)
    {
        viewContext.setContentView(R.layout.content_setup);
        broadcast.onSetupViewLoaded();
    }


    /**
     *
     * @Description: Changes context view to content_setup.
     * @input: AppCompactActivity - context of the application.
     *
     * */
    public void changeToSettingsView(AppCompatActivity viewContext)
    {
        viewContext.setContentView(R.layout.content_setup);
        broadcast.onSettingsViewLoaded();
    }

    /**
     *
     * @Description: Changes context view to content_main.
     * @input: AppCompactActivity - context of the application.
     *
     * */
    public void changeToMainView(AppCompatActivity viewContext)
    {
        viewContext.setContentView(R.layout.content_main);
        broadcast.onMainViewLoaded();
    }
}
