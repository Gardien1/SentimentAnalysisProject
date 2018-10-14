package com.sentimentanalysis.usq.sentimentanalysis;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ScanHistory implements Serializable{

    String lastScanTime;
    HashMap<Date,Integer> scanHistory;


    public ScanHistory()
    {
        lastScanTime = "";
        scanHistory = new HashMap<Date,Integer>();
    }

    public ScanHistory(String scanTime)
    {
        lastScanTime = scanTime;
        scanHistory = new HashMap<Date,Integer>();
    }

    public void setLastScanTime(String scanTime)
    {
        lastScanTime = scanTime;
    }

    public String getLastScanTime()
    {
        return lastScanTime;
    }

    public String getLastScanTimeFormatted()
    {
        return "Last Scanned: " + lastScanTime;
    }

    //
    public void addToHistory(Date scanTime , Integer score)
    {
        boolean foundInHistory = false;

        for(Date key : scanHistory.keySet())
        {
            if(key.getMonth() == scanTime.getMonth() && key.getDay() == scanTime.getDay())
            {
                Integer updateScore = scanHistory.get(key) + score;
                scanHistory.put(key , updateScore);
                foundInHistory = true;
                Log.i("ADD TO HISTORY" , "FOUND IN HISTORY: UPDATING!");
                break;
            }
        }

        if(!foundInHistory)
        {
            scanHistory.put(scanTime,score);
            Log.i("ADD TO HISTORY" , "NOT FOUND IN HISTORY ADDING NEW");
        }
    }

    public HashMap<Date, Integer> getScanHistory() {
        return scanHistory;
    }

}
