package com.sentimentanalysis.usq.sentimentanalysis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @Description: Returns the current time in a particular format.
 * @author  Bryce Woods
 * @version 1.0
 * @LastUpdated: 19/09/2018
 *
 */
public class ScanTime {

    private DateFormat formatter;

    public ScanTime()
    {
        formatter = new SimpleDateFormat("hh::mm:ss dd-mm-yyyy");
    }


    public ScanTime(String format)
    {
        formatter = new SimpleDateFormat(format);
    }


    public void setFormat(String format)
    {
        formatter = new SimpleDateFormat(format);
    }


    /**
     *
     * @Description: Gets the current time according to the format.
     * @return: String - Formatted time.
     *
     * */
    public String getCurrentTime()
    {
        Calendar time = Calendar.getInstance();
        return formatter.format(time.getTime());
    }


}
