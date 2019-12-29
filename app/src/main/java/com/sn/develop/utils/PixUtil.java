package com.sn.develop.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class PixUtil {

    public static  float dpToPx(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static Bitmap getBitmap(Resources resources, int resourceId, int inch){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resourceId, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = inch;
        return BitmapFactory.decodeResource(resources,resourceId, options);

    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth,int reqHeight){
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        // 宽或高大于预期就将采样率 *=2 进行缩放
        if(width > reqWidth || height > reqHeight){
            final int halfHeight = height/2;
            final int halfWidth = width/2;
            while((halfHeight / inSampleSize) >= reqHeight &&
                    (halfWidth / inSampleSize) >= reqWidth){
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
