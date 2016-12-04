package com.themoviedb.utils;

import android.net.Uri;

import com.themoviedb.api.ServiceConfig;

public class ImageUtils {
    public static Uri movieUrl(String size, String posterPath) {
        posterPath = posterPath.replace("/", "");
        return Uri.parse(ServiceConfig.BASE_IMAGE_URL).buildUpon()
                .appendPath(size)
                .appendPath(posterPath)
                .build();
    }
}
