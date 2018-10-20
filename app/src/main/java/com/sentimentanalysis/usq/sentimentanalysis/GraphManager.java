package com.sentimentanalysis.usq.sentimentanalysis;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
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


    public void updateGraph(ArrayList<Integer> x_axis, ArrayList<Integer> y_axis, AppCompatActivity context)
    {
        try
        {
            LineGraphSeries series = new LineGraphSeries();

            Log.i("X_AXIS",x_axis.toString());
            Log.i("Y_AXIS",y_axis.toString());

            for(int i = 0; i<x_axis.size();i++)
            {
                series.appendData(new DataPoint(x_axis.get(i),y_axis.get(i)),true,100);
            }

            graph.addSeries(series);

            graph.getGridLabelRenderer().setNumHorizontalLabels(x_axis.size());

            //graph.getViewport().setMinX(d1.getTime());
            //graph.getViewport().setMaxX(d2.getTime());
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getGridLabelRenderer().setHumanRounding(false);
        }
        catch(Exception e)
        {
            Log.e("GRAPH ERROR", e.toString());
        }

    }

}
