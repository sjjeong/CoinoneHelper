/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googry.coinonehelper.util;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * An assortment of UI helpers.
 */
public class UIUtil {
    private static ThreadLocal<SimpleDateFormat> dfTime = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("aa hh:mm", Locale.getDefault());
        }
    };
    private static ThreadLocal<SimpleDateFormat> dfDateTime = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        }
    };

    public static String formatTime(@NonNull Date time) {
        return dfTime.get().format(time);
    }

    public static String formatDateTime(@NonNull Date time) {
        return dfDateTime.get().format(time);
    }

    public static String formatDateTime(long time){
        return formatDateTime(new Date(time));
    }
}
