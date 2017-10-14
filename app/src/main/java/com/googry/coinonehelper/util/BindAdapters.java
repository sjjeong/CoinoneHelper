package com.googry.coinonehelper.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

/**
 * Created by seokjunjeong on 2017. 10. 14..
 */

public class BindAdapters {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        ImageUtil.loadImage(imageView, url);
    }
}
