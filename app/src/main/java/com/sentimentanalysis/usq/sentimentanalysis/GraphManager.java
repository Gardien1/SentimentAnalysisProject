package com.sentimentanalysis.usq.sentimentanalysis;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GraphManager {

    private GraphView graph;

    public GraphManager(GraphView graph)
    {

    }

    public GraphManager(AppCompatActivity context)
    {
        graph = new GraphView(context);
    }

    public GraphView getGraph() {
        return graph;
    }

    public void setGraph(GraphView inputGraph)
    {
        graph = inputGraph;
    }


    public void updateGraph(HashMap<Date,Integer> data, AppCompatActivity context)
    {

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
        Date minDate = new Date();
        Date maxDate = new Date();
        int sizeCount = 0;
        Log.i("GraphManager" , "Update graph called: " + data.size());
        for(Date key : data.keySet())
        {
            Log.i("Graph Manager","ADDED TO SCAN HISTORY: " + data.get(key).toString());
            if(sizeCount == data.keySet().size())
            {
                maxDate = key;
            }
            else if(sizeCount == 0)
            {
                minDate = key;
            }

            series.appendData(new DataPoint(key,data.get(key)),true,10);
            sizeCount++;
        }

        graph.addSeries(series);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));
        graph.getGridLabelRenderer().setNumHorizontalLabels(data.size());

        graph.getViewport().setMinX(minDate.getTime());
        graph.getViewport().setMaxX(maxDate.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);
    }

}
