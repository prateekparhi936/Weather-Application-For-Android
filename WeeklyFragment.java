package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;


public class WeeklyFragment extends Fragment {
    JSONObject dailyjsonobj=new JSONObject();
    JSONArray dailyweatherarr=new JSONArray();
    String summary="";
    String icon="";
    List<Double> mintemplist=new ArrayList<>();
    List<Double> maxtemplist=new ArrayList<>();
    XAxis xAxis;
    YAxis yAxis;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DetailTabs activity=(DetailTabs) getActivity();
        dailyjsonobj=activity.dailyjsonobj;

        try {
            icon=dailyjsonobj.getString("icon");
            summary=dailyjsonobj.getString("summary");
            JSONArray dailyweatherarr=dailyjsonobj.getJSONArray("data");
            for (int i = 0; i < dailyweatherarr.length(); i++) {
                JSONObject jsonobject = dailyweatherarr.getJSONObject(i);
                System.out.println(jsonobject.getString("temperatureLow")+" "+ jsonobject.getString("temperatureHigh"));
                mintemplist.add(Double.valueOf(jsonobject.getString("temperatureMax")));
                maxtemplist.add(Double.valueOf(jsonobject.getString("temperatureMin")));
            }
            out.println("Min temp list: "+mintemplist.toString());
            out.println("Max temp list: "+maxtemplist.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return inflater.inflate(R.layout.fragment_weekly, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){

        addData();
        addChart();
    }

    private void addChart() {
        LineChart mplinechart=(LineChart)getView().findViewById(R.id.weekly_linechart);
        LineDataSet lineDataSet1=new LineDataSet(minTemp(),"Minimum Temperature");
        LineDataSet lineDataSet2=new LineDataSet(maxTemp(),"Maximum Temperature");
        lineDataSet1.setFillAlpha(110);
        lineDataSet2.setFillAlpha(110);
        lineDataSet1.setValueTextColor(R.color.colorwhite);
        lineDataSet2.setValueTextColor(R.color.colorwhite);
        lineDataSet1.setColor(getResources().getColor(R.color.colorpurple));
        lineDataSet2.setColor(getResources().getColor(R.color.colororange));
        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);
        LineData data=new LineData(dataSets);
        mplinechart.setData(data);

        xAxis=mplinechart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(getResources().getColor(R.color.colorwhite));

        yAxis=mplinechart.getAxisLeft();
        yAxis.setTextSize(10f);
        yAxis.setTextColor(getResources().getColor(R.color.colorwhite));

        yAxis=mplinechart.getAxisRight();
        yAxis.setTextSize(10f);
        yAxis.setTextColor(getResources().getColor(R.color.colorwhite));

        mplinechart.getAxisLeft().setDrawGridLines(false);
        mplinechart.getXAxis().setDrawGridLines(false);
        mplinechart.getAxisRight().setDrawGridLines(false);

        Legend legend = mplinechart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setFormSize(14);
        legend.setTextColor(Color.WHITE);
        legend.setXEntrySpace(10f);
        legend.setTextSize(14);


        mplinechart.invalidate();

    }
    private ArrayList<Entry> maxTemp(){
        ArrayList<Entry> datavals=new ArrayList<>();
        datavals.add(new Entry(0,maxtemplist.get(0).floatValue()));
        datavals.add(new Entry(1,maxtemplist.get(1).floatValue()));
        datavals.add(new Entry(2,maxtemplist.get(2).floatValue()));
        datavals.add(new Entry(3,maxtemplist.get(3).floatValue()));
        datavals.add(new Entry(4,maxtemplist.get(4).floatValue()));
        datavals.add(new Entry(5,maxtemplist.get(5).floatValue()));
        datavals.add(new Entry(6,maxtemplist.get(6).floatValue()));
        datavals.add(new Entry(7,maxtemplist.get(7).floatValue()));
        return datavals;
    }
    private ArrayList<Entry> minTemp(){
        ArrayList<Entry> datavals=new ArrayList<>();
        datavals.add(new Entry(0,mintemplist.get(0).floatValue()));
        datavals.add(new Entry(1,mintemplist.get(1).floatValue()));
        datavals.add(new Entry(2,mintemplist.get(2).floatValue()));
        datavals.add(new Entry(3,mintemplist.get(3).floatValue()));
        datavals.add(new Entry(4,mintemplist.get(4).floatValue()));
        datavals.add(new Entry(5,mintemplist.get(5).floatValue()));
        datavals.add(new Entry(6,mintemplist.get(6).floatValue()));
        datavals.add(new Entry(7,mintemplist.get(7).floatValue()));
        return datavals;
    }

    private void addData() {
        if(summary!="") {
            TextView tv1 = (TextView) getView().findViewById(R.id.weekly_summtext);
            tv1.setText(summary);
        }
        if(icon!=""){
            ImageView  val_r11_icon=(ImageView)getView().findViewById(R.id.weekly_icon);
            if("clear-day".equals(icon)){
                val_r11_icon.setImageResource(R.drawable.weather_sunny);
            }
            if("clear-night".equals(icon)){
                val_r11_icon.setImageResource(R.drawable.weather_night);
            }
            if("rain".equals(icon)){
                val_r11_icon.setImageResource(R.drawable.weather_rainy);
            }
            if("sleet".equals(icon)){
                val_r11_icon.setImageResource(R.drawable.weather_snowy_rainy);
            }
            if("snow".equals(icon)){
                val_r11_icon.setImageResource(R.drawable.weather_snowy);
            }
            if("wind".equals(icon)){
                val_r11_icon.setImageResource(R.drawable.weather_windy_variant);
            }
            if("fog".equals(icon)){
                val_r11_icon.setImageResource(R.drawable.weather_fog);
            }
            if("cloudy".equals(icon)){
                val_r11_icon.setImageResource(R.drawable.weather_cloudy);
            }
            if("partly-cloudy-day".equals(icon)){
                val_r11_icon.setImageResource(R.drawable.weather_partly_cloudy);
            }
            if("partly-cloudy-night".equals(icon)){
                val_r11_icon.setImageResource(R.drawable.weather_night_partly_cloudy);
            }
        }
    }
}