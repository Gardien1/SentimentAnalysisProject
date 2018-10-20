package com.sentimentanalysis.usq.sentimentanalysis;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


/**
 *
 * @Description: Sentiment Analysis -  Detects depression in tweet feeds.
 * @author  Ben,Rob,Steph and Bryce
 * @version 1.0
 * @LastUpdated: 19/09/2018
 *
 */
public class MainActivity extends AppCompatActivity {

    // Main data/backend manager.
    SentimentAnalysisManager manager;
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Emulate splash screen
        // Will need to rework
        setContentView(R.layout.content_splash_screen);

        createNotification();
        manager = new SentimentAnalysisManager(this,this.getBaseContext().getAssets(), notificationManager);

        Thread timer = new Thread(){
            public void run()
            {
                try
                {
                    sleep(4000);
                }
                catch(Exception e)
                {

                }
                finally
                {
                    manager.start();
                }
            }
        };
        timer.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(26)
    public void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel negativeTrend = new NotificationChannel("NT",
                    "negativeTrend", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(negativeTrend);
        } else notificationManager = getSystemService(NotificationManager.class);
    }
}
