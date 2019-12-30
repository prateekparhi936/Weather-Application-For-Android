package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    ArrayList<String> urls;
    Context context;

    public Adapter(ArrayList<String> ImgUrl, Context context_)
    {
        this.urls = ImgUrl;
        this.context = context_;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView image;

        public ViewHolder(View v)
        {
            super(v);
            image =(ImageView)v.findViewById(R.id.img);
        }

        public ImageView getImage(){ return this.image;}
    }


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
//        v.setLayoutParams(new RecyclerView.LayoutParams(1000,400));
//        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
//        RecyclerView.addItemDecoration(itemDecoration);

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4,20,4,23);
        rootView.setLayoutParams(lp);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(this.context).load(urls.get(position)).apply(requestOptions).into(holder.getImage());
    }
    @Override
    public int getItemCount()
    {
        return urls.size();
    }


}
