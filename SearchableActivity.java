package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchableActivity extends AppCompatActivity {
    HashMap<String,String> mapadd=new HashMap<>();
    String lat="";
    String lng="";
    String regionName="";
    String fullplace="";
    Double temp=0.0;
    boolean isFav=false;
    int tem=-1;
    JSONArray dailyweatherarr=new JSONArray();
    JSONObject currentlyobj_searchable=new JSONObject();
    List<String> cities=new ArrayList<>();
    String city="";
    String status="";
    HashMap<String,String> ipjson_map=new HashMap<>();
    HashMap<String,String> summ_page_card1_data=new HashMap<>();
    HashMap<String, String> summ_page_card2_data=new HashMap<String, String>();
    HashMap<String,String> icons = new HashMap<>();
    List<List<String>> dailydatalist=new ArrayList<>();
    JSONObject dailyobj=new JSONObject();
    String weatherSearchQuery="";
    String searchQuery="";
    List<String> urlphotos=new ArrayList<>();
    ArrayList<String> ImgUrl= new ArrayList<>();
    SharedPreferences shared;
    ArrayList<String> arrPackage;
    HashSet<String> favcityset=new HashSet<>();
    boolean ispresent=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.getSharedPreferences("SearchableActivity", 0).edit().clear().apply();

        setContentView(R.layout.activity_searchable);
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar_summary);
        showviews();
        shared=getSharedPreferences("SearchableActivity",MODE_PRIVATE);

        if(toolbar==null){
            System.out.println("toolbar is null");
        }
        setSupportActionBar(toolbar);
        Intent intent=new Intent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        fullplace=bundle.getString("fulladdr");
        searchQuery = bundle.getString("searchQuery");
        String[] x=searchQuery.split(",");
        if(searchQuery!=null) {
            System.out.println("Inside Searchable Activity from MainActivity: Search this: " + searchQuery);
            getSupportActionBar().setTitle(fullplace.split(",")[0]+", "+fullplace.split(",")[1]+", "+"USA");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setResult(2,intent);

            List<String> arr=Arrays.asList(searchQuery.split(","));

            String ci="";
            String si="";
            String co="";
            if(arr.size()==3) {
                ci = arr.get(0).replaceAll("\\s", "");
                si = arr.get(1).replaceAll("\\s", "");
                co = arr.get(2).replaceAll("\\s", "");
            }
            else if(arr.size()==2){
                ci = arr.get(0).replaceAll("\\s", "");
                si = arr.get(1).replaceAll("\\s", "");
            }

            mapadd.put("city",ci);
            mapadd.put("state",si);
            mapadd.put("country","USA");

            getLatLongFromAddress(mapadd);
            onRestart();
        }
        ispresent=checkPrefs();
        if(ispresent){
            ImageView favicon=(ImageView)findViewById(R.id.addFavsea);
            favicon.setImageResource(R.drawable.map_marker_minus);
        }
        else{
            ImageView favicon=(ImageView)findViewById(R.id.addFavsea);
            favicon.setImageResource(R.drawable.map_marker_plus);
        }
    }

    private void getLatLongFromAddress(final HashMap<String, String> mapadd) {
        String url="http://prateekhw9usc.us-east-2.elasticbeanstalk.com/weathercoord?street="+mapadd.get("city")+"&city="+mapadd.get("state")+"&state="+mapadd.get("country");

//            String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+mapadd.get("city")+"&"+mapadd.get("state")+"&"+mapadd.get("country")+"&key=AIzaSyC2qDPaOb-pMuqlhNmMbDAz-FOLjZ55_ng";
            System.out.println(url);
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println("Weather Data in Searchable: "+response.toString());
                        JSONArray jsonArray=response.getJSONArray("results");
                        JSONObject innerObj = jsonArray.getJSONObject(0);
                        String state=innerObj.getJSONArray("address_components").getJSONObject(2).getString("long_name");
                        System.out.println("State: "+state);
                        JSONObject location=innerObj.getJSONObject("geometry").getJSONObject("location");
                        lat=location.getString("lat");
                        lng=location.getString("lng");
                        mapadd.put("lat",lat);
                        mapadd.put("lon",lng);
                        mapadd.put("state",state);

                        getWeatherData(mapadd);
                    }
                    catch (Exception ex){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            Volley.newRequestQueue(this).add(jsonRequest);
    }
//
    public void getWeatherData(HashMap<String,String> map){
        String lat="";
        String lon="";
        lat=map.get("lat");
        lon=map.get("lon");
        String url="http://prateekhw9usc.us-east-2.elasticbeanstalk.com"+"/weatherplace?lat="+lat+"&"+"lon="+lon;
//        System.out.println("URl : "+url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String weather_json_string=response.toString();
//                System.out.println("weather json string: "+weather_json_string);
                try {
                    JSONObject obj = new JSONObject(weather_json_string);
                    currentlyobj_searchable=obj.getJSONObject("currently");
                    dailyobj=obj.getJSONObject("daily");


                    String sum=currentlyobj_searchable.getString("icon");
                    String temp=currentlyobj_searchable.getString("temperature");
                    String city=mapadd.get("city");
                    String country=mapadd.get("country");
                    String region=mapadd.get("state");

//                    System.out.println(sum+""+temp+""+city+""+country+""+region);

                    summ_page_card1_data.put("icon",sum);
                    summ_page_card1_data.put("temp",temp);
                    summ_page_card1_data.put("city",city);
                    summ_page_card1_data.put("country",country);
                    summ_page_card1_data.put("region",region);


                    String humidity=currentlyobj_searchable.getString("humidity");
                    String windSpeed=currentlyobj_searchable.getString("windSpeed");
                    String visibility=currentlyobj_searchable.getString("visibility");
                    String pressure=currentlyobj_searchable.getString("pressure");

//                    System.out.println(humidity+""+windSpeed+""+visibility+""+pressure);

                    Double humid=Double.parseDouble(humidity);
                    int humidvalue = humid.intValue();
                    humidity=humid.toString();

                    summ_page_card2_data.put("humidity",humidity);
                    summ_page_card2_data.put("windspeed",windSpeed);
                    summ_page_card2_data.put("visibility",visibility);
                    summ_page_card2_data.put("pressure",pressure);

//                    for (HashMap.Entry<String,String> entry :  summ_page_card1_data.entrySet()) {
//                        String key = entry.getKey();
//                        String value = entry.getValue();
//                        System.out.println(key+" "+value);
//                    }

                    dailyweatherarr=dailyobj.getJSONArray("data");
                    for (int i = 0; i < dailyweatherarr.length(); i++) {
                        JSONObject jsonobject = dailyweatherarr.getJSONObject(i);
                        String time=jsonobject.getString("time");
                        long timestamp = Long.parseLong(time);
                        Date expiry = new Date(timestamp * 1000);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(expiry);
                        int year=calendar.get(Calendar.YEAR);
                        int day=calendar.get(Calendar.DATE);
                        int month=calendar.get(Calendar.MONTH);
                        String dateString=day+"/"+month+"/"+year;
                        String tempHigh=jsonobject.getString("temperatureHigh");
                        String tempLow=jsonobject.getString("temperatureLow");
                        String icon=jsonobject.getString("icon");
                        System.out.println(dateString+""+tempHigh+""+tempLow+""+icon);
                        List<String> list=new ArrayList<>();
                        list.add(dateString);
                        list.add(icon);
                        list.add(tempLow);
                        list.add(tempHigh);
                        dailydatalist.add(list);

                        if("rain".equals(icon)) {
                            Integer x=R.drawable.weather_rainy;
                            icons.put(icon,x.toString());
                        }
                        if("clear-day".equals(icon)){
                            Integer x=R.drawable.weather_sunny;
                            icons.put(icon,x.toString());
                        }
                        if("clear-night".equals(icon)){
                            Integer x=R.drawable.weather_night;
                            icons.put(icon,x.toString());
                        }
                        if("cloudy".equals(icon)){
                            Integer x=R.drawable.weather_cloudy;
                            icons.put(icon,x.toString());
                        }
                        if("partly-cloudy-day".equals(icon)){
                            Integer x=R.drawable.weather_partly_cloudy;
                            icons.put(icon,x.toString());
                        }
                        if("sleet".equals(icon)){
                            Integer x=R.drawable.weather_sunny;
                            icons.put(icon,x.toString());
                        }
                        if("snow".equals(icon)){
                            Integer x=R.drawable.weather_snowy;
                            icons.put(icon,x.toString());
                        }
                        if("wind".equals(icon)){
                            Integer x=R.drawable.weather_windy_variant;
                            icons.put(icon,x.toString());
                        }
                        if("fog".equals(icon)){
                            Integer x=R.drawable.weather_fog;
                            icons.put(icon,x.toString());
                        }
                        if("partly-cloudy-night".equals(icon)){
                            Integer x=R.drawable.weather_night_partly_cloudy;
                            icons.put(icon,x.toString());
                        }

                    }



                    addSummCard1(summ_page_card1_data);
                    addSummCard2(summ_page_card2_data);
                    addSummCard3(dailydatalist);
                    System.out.println("between sumcard3 and photos");
                    getPhotos();

                }
                catch (Exception ex){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
//
        Volley.newRequestQueue(this).add(jsonRequest);
    }

    private void getPhotos(){
        System.out.println("inside photos: "+summ_page_card1_data.get("city"));
        String url = "http://prateekhw9usc.us-east-2.elasticbeanstalk.com/customsearch?cityname="+summ_page_card1_data.get("city").replace(" ","");
        System.out.println("in Searchable tabs photo api URL "+url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray=response.getJSONArray("items");
                    System.out.println(jsonArray.length());
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String photolink=jsonObject.getString("link");
                        urlphotos.add(photolink);
                    }
                    System.out.println("links: "+urlphotos.toString());
                    ImgUrl.add(urlphotos.get(0));
                    ImgUrl.add(urlphotos.get(1));
                    ImgUrl.add(urlphotos.get(2));
                    ImgUrl.add(urlphotos.get(3));
                    ImgUrl.add(urlphotos.get(4));
                    ImgUrl.add(urlphotos.get(5));
                    ImgUrl.add(urlphotos.get(6));
                    ImgUrl.add(urlphotos.get(7));
                }
                catch (Exception ex){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchable_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



    public void addSummCard1(HashMap<String,String> mapcard1){
        //Get ID's
        System.out.println("inside card1");

        TextView card1_sea_temp =(TextView) findViewById(R.id.card1_sea_textview1);
        TextView card1_sea_summ=(TextView) findViewById(R.id.card1_sea_textview2);
        TextView card1_sea_loc =(TextView) findViewById(R.id.card1_sea_textview3);
        ImageView card1_sea_icon=(ImageView) findViewById(R.id.card1_sea_summicon);
        CardView cardView_sea_summ=(CardView) findViewById(R.id.card_view1_sea);

        //Set ID's;
        temp=Double.valueOf(mapcard1.get("temp"));
        tem=(int)Math.rint(temp);

        card1_sea_temp.setText(tem+"\u00B0"+" F");
        card1_sea_summ.setText(mapcard1.get("icon"));
        card1_sea_loc.setText(mapcard1.get("city")+", "+mapcard1.get("region")+", "+mapcard1.get("country"));

        String icon=mapcard1.get("icon");

        if("clear-day".equals(icon)){
            card1_sea_icon.setImageResource(R.drawable.weather_sunny);
        }
        if("clear-night".equals(icon)){
            card1_sea_icon.setImageResource(R.drawable.weather_night);
        }
        if("rain".equals(icon)){
            card1_sea_icon.setImageResource(R.drawable.weather_rainy);
        }
        if("sleet".equals(icon)){
            card1_sea_icon.setImageResource(R.drawable.weather_snowy_rainy);
        }
        if("snow".equals(icon)){
            card1_sea_icon.setImageResource(R.drawable.weather_snowy);
        }
        if("wind".equals(icon)){
            card1_sea_icon.setImageResource(R.drawable.weather_windy_variant);
        }
        if("fog".equals(icon)){
            card1_sea_icon.setImageResource(R.drawable.weather_fog);
        }
        if("cloudy".equals(icon)){
            card1_sea_icon.setImageResource(R.drawable.weather_cloudy);
        }
        if("partly-cloudy-day".equals(icon)){
            card1_sea_icon.setImageResource(R.drawable.weather_partly_cloudy);
        }
        if("partly-cloudy-night".equals(icon)){
            card1_sea_icon.setImageResource(R.drawable.weather_night_partly_cloudy);
        }

    }

    public void addSummCard2(HashMap<String,String> mapcard2) {
        System.out.println("insdie summ card2");

        //Get Id's
        ImageView card2_sea_r1_humidicon = (ImageView) findViewById(R.id.card2_sea_row1_humidicon);
        ImageView card2_sea_r1_windicon  = (ImageView) findViewById(R.id.card2_sea_row1_windspeedicon);
        ImageView card2_sea_r1_visibicon = (ImageView) findViewById(R.id.card2_sea_row1_visibilityicon);
        ImageView card2_sea_r1_pressicon = (ImageView) findViewById(R.id.card2_sea_row1_pressureicon);

        TextView card2_sea_r2_humidval = (TextView) findViewById(R.id.card2_sea_row2_txt1);
        TextView card2_sea_r2_windval  = (TextView) findViewById(R.id.card2_sea_row2_txt2);
        TextView card2_sea_r2_visibval = (TextView) findViewById(R.id.card2_sea_row2_txt3);
        TextView card2_sea_r2_pressureval = (TextView) findViewById(R.id.card2_sea_row2_txt4);

        TextView card2_sea_r3_Humidity = (TextView) findViewById(R.id.card2_sea_row3_txt1);
        TextView card2_sea_r3_WindSpeed  = (TextView) findViewById(R.id.card2_sea_row3_txt2);
        TextView card2_sea_r3_Visibility = (TextView) findViewById(R.id.card2_sea_row3_txt3);
        TextView card2_sea_r3_Pressure = (TextView) findViewById(R.id.card2_sea_row3_txt4);


        double x=Double.valueOf(mapcard2.get("humidity"));
        x=x*100;
        int humidpercent=(int)x;
        String humidpercentstr=humidpercent+" %";


        //Set Id's
        DecimalFormat df2 = new DecimalFormat("#.##");
        String visib=df2.format(Double.valueOf(mapcard2.get("visibility")));
        String press=df2.format(Double.valueOf(mapcard2.get("pressure")));
        String wind=df2.format(Double.valueOf(mapcard2.get("windspeed")));

        card2_sea_r1_humidicon.setImageResource(R.drawable.water_percent);
        card2_sea_r1_windicon.setImageResource(R.drawable.weather_windy);
        card2_sea_r1_visibicon.setImageResource(R.drawable.eye_outline);
        card2_sea_r1_pressicon.setImageResource(R.drawable.gauge);

        card2_sea_r2_humidval.setText(humidpercentstr);
        card2_sea_r2_pressureval.setText(press+" mb");
        card2_sea_r2_visibval.setText(visib+" km");
        card2_sea_r2_windval.setText(wind+" mph");

        card2_sea_r3_Pressure.setText("Pressure");
        card2_sea_r3_WindSpeed.setText("Wind Speed");
        card2_sea_r3_Visibility.setText("Visibility");
        card2_sea_r3_Humidity.setText("Humidity");

    }

    public void addSummCard3(List<List<String>> dailydatalist){
        System.out.println("Inside add summ card3");
//        for(List<String> list:dailydatalist){
//            System.out.println(list.get(0)+" "+icons.get(list.get(1))+" "+Math.round(Double.parseDouble(list.get(2)))+" "+Math.round(Double.parseDouble(list.get(3))));
//        }
//        for(String val: icons.values()){
//            System.out.print(val+" ");
//        }

        TextView card3_sea_r1_txt1 = (TextView) findViewById(R.id.card3_sea_r1_text1);
        TextView card3_sea_r1_txt2 = (TextView) findViewById(R.id.card3_sea_r1_text2);
        TextView card3_sea_r1_txt3 = (TextView) findViewById(R.id.card3_sea_r1_text3);
        ImageView card3_sea_r1_icon = (ImageView) findViewById(R.id.card3_sea_r1_icon);

        card3_sea_r1_txt1.setText(dailydatalist.get(0).get(0));
        card3_sea_r1_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(0).get(1))));
        card3_sea_r1_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(0).get(2)))));
        card3_sea_r1_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(0).get(3)))));

        TextView card3_sea_r2_txt1 = (TextView) findViewById(R.id.card3_sea_r2_text1);
        TextView card3_sea_r2_txt2 = (TextView) findViewById(R.id.card3_sea_r2_text2);
        TextView card3_sea_r2_txt3 = (TextView) findViewById(R.id.card3_sea_r2_text3);
        ImageView card3_sea_r2_icon = (ImageView) findViewById(R.id.card3_sea_r2_icon);

        card3_sea_r2_txt1.setText(dailydatalist.get(1).get(0));
        card3_sea_r2_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(1).get(1))));
        card3_sea_r2_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(1).get(2)))));
        card3_sea_r2_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(1).get(3)))));


        TextView card3_sea_r3_txt1 = (TextView) findViewById(R.id.card3_sea_r3_text1);
        TextView card3_sea_r3_txt2 = (TextView) findViewById(R.id.card3_sea_r3_text2);
        TextView card3_sea_r3_txt3 = (TextView) findViewById(R.id.card3_sea_r3_text3);
        ImageView card3_sea_r3_icon = (ImageView) findViewById(R.id.card3_sea_r3_icon);

        card3_sea_r3_txt1.setText(dailydatalist.get(2).get(0));
        card3_sea_r3_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(2).get(1))));
        card3_sea_r3_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(2).get(2)))));
        card3_sea_r3_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(2).get(3)))));


        TextView card3_sea_r4_txt1 = (TextView) findViewById(R.id.card3_sea_r4_text1);
        TextView card3_sea_r4_txt2 = (TextView) findViewById(R.id.card3_sea_r4_text2);
        TextView card3_sea_r4_txt3 = (TextView) findViewById(R.id.card3_sea_r4_text3);
        ImageView card3_sea_r4_icon = (ImageView) findViewById(R.id.card3_sea_r4_icon);

        card3_sea_r4_txt1.setText(dailydatalist.get(3).get(0));
        card3_sea_r4_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(3).get(1))));
        card3_sea_r4_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(3).get(2)))));
        card3_sea_r4_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(3).get(3)))));

        TextView card3_sea_r5_txt1 = (TextView) findViewById(R.id.card3_sea_r5_text1);
        TextView card3_sea_r5_txt2 = (TextView) findViewById(R.id.card3_sea_r5_text2);
        TextView card3_sea_r5_txt3 = (TextView) findViewById(R.id.card3_sea_r5_text3);
        ImageView card3_sea_r5_icon = (ImageView) findViewById(R.id.card3_sea_r5_icon);

        card3_sea_r5_txt1.setText(dailydatalist.get(4).get(0));
        card3_sea_r5_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(4).get(1))));
        card3_sea_r5_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(4).get(2)))));
        card3_sea_r5_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(4).get(3)))));

        TextView card3_sea_r6_txt1 = (TextView) findViewById(R.id.card3_sea_r6_text1);
        TextView card3_sea_r6_txt2 = (TextView) findViewById(R.id.card3_sea_r6_text2);
        TextView card3_sea_r6_txt3 = (TextView) findViewById(R.id.card3_sea_r6_text3);
        ImageView card3_sea_r6_icon = (ImageView) findViewById(R.id.card3_sea_r6_icon);

        card3_sea_r6_txt1.setText(dailydatalist.get(5).get(0));
        card3_sea_r6_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(5).get(1))));
        card3_sea_r6_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(5).get(2)))));
        card3_sea_r6_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(5).get(3)))));

        TextView card3_sea_r7_txt1 = (TextView) findViewById(R.id.card3_sea_r7_text1);
        TextView card3_sea_r7_txt2 = (TextView) findViewById(R.id.card3_sea_r7_text2);
        TextView card3_sea_r7_txt3 = (TextView) findViewById(R.id.card3_sea_r7_text3);
        ImageView card3_sea_r7_icon = (ImageView) findViewById(R.id.card3_sea_r7_icon);

        card3_sea_r7_txt1.setText(dailydatalist.get(6).get(0));
        card3_sea_r7_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(6).get(1))));
        card3_sea_r7_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(6).get(2)))));
        card3_sea_r7_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(6).get(3)))));

        TextView card3_sea_r8_txt1 = (TextView) findViewById(R.id.card3_sea_r8_text1);
        TextView card3_sea_r8_txt2 = (TextView) findViewById(R.id.card3_sea_r8_text2);
        TextView card3_sea_r8_txt3 = (TextView) findViewById(R.id.card3_sea_r8_text3);
        ImageView card3_sea_r8_icon = (ImageView) findViewById(R.id.card3_sea_r8_icon);

        card3_sea_r8_txt1.setText(dailydatalist.get(7).get(0));
        card3_sea_r8_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(7).get(1))));
        card3_sea_r8_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(7).get(2)))));
        card3_sea_r8_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(7).get(3)))));
    }

    public void startSummaryActivity(View view){
        hideviews();
        String weatherSearchQuery1="";
        if(weatherSearchQuery1==""){
            String splitAddr[]=searchQuery.split(", ");
            System.out.println("Inside startSummaryActivity func in searchable activity "+splitAddr.toString());
            weatherSearchQuery1=splitAddr[1]+", United States";
        }
        Intent intent = new Intent(SearchableActivity.this,DetailTabs.class);
        intent.putExtra("searchQuery",weatherSearchQuery1);
        intent.putExtra("citynametweetsearch",mapadd.get("city")+", "+mapadd.get("state")+", USA");
        intent.putExtra("temptweet",tem+"\u00B0"+" F");
        intent.putExtra("currentlyJSONString", currentlyobj_searchable.toString());
        intent.putExtra("dailyweatherjson",dailyobj.toString());
        intent.putStringArrayListExtra("ImgUrl",ImgUrl);
        startActivityForResult(intent, 3);// Activity is started with requestCode 2
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==1){
            System.out.println("Detail Tabs Activity ended..back to Searchable activity" +requestCode);
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        ispresent=checkPrefs();
        showviews();
        if(ispresent){
            ImageView favicon=(ImageView)findViewById(R.id.addFavsea);
            favicon.setImageResource(R.drawable.map_marker_minus);
        }
        else{
            ImageView favicon=(ImageView)findViewById(R.id.addFavsea);
            favicon.setImageResource(R.drawable.map_marker_plus);
        }

        System.out.println("showing views in restart");
    }

    @Override
    public void onStart(){
        super.onStart();
        ispresent=checkPrefs();
        showviews();
        if(ispresent){
            ImageView favicon=(ImageView)findViewById(R.id.addFavsea);
            favicon.setImageResource(R.drawable.map_marker_minus);
        }
        else{
            ImageView favicon=(ImageView)findViewById(R.id.addFavsea);
            favicon.setImageResource(R.drawable.map_marker_plus);
        }

        System.out.println("showing views in restart");
    }



    private boolean checkPrefs() {
        System.out.println("in check prefs  for"+fullplace);
        Set<String> set = shared.getStringSet("FAV_LIST", null);
        if(set==null){
            System.out.println("set is null plus sign");
            return false;
        }
        if(set.contains(fullplace)){
            System.out.println("set has city already minus sign");
            return true;
        }
        System.out.println("set does has city already plus sign");
        return false;
    }

    public void hideviews(){
        Toolbar tool=(Toolbar)findViewById(R.id.app_bar_summary);
        ProgressBar bar1=(ProgressBar)findViewById(R.id.progressBar1_sea);
        CardView cv1=(CardView)findViewById(R.id.card_view1_sea);
        CardView cv2=(CardView)findViewById(R.id.card_sea_view2_summ);
        ScrollView sv=(ScrollView)findViewById(R.id.scroll_sea);
        TextView tv=(TextView)findViewById(R.id.tv_below_prog_sea);
        TextView tv1=(TextView)findViewById(R.id.searchrestext);
        cv1.setVisibility(View.GONE);
        sv.setVisibility(View.GONE);
        tool.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        bar1.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
        tv1.setVisibility(View.GONE);
    }

    public void showviews(){
        Toolbar tool=(Toolbar)findViewById(R.id.app_bar_summary);
        ProgressBar bar1=(ProgressBar)findViewById(R.id.progressBar1_sea);
        CardView cv1=(CardView)findViewById(R.id.card_view1_sea);
        CardView cv2=(CardView)findViewById(R.id.card_sea_view2_summ);
        ScrollView sv=(ScrollView)findViewById(R.id.scroll_sea);
        TextView tv=(TextView)findViewById(R.id.tv_below_prog_sea);
        TextView tv1=(TextView)findViewById(R.id.searchrestext);
        cv1.setVisibility(View.VISIBLE);
        sv.setVisibility(View.VISIBLE);
        tool.setVisibility(View.VISIBLE);
        cv2.setVisibility(View.VISIBLE);
        bar1.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        tv1.setVisibility(View.VISIBLE);
    }



    public void Favfunc(View view) {
        ImageView favicon=(ImageView)findViewById(R.id.addFavsea);
        boolean added=addtopreferences(fullplace);

        if(added){
            System.out.println("was not added before so added this time");
            favicon.setImageResource(R.drawable.map_marker_minus);
            String text=mapadd.get("city")+", "+mapadd.get("state")+", "+mapadd.get("country")+" was added to favourites";
            Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
        }
        else{
            System.out.println("was added before so removed this time");
            favicon.setImageResource(R.drawable.map_marker_plus);
            String text=mapadd.get("city")+", "+mapadd.get("state")+", "+mapadd.get("country")+" was removed from favourites";
            Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean addtopreferences(String fullplace) {
        Set<String> set = shared.getStringSet("FAV_LIST", null);
        //key not present
        if(set==null){
            SharedPreferences.Editor editor = shared.edit();
            editor.putStringSet("FAV_LIST", new HashSet<String>());
            editor.apply();
            set = shared.getStringSet("FAV_LIST", null);
            System.out.println("set: "+set+" city "+fullplace);
            set.add(fullplace);
            System.out.println("fav data: "+set.toString());
            return true;
        }
        System.out.println("fav data: "+set.toString());
        //set had  city
        if(set.contains(fullplace)){
            set.remove(fullplace);
            return false;
        }

        //set did not had  city
        set.add(fullplace);
        System.out.println("fav data: "+set.toString());
        return true;
    }
}
