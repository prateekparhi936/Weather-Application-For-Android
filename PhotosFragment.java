package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhotosFragment extends Fragment {
    String cityname="";
    List<String> urlphotos=new ArrayList<>();
    ArrayList<String> ImgUrl= new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager Manager;
    RecyclerView.Adapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DetailTabs activity=(DetailTabs) getActivity();
        ImgUrl=activity.ImgUrl;
        System.out.println("In Photo frag: "+ImgUrl.toString());
        return inflater.inflate(R.layout.fragment_photos, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.recyclerView = getView().findViewById(R.id.recyclerView);
        Manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(Manager);
        adapter = new Adapter(ImgUrl,getContext());
        recyclerView.setAdapter(adapter);
    }

}
