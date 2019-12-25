package com.sn.develop.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.sn.develop.R;
import com.sn.develop.utils.PixUtil;

//图文混排中，文字的绘制，其中图片的数量为1
public class ImageTextView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String text = "中国汉代以前，人们对女性只注重面部形象，到了魏晋，才开始着重于装饰。魏文帝喜欢打扮华丽并将头发挽成蝉翼形的妃子。唐朝是开放社会，容许袒胸露臂，崇尚的女性体态美是额宽、脸圆、体胖。 [3] \n" +
            "唐朝以后没有定论。宋朝以后，大致是以观音菩萨的本貌作为女性美的高标准，各个时代所雕塑绘画的观音菩萨，就是当时审美标准的具体说明" +
            "时代对美的诠释更是大相径庭。唐代世人又以体态丰腴、丰胸肥臀的杨玉环为美；但是到了宋代人们以“身轻如燕，身姿窈窕的赵飞燕（汉代）为最美";
    private Bitmap bitmap;
    private float[] textWidth = new float[1];
    private Rect rect = new Rect();
    private float startBitmapY = 100;
    private float startBitmapX = 700;
    private float startTextX = 5;
    private boolean isBitmpaInCenter = true;
    private Paint.FontMetrics fontMetrics = new Paint.FontMetrics();
    public ImageTextView(Context context) {
        super(context);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = PixUtil.getBitmap(getResources(), R.drawable.girl, (int) PixUtil.dpToPx(25));

    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(PixUtil.dpToPx(16));
        paint.setTypeface(Typeface.SERIF);
        paint.setColor(Color.BLACK);
        paint.getFontMetrics(fontMetrics);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制图片
        canvas.drawBitmap(bitmap,startBitmapX,startBitmapY,paint);
        int startIndex = 0;
        float textY = 10 - fontMetrics.ascent + fontMetrics.descent;
        float rowMargin = paint.getFontSpacing() * 1.5f;
        //绘制文字
        while(startIndex < text.length()) {
            //判断y的坐标是否在图标范围内,即文字顶部的y坐标
            int addNum = 0;
            if(textY+fontMetrics.ascent >= startBitmapY && textY+fontMetrics.ascent <= (startBitmapY+bitmap.getHeight())){
                //当图片位于中间时，还需判断x轴的坐标
                if(isBitmpaInCenter){
                    int splitNumOne = paint.breakText(text, startIndex, text.length(), true, startBitmapX-startTextX, textWidth);
                    canvas.drawText(text, startIndex, startIndex+splitNumOne, startTextX,textY , paint);
                    int splitNumTwo = paint.breakText(text, startIndex+splitNumOne, text.length(), true,
                            getWidth()-bitmap.getWidth()-startBitmapX, textWidth);
                    canvas.drawText(text, startIndex+splitNumOne, startIndex+splitNumOne+splitNumTwo, startBitmapX+bitmap.getWidth()+10,textY , paint);
                    addNum = splitNumOne + splitNumTwo;

                } else {
                    addNum = paint.breakText(text, startIndex, text.length(), true, getWidth()-bitmap.getWidth(), textWidth);
                    canvas.drawText(text, startIndex, startIndex+addNum, startTextX+bitmap.getWidth()+startBitmapX,textY , paint);
                }

            } else {
                addNum = paint.breakText(text, startIndex, text.length(), true, getWidth(), textWidth);
                canvas.drawText(text, startIndex, startIndex+addNum, 5,textY , paint);
            }

            startIndex += addNum;
            textY = textY+rowMargin ;
        }

    }

}

