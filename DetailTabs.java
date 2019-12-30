package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailTabs extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    List<String> urlphotos=new ArrayList<>();
    ArrayList<String> ImgUrl= new ArrayList<>();
    JSONObject jsonObj=new JSONObject();
    JSONObject dailyjsonobj=new JSONObject();
    String citytweetmain="";
    String citytweetsearch="";
    String temptweet="";
    private ViewPager viewPager;private static final String DESCRIBABLE_KEY = "describable_key";

    JSONObject jsonObjcurrently=new JSONObject();
    JSONArray jsonArray=new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Inside Detail tabs activity class");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tabs);
        Toolbar toolbar=(Toolbar) findViewById(R.id.app_bar_summary);
        if(toolbar==null){
            System.out.println("toolbar is null");
        }
        setSupportActionBar(toolbar);
        Intent intent=new Intent();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        String searchQuery = bundle.getString("searchQuery");
        citytweetmain=bundle.getString("citynametweetmain");
        citytweetsearch=bundle.getString("citynametweetsearch");
        System.out.println("citytweetmain: "+citytweetmain);
        System.out.println("citytweetsearch: "+citytweetsearch);
        temptweet=bundle.getString("temptweet");
        ImgUrl=getIntent().getStringArrayListExtra("ImgUrl");

        System.out.println("In Detail tab: "+ImgUrl.toString());

        System.out.println("search query "+searchQuery);
        try {
            jsonObjcurrently = new JSONObject(getIntent().getStringExtra("currentlyJSONString"));
            dailyjsonobj=new JSONObject(getIntent().getStringExtra("dailyweatherjson"));
            System.out.println("In Detail Activity: "+jsonObjcurrently.toString());
            System.out.println("In Detail Activity: "+dailyjsonobj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setResult(3,intent);
        String state=searchQuery.split(",")[0];
        if (citytweetmain!=null) {
            System.out.println("inside tweet main: "+citytweetmain);
            getSupportActionBar().setTitle(citytweetmain);
        }
        if(citytweetsearch!=null){
            System.out.println("inside tweet search: "+citytweetsearch);
            getSupportActionBar().setTitle(citytweetsearch);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.isEnabled();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        Color mColor = new Color();
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorblack));
        viewPager.setBackgroundColor(getResources().getColor(R.color.colorblack));
        int[] tabIcons = { R.drawable.calendar_today, R.drawable.trending_up, R.drawable.google_photos };

        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new TodayFragment(), "Today");
        adapter.addFragment(new WeeklyFragment(), "Weekly");
        adapter.addFragment(new PhotosFragment(), "Photos");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.twitter_feed:
                twitterClick();
                return true;
            default:
                return false;
        }
    }

    private void twitterClick() {
        String citytweet="";
        if(citytweetmain!=null){
            citytweet=citytweetmain;
        }
        if(citytweetsearch!=null){
            citytweet=citytweetsearch;
        }
        String uritext="https://twitter.com/intent/tweet?text=Check Out "+citytweet+" Weather! +It is "+temptweet+"! "+" %23"+"CSCI571WeatherSearch";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uritext));
        startActivity(browserIntent);

    }

}
