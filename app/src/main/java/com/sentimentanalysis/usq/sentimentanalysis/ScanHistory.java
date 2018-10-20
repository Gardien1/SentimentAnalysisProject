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
    ArrayList<Integer> daysScanned;
    ArrayList<Integer> scores;


    public ScanHistory()
    {
        lastScanTime = "";
        scanHistory = new HashMap<Date,Integer>();
        daysScanned = new ArrayList<Integer>();
        scores = new ArrayList<Integer>();
    }

    public ScanHistory(String scanTime)
    {
        lastScanTime = scanTime;
        scanHistory = new HashMap<Date,Integer>();
        daysScanned = new ArrayList<Integer>();
        scores = new ArrayList<Integer>();
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
    public void addToHistory(Integer scanDay , Integer score)
    {
        boolean foundInHistory = false;

        // Check for new month
        if(daysScanned.size() > 0)
        {
            if(scanDay < daysScanned.get(0))
            {
                Log.i("ADD TO HISTORY", "NEW MONTH: RESETTING DATA");
                resetData();
            }

            for(int i=0;i<daysScanned.size();i++)
            {
                if(scanDay == daysScanned.get(i))
                {
                    Log.i("SCAN HITORY" , "EXISITNG DAY: UPDATING");
                    scores.set(i , scores.get(i) + score);
                    foundInHistory = true;
                    break;
                }
            }
        }

        if(!foundInHistory)
        {
            Log.i("SCAN HISTORY" , "NEW DAY: ADDING");
            daysScanned.add(scanDay);
            scores.add(score);
        }
    }

    public HashMap<Date, Integer> getScanHistory() {
        return scanHistory;
    }

    public ArrayList<Integer> getScanDays()
    {
        return daysScanned;
    }

    public ArrayList<Integer> getScores()
    {
        return scores;
    }

    public void resetData()
    {
        scores = new ArrayList<Integer>();
        daysScanned = new ArrayList<Integer>();
    }

}
