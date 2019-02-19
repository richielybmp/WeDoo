package com.espfullstack.wedoo.helper;

import android.content.Context;
import android.util.DisplayMetrics;

public class AndroidUtils {
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
