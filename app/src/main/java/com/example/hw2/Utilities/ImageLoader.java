package com.example.hw2.Utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hw2.R;


public class ImageLoader {
    private static Context appContext;

    public ImageLoader(Context context) {
        if (context == null)
            throw new IllegalArgumentException("Context cannot be null");
        appContext = context;
    }

    public void load (String link, ImageView imageView){
        Glide.
                with(appContext)
                .load(link)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);
    }

    public void load (int drawableId, ImageView imageView){
        Glide.
                with(appContext)
                .load(drawableId)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);
    }
}