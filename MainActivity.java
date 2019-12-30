package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import org.json.JSONArray;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.AutocompleteFilter;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;


import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


public class MainActivity extends AppCompatActivity {
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    boolean isFav=false;
    SharedPreferences shared;
    String regionName="";
    String city="";
    String status="";
    String lat="";
    String lon="";
    int tem=-1;
    String fullAddr="";
    JSONArray dailyweatherarr=new JSONArray();
    JSONObject currentlyobj_main=new JSONObject();
    JSONObject dailyobj=new JSONObject();
    String weatherSearchQuery="";
    HashMap<String,String> ipjson_map=new HashMap<>();
    HashMap<String,String> summ_page_card1_data=new HashMap<>();
    HashMap<String, String> summ_page_card2_data=new HashMap<String, String>();
    HashMap<String,String> icons = new HashMap<>();
    List<String> cities=new ArrayList<>();
    List<List<String>> dailydatalist=new ArrayList<>();
    List<String> urlphotos=new ArrayList<>();
    ArrayList<String> ImgUrl= new ArrayList<>();
    boolean ispresent=false;
    Double temp=0.0;
    String url = "http://ip-api.com/json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showviews();
        Places.initialize(getApplicationContext(),"AIzaSyBLlKVQiUlYIsh2gi7CS4Xox4VofJxtoOI");
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar_summary);
        shared=getSharedPreferences("SearchableActivity",MODE_PRIVATE);
        Set<String> set = shared.getStringSet("FAV_LIST", null);
        System.out.println(set.toString());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Weather App");
        getIPAPI();
        System.out.println("ip api ended");
        System.out.println(ipjson_map.get("city"));
    }

    public void getIPAPI(){
        String url = "http://ip-api.com/json";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String ip_json_string=response.toString();
                try {
                    JSONObject obj = new JSONObject(ip_json_string);
                    JSONObject  obj2=new JSONObject(ip_json_string);
                    String regionName=obj.getString("regionName");
                    String region=obj.getString("region");
                    String city=obj.getString("city");
                    String status=obj.getString("status");
                    String lat=obj.getString("lat");
                    String lon=obj.getString("lon");
                    String country=obj.getString("countryCode");
                    ipjson_map.put("status",status);
                    ipjson_map.put("lat",lat);
                    ipjson_map.put("lon",lon);
                    ipjson_map.put("region",region);
                    ipjson_map.put("regionName",regionName);
                    ipjson_map.put("city",city);
                    ipjson_map.put("country",country);
                    getWeatherData(ipjson_map);
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

    public void getWeatherData(HashMap<String,String> map){
        String lat="";
        String lon="";
        lat=map.get("lat");
        lon=map.get("lon");
//        http://prateekparhinode.us-east-2.elasticbeanstalk.com
        String url="http://prateekhw9usc.us-east-2.elasticbeanstalk.com"+"/weatherplace?lat="+lat+"&"+"lon="+lon;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String weather_json_string=response.toString();
                try {
                    JSONObject obj = new JSONObject(weather_json_string);
                    currentlyobj_main=obj.getJSONObject("currently");
                    dailyobj=obj.getJSONObject("daily");
                    System.out.println("dailyjson "+dailyobj.toString());

                    String sum=currentlyobj_main.getString("icon");
                    String temp=currentlyobj_main.getString("temperature");
                    String city=ipjson_map.get("city");
                    String country=ipjson_map.get("country");
                    String region=ipjson_map.get("region");

                    summ_page_card1_data.put("icon",sum);
                    summ_page_card1_data.put("temp",temp);
                    summ_page_card1_data.put("city",city);
                    summ_page_card1_data.put("country",country);
                    summ_page_card1_data.put("region",region);


                    String humidity=currentlyobj_main.getString("humidity");
                    String windSpeed=currentlyobj_main.getString("windSpeed");
                    String visibility=currentlyobj_main.getString("visibility");
                    String pressure=currentlyobj_main.getString("pressure");

                    Double humid=Double.parseDouble(humidity);
                    int humidvalue = humid.intValue();
                    humidity=humid.toString();

                    summ_page_card2_data.put("humidity",humidity);
                    summ_page_card2_data.put("windspeed",windSpeed);
                    summ_page_card2_data.put("visibility",visibility);
                    summ_page_card2_data.put("pressure",pressure);


                    dailyweatherarr=dailyobj.getJSONArray("data");
                    System.out.println("dailyweahterarr  "+dailyweatherarr.toString());
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
//                        System.out.println("day"+i+" -> "+dateString+" "+tempHigh+" "+tempLow+" "+icon);
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
//                            System.out.println("cloudy: "+x);
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

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    public void addSummCard2(HashMap<String,String> mapcard2) {

        //Get Id's
        ImageView card2_r1_humidicon = (ImageView) findViewById(R.id.card2_row1_humidicon);
        ImageView card2_r1_windicon  = (ImageView) findViewById(R.id.card2_row1_windspeedicon);
        ImageView card2_r1_visibicon = (ImageView) findViewById(R.id.card2_row1_visibilityicon);
        ImageView card2_r1_pressicon = (ImageView) findViewById(R.id.card2_row1_pressureicon);

        TextView card2_r2_humidval = (TextView) findViewById(R.id.card2_row2_txt1);
        TextView card2_r2_windval  = (TextView) findViewById(R.id.card2_row2_txt2);
        TextView card2_r2_visibval = (TextView) findViewById(R.id.card2_row2_txt3);
        TextView card2_r2_pressureval = (TextView) findViewById(R.id.card2_row2_txt4);

        TextView card2_r3_Humidity = (TextView) findViewById(R.id.card2_row3_txt1);
        TextView card2_r3_WindSpeed  = (TextView) findViewById(R.id.card2_row3_txt2);
        TextView card2_r3_Visibility = (TextView) findViewById(R.id.card2_row3_txt3);
        TextView card2_r3_Pressure = (TextView) findViewById(R.id.card2_row3_txt4);



        double x=Double.valueOf(mapcard2.get("humidity"));
        x=x*100;
        int humidpercent=(int)x;
        String humidpercentstr=humidpercent+" %";


        //Set Id's
        DecimalFormat df2 = new DecimalFormat("#.##");
        String visib=df2.format(Double.valueOf(mapcard2.get("visibility")));
        String press=df2.format(Double.valueOf(mapcard2.get("pressure")));
        String wind=df2.format(Double.valueOf(mapcard2.get("windspeed")));

        card2_r1_humidicon.setImageResource(R.drawable.water_percent);
        card2_r1_windicon.setImageResource(R.drawable.weather_windy);
        card2_r1_visibicon.setImageResource(R.drawable.eye_outline);
        card2_r1_pressicon.setImageResource(R.drawable.gauge);

        card2_r2_humidval.setText(humidpercentstr);
        card2_r2_pressureval.setText(press+" mb");
        card2_r2_visibval.setText(visib+" km");
        card2_r2_windval.setText(wind+" mph");

        card2_r3_Pressure.setText("Pressure");
        card2_r3_WindSpeed.setText("Wind Speed");
        card2_r3_Visibility.setText("Visibility");
        card2_r3_Humidity.setText("Humidity");

    }

    public void addSummCard1(HashMap<String,String> mapcard1){
        //Get ID's
        TextView card1_temp =(TextView) findViewById(R.id.card1_textview1);
        TextView card1_summ=(TextView) findViewById(R.id.card1_textview2);
        TextView card1_loc =(TextView) findViewById(R.id.card1_textview3);
        ImageView card1_icon=(ImageView) findViewById(R.id.card1_summicon);
        CardView cardView_summ=(CardView) findViewById(R.id.card_view1_summ);

        //Set ID's;
        temp=Double.valueOf(mapcard1.get("temp"));
        tem=(int)Math.rint(temp);



        card1_temp.setText(tem+"\u00B0"+" F");
        card1_summ.setText(mapcard1.get("icon"));
        card1_loc.setText(mapcard1.get("city")+", "+mapcard1.get("region")+", "+mapcard1.get("country"));

        String icon=mapcard1.get("icon");

        if("clear-day".equals(icon)){
            card1_icon.setImageResource(R.drawable.weather_sunny);
        }
        if("clear-night".equals(icon)){
            card1_icon.setImageResource(R.drawable.weather_night);
        }
        if("rain".equals(icon)){
            card1_icon.setImageResource(R.drawable.weather_rainy);
        }
        if("sleet".equals(icon)){
            card1_icon.setImageResource(R.drawable.weather_snowy_rainy);
        }
        if("snow".equals(icon)){
            card1_icon.setImageResource(R.drawable.weather_snowy);
        }
        if("wind".equals(icon)){
            card1_icon.setImageResource(R.drawable.weather_windy_variant);
        }
        if("fog".equals(icon)){
            card1_icon.setImageResource(R.drawable.weather_fog);
        }
        if("cloudy".equals(icon)){
            card1_icon.setImageResource(R.drawable.weather_cloudy);
        }
        if("partly-cloudy-day".equals(icon)){
            card1_icon.setImageResource(R.drawable.weather_partly_cloudy);
        }
        if("partly-cloudy-night".equals(icon)){
            card1_icon.setImageResource(R.drawable.weather_night_partly_cloudy);
        }

    }

    public void addSummCard3(List<List<String>> dailydatalist){


        TextView card3_r1_txt1 = (TextView) findViewById(R.id.card3_r1_text1);
        TextView card3_r1_txt2 = (TextView) findViewById(R.id.card3_r1_text2);
        TextView card3_r1_txt3 = (TextView) findViewById(R.id.card3_r1_text3);
        ImageView card3_r1_icon = (ImageView) findViewById(R.id.card3_r1_icon);

        card3_r1_txt1.setText(dailydatalist.get(0).get(0));
        card3_r1_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(0).get(1))));
        card3_r1_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(0).get(2)))));
        card3_r1_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(0).get(3)))));

        TextView card3_r2_txt1 = (TextView) findViewById(R.id.card3_r2_text1);
        TextView card3_r2_txt2 = (TextView) findViewById(R.id.card3_r2_text2);
        TextView card3_r2_txt3 = (TextView) findViewById(R.id.card3_r2_text3);
        ImageView card3_r2_icon = (ImageView) findViewById(R.id.card3_r2_icon);

        card3_r2_txt1.setText(dailydatalist.get(1).get(0));
        card3_r2_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(1).get(1))));
        card3_r2_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(1).get(2)))));
        card3_r2_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(1).get(3)))));


        TextView card3_r3_txt1 = (TextView) findViewById(R.id.card3_r3_text1);
        TextView card3_r3_txt2 = (TextView) findViewById(R.id.card3_r3_text2);
        TextView card3_r3_txt3 = (TextView) findViewById(R.id.card3_r3_text3);
        ImageView card3_r3_icon = (ImageView) findViewById(R.id.card3_r3_icon);

        card3_r3_txt1.setText(dailydatalist.get(2).get(0));
        card3_r3_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(2).get(1))));
        card3_r3_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(2).get(2)))));
        card3_r3_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(2).get(3)))));


        TextView card3_r4_txt1 = (TextView) findViewById(R.id.card3_r4_text1);
        TextView card3_r4_txt2 = (TextView) findViewById(R.id.card3_r4_text2);
        TextView card3_r4_txt3 = (TextView) findViewById(R.id.card3_r4_text3);
        ImageView card3_r4_icon = (ImageView) findViewById(R.id.card3_r4_icon);

        card3_r4_txt1.setText(dailydatalist.get(3).get(0));
        card3_r4_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(3).get(1))));
        card3_r4_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(3).get(2)))));
        card3_r4_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(3).get(3)))));

        TextView card3_r5_txt1 = (TextView) findViewById(R.id.card3_r5_text1);
        TextView card3_r5_txt2 = (TextView) findViewById(R.id.card3_r5_text2);
        TextView card3_r5_txt3 = (TextView) findViewById(R.id.card3_r5_text3);
        ImageView card3_r5_icon = (ImageView) findViewById(R.id.card3_r5_icon);

        card3_r5_txt1.setText(dailydatalist.get(4).get(0));
        card3_r5_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(4).get(1))));
        card3_r5_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(4).get(2)))));
        card3_r5_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(4).get(3)))));

        TextView card3_r6_txt1 = (TextView) findViewById(R.id.card3_r6_text1);
        TextView card3_r6_txt2 = (TextView) findViewById(R.id.card3_r6_text2);
        TextView card3_r6_txt3 = (TextView) findViewById(R.id.card3_r6_text3);
        ImageView card3_r6_icon = (ImageView) findViewById(R.id.card3_r6_icon);

        card3_r6_txt1.setText(dailydatalist.get(5).get(0));
        card3_r6_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(5).get(1))));
        card3_r6_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(5).get(2)))));
        card3_r6_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(5).get(3)))));


        TextView card3_r7_txt1 = (TextView) findViewById(R.id.card3_r7_text1);
        TextView card3_r7_txt2 = (TextView) findViewById(R.id.card3_r7_text2);
        TextView card3_r7_txt3 = (TextView) findViewById(R.id.card3_r7_text3);
        ImageView card3_r7_icon = (ImageView) findViewById(R.id.card3_r7_icon);

        card3_r7_txt1.setText(dailydatalist.get(6).get(0));
        card3_r7_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(6).get(1))));
        card3_r7_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(6).get(2)))));
        card3_r7_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(6).get(3)))));

        TextView card3_r8_txt1 = (TextView) findViewById(R.id.card3_r8_text1);
        TextView card3_r8_txt2 = (TextView) findViewById(R.id.card3_r8_text2);
        TextView card3_r8_txt3 = (TextView) findViewById(R.id.card3_r8_text3);
        ImageView card3_r8_icon = (ImageView) findViewById(R.id.card3_r8_icon);

        card3_r8_txt1.setText(dailydatalist.get(7).get(0));
        card3_r8_icon.setImageResource(Integer.parseInt(icons.get(dailydatalist.get(7).get(1))));
        card3_r8_txt2.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(7).get(2)))));
        card3_r8_txt3.setText(String.valueOf(Math.round(Double.parseDouble(dailydatalist.get(7).get(3)))));
    }

    public void hideviews(){
        Toolbar tool=(Toolbar)findViewById(R.id.app_bar_summary);
        ImageView imv1=(ImageView)findViewById(R.id.favtabid);
        ProgressBar bar1=(ProgressBar)findViewById(R.id.progressBar1);
        CardView cv1=(CardView)findViewById(R.id.card_view1_summ);
        CardView cv2=(CardView)findViewById(R.id.card_view2_summ);
        ScrollView sv=(ScrollView)findViewById(R.id.scollview_page1);
        TextView tv=(TextView)findViewById(R.id.tv_below_prog_main);
        imv1.setVisibility(View.GONE);
        cv1.setVisibility(View.GONE);
        sv.setVisibility(View.GONE);
        tool.setVisibility(View.GONE);
        cv2.setVisibility(View.GONE);
        bar1.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
    }

    public void showviews(){
        Toolbar tool=(Toolbar)findViewById(R.id.app_bar_summary);
        ProgressBar bar1=(ProgressBar)findViewById(R.id.progressBar1);
        ImageView imv1=(ImageView)findViewById(R.id.favtabid);
        CardView cv1=(CardView)findViewById(R.id.card_view1_summ);
        CardView cv2=(CardView)findViewById(R.id.card_view2_summ);
        ScrollView sv=(ScrollView)findViewById(R.id.scollview_page1);
        TextView tv=(TextView)findViewById(R.id.tv_below_prog_main);
        cv1.setVisibility(View.VISIBLE);
        sv.setVisibility(View.VISIBLE);
        tool.setVisibility(View.VISIBLE);
        cv2.setVisibility(View.VISIBLE);
        imv1.setVisibility(View.VISIBLE);
        bar1.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
    }

    public void startSummaryActivity(View view){
        hideviews();
        String  weatherSearchQuery1="";
        if(weatherSearchQuery1==""){
            weatherSearchQuery1=ipjson_map.get("regionName")+", United States";
        }
//        System.out.println("Inside startSummaryActivity func: and sending intent and message -> "+weatherSearchQuery1+" to "+"DetailTabsActivity");
        Intent intent = new Intent(MainActivity.this,DetailTabs.class);
        intent.putExtra("searchQuery",weatherSearchQuery1);
        intent.putExtra("citynametweetmain",ipjson_map.get("city")+", "+ipjson_map.get("region")+", USA");
        intent.putExtra("temptweet",tem+"\u00B0"+" F");
        intent.putExtra("currentlyJSONString", currentlyobj_main.toString());
        intent.putExtra("dailyweatherjson",dailyobj.toString());
        intent.putStringArrayListExtra("ImgUrl",ImgUrl);
        startActivityForResult(intent, 3);// Activity is started with requestCode 2
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("inside menu mainactivity");
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        // Get SearchView object.
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                startSearchableActivity(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String text) {
                //auto complete
                return false;
            }
        });

        return true;
    }


    private void startSearchableActivity(String query) {
        System.out.println("query: "+query);
        weatherSearchQuery=query;
        System.out.println(query);
        if(weatherSearchQuery==""){
            String splitAddr[]=query.split(", ");
            System.out.println("Inside startSummaryActivity func in searchable activity "+splitAddr);
            weatherSearchQuery=splitAddr[0]+", "+splitAddr[1]+", USA";
        }
//        System.out.println("Inside searchable activity func: and sending intent and message -> "+weatherSearchQuery+" to "+"SearchableActivity");
        Intent intent = new Intent(MainActivity.this, SearchableActivity.class);
        intent.putExtra("searchQuery",weatherSearchQuery);
        intent.putExtra("fulladdr",fullAddr);
        startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            System.out.println("request code: "+requestCode);

        if (resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            System.out.println("Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());

            fullAddr=place.getAddress();
            LatLng latLng = place.getLatLng();
            double MyLat = latLng.latitude;
            double MyLong = latLng.longitude;
            System.out.println("Lat: "+MyLat+" Long"+MyLong);
            String stateName="";
            String cityName="";
            String countryName="";

            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
                Locale locale = addresses.get(0).getLocale();
                stateName = addresses.get(0).getAdminArea();
                cityName = addresses.get(0).getLocality();
                countryName = addresses.get(0).getCountryCode();
                System.out.println("City Name: "+cityName+" "+"State Name: "+stateName+"Country Name: "+countryName);
            } catch (IOException e) {
                e.printStackTrace();

            }
            String address = place.getAddress();
            System.out.println("Address: "+ address);

            startSearchableActivity(address);


            // do query with address

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            Status status = Autocomplete.getStatusFromIntent(data);
        } else if (resultCode == RESULT_CANCELED) {
        }
    }
        if(requestCode==2)
        {
            showviews();
            System.out.println("Searchable activity ended..back to Main activity "+requestCode);
        }

        if(requestCode==1){
            showviews();
            System.out.println("Detail Tabs Activity ended..back to Main activity" +requestCode);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchCalled();
                return true;
            default:
                return false;
        }
    }


    public void onSearchCalled() {
        System.out.println("on search called");
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        System.out.println(fields);
        hideviews();
//        Intent intent=new PlaceAutocomplete.IntentBuilder();
//        try {
//            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
//                    .build();
//            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(this);
//            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//        }
//        catch (GooglePlayServicesRepairableException |
//                GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).setCountry("US") .build(this);
//        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).s
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);  //1
    }


    private void getPhotos(){
        String url = "http://prateekhw9usc.us-east-2.elasticbeanstalk.com/customsearch?cityname="+ipjson_map.get("city").replace(" ","");
        System.out.println("in MainActivity photos url  "+url);
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
    public void onRestart(){
        super.onRestart();
        showviews();

        System.out.println("showing views in restart");
    }

    @Override
    public void onStart(){
        super.onStart();
        showviews();

        System.out.println("showing views in start");
    }



}
