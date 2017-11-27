package com.googry.coinonehelper.binding;

import android.databinding.BindingAdapter;
import android.support.v4.widget.TextViewCompat;
import android.widget.TextView;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class AutoSizeTextViewBinding {

    @BindingAdapter("bind:autoSizeTextType")
    public static void autoSizeTextType(TextView textView, @TextViewCompat.AutoSizeTextType int autoSizeTextType) {

        TextViewCompat.setAutoSizeTextTypeWithDefaults(
                textView, autoSizeTextType
        );

    }


}
