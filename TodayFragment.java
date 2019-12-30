package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

public class TodayFragment extends Fragment {
    String pressure="";
    String humidity="";
    String icon="";
    String temperature="";
    String visibility="";
    String ozone="";
    String windSpeed="";
    String precipitation="";
    String cloudCover="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DetailTabs activity=(DetailTabs) getActivity();
        JSONObject jsonObject=activity.jsonObjcurrently;
        System.out.println("Inside today fragment: "+jsonObject.toString());
        try {
            if(jsonObject.has("pressure")) {
                pressure = jsonObject.getString("pressure");
            }
            else{
                pressure="0";
            }
            if(jsonObject.has("humidity")) {
                humidity = jsonObject.getString("humidity");
            }
            else{
                humidity="0";
            }
            if(jsonObject.has("windSpeed")) {
                windSpeed = jsonObject.getString("windSpeed");
            }
            else{
                windSpeed="0";
            }
            if(jsonObject.has("temperature")) {
                temperature = jsonObject.getString("temperature");
            }
            else{
                temperature="0";
            }
            if(jsonObject.has("visibility")) {
                visibility = jsonObject.getString("visibility");
            }
            else{
                visibility="0";
            }
            if(jsonObject.has("ozone")) {
                ozone = jsonObject.getString("ozone");
            }
            else{
                ozone="0";
            }

            if(jsonObject.has("precipIntensity")) {
                precipitation = jsonObject.getString("precipIntensity");
            }
            else{
                precipitation="0";
            }
            if(jsonObject.has("icon")) {
               icon = jsonObject.getString("icon");
            }
            else{
                icon="";
            }
            if(jsonObject.has("cloudCover")) {
                cloudCover = jsonObject.getString("cloudCover");
            }
            else{
                cloudCover="0";
            }

            System.out.println(pressure+","+humidity+","+windSpeed+","+temperature+","+visibility+","+ozone+","+precipitation+","+icon+","+cloudCover);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        double dh = Double.parseDouble(humidity); //%
        double dcc = Double.parseDouble(cloudCover);  //%

        double dw = Double.parseDouble(windSpeed);
        double doz = Double.parseDouble(ozone);
        double dpres = Double.parseDouble(pressure);
        double dprec = Double.parseDouble(precipitation);
        double dvisi=Double.parseDouble(visibility);

        dw=dw*100;
        int humidpercent=(int)dw;
        String humidpercentstr=humidpercent+" %";

        dcc=dcc*100;
        int cloudcoverpercent=(int)dcc;
        String cloudcoverpercentstr=cloudcoverpercent+" %";


        Double temp=Double.valueOf(temperature);
        int tem=(int)Math.rint(temp);
        String tempstr=tem+"\u00B0"+" F";


        String ozonestr = String.format("%.02f", doz);
        String precipstr = String.format("%.02f", dprec);
        String pressurestr = String.format("%.02f", dpres);
        String windstr = String.format("%.02f", dw);
        String visibstr = String.format("%.02f", dvisi);


        TextView val_r00_t1=(TextView)getView().findViewById(R.id.today_r00_text1);
        val_r00_t1.setText(windstr+" mph");

        TextView val_r01_t1=(TextView)getView().findViewById(R.id.today_r01_text1);
        val_r01_t1.setText(pressurestr+" mb");

        TextView val_r02_t1=(TextView)getView().findViewById(R.id.today_r02_text1);
        val_r02_t1.setText(precipstr+" mph");


        TextView val_r10_t1=(TextView)getView().findViewById(R.id.today_r10_text1);
        val_r10_t1.setText(tempstr);

        TextView val_r11_t1=(TextView)getView().findViewById(R.id.today_r11_text1);
        ImageView val_r11_icon=(ImageView) getView().findViewById(R.id.today_r11_icon);

        //change color of icon in "icon" card to yellow

        if("clear-day".equals(icon)){
            val_r11_icon.setImageResource(R.drawable.weather_sunny);
            val_r11_t1.setText("clear day");
        }
        if("clear-night".equals(icon)){
            val_r11_icon.setImageResource(R.drawable.weather_night);
            val_r11_t1.setText("clear night");
        }
        if("rain".equals(icon)){
            val_r11_icon.setImageResource(R.drawable.weather_rainy);
            val_r11_t1.setText("rain");
        }
        if("sleet".equals(icon)){
            val_r11_icon.setImageResource(R.drawable.weather_snowy_rainy);
            val_r11_t1.setText("sleet");
        }
        if("snow".equals(icon)){
            val_r11_icon.setImageResource(R.drawable.weather_snowy);
            val_r11_t1.setText("snow");
        }
        if("wind".equals(icon)){
            val_r11_icon.setImageResource(R.drawable.weather_windy_variant);
            val_r11_t1.setText("wind");
        }
        if("fog".equals(icon)){
            val_r11_icon.setImageResource(R.drawable.weather_fog);
            val_r11_t1.setText("fog");
        }
        if("cloudy".equals(icon)){
            val_r11_icon.setImageResource(R.drawable.weather_cloudy);
            val_r11_t1.setText("cloudy");
        }
        if("partly-cloudy-day".equals(icon)){
            val_r11_icon.setImageResource(R.drawable.weather_partly_cloudy);
            val_r11_t1.setText("cloudy day");
        }
        if("partly-cloudy-night".equals(icon)){
            val_r11_icon.setImageResource(R.drawable.weather_night_partly_cloudy);
            val_r11_t1.setText("cloudy day");
        }

        TextView val_r12_t1=(TextView)getView().findViewById(R.id.today_r12_text1);
        val_r12_t1.setText(humidpercentstr);

        TextView val_r20_t1=(TextView)getView().findViewById(R.id.today_r20_text1);
        val_r20_t1.setText(visibstr+" km");


        TextView val_r21_t1=(TextView)getView().findViewById(R.id.today_r21_text1);
        val_r21_t1.setText(cloudcoverpercentstr);

        TextView val_r22_t1=(TextView)getView().findViewById(R.id.today_r22_text1);
        val_r22_t1.setText(ozonestr+" DU");


    }


//    Notes:
//            • For the “icon” card:
//    o Based on the “icon” property of the “currently” json.
//    o For the text below icon, replace “- “s with space and display the icon.
//    o If the icon is “partly-cloudy-day” or “partly-cloudy-night” display only “cloudy day”
//    and “cloudy night” correspondingly.
//    o All possible values of icon can be found here:
//    https://darksky.net/dev/docs#response-format
//    o See hints for how to manipulate strings.
//            • Handle any missing values, otherwise the app can crash.
//            • Make sure all floating values are exactly 2 digits after the decimal point. Temperature
//    must be rounded off to the nearest integer. See hints
//• If a value is not present/malformed you can choose to display 0 or NA
//• Make sure that the cards are well-styled and have appropriate sizes with centered
//    images/texts
}