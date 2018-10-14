package com.sentimentanalysis.usq.sentimentanalysis;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class ViewRetriever {

    public static ArrayList<TextView> getAllTextViews(LinearLayout layout)
    {
        ArrayList<TextView> textViews = new ArrayList<TextView>();
        for(int i=0; i < layout.getChildCount(); i++)
        {
            if(layout.getChildAt(i).getClass() == TextView.class)
            {
                textViews.add((TextView) layout.getChildAt(i));
            }
        }

        return textViews;

    }

}
