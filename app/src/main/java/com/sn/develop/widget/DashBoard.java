package com.sn.develop.widget;

import android.content.Context;
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
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.sn.develop.utils.PixUtil;

public class DashBoard extends View {

    private Paint paint;
    private RectF rectF;
    private RectF rect;
    private float angle = 120;
    float radius;
    private PathEffect pathEffect;
    private Path path;
    private PathMeasure pathMeasure;
    private Path circlePath;
    private Path linePath;
    private float indictAngle = 24;


    public DashBoard(Context context) {
        super(context);
    }

    public DashBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public DashBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        radius = PixUtil.dpToPx(100);
        rectF = new RectF( - radius, -radius, radius, radius);

        pathMeasure = new PathMeasure();
        circlePath = new Path();
        circlePath.addArc(rectF,90+ angle/2,360-angle);
        pathMeasure.setPath(circlePath,false);
        pathMeasure.getLength();

        path = new Path();
        rect = new RectF();
        rect.set(0,0,PixUtil.dpToPx(1),PixUtil.dpToPx(10));
        path.addRect(rect, Path.Direction.CW);
        float dashMargin = (pathMeasure.getLength()-20*PixUtil.dpToPx(1))/20;
        pathEffect = new PathDashPathEffect(path, dashMargin,0, PathDashPathEffect.Style.ROTATE);

        linePath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getWidth()>>1,getHeight()>>1);
        //绘制弧
        canvas.save();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(PixUtil.dpToPx(1));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(pathEffect);
        canvas.drawArc(rectF,90+ angle/2,360-angle,false,paint);
        canvas.restore();

        paint.setPathEffect(null);
        canvas.drawArc(rectF,90+ angle/2,360-angle,false,paint);
        indictAngle += 90+ angle/2;
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.BEVEL);
        float endX = radius*0.6f * (float) Math.cos(Math.toRadians(indictAngle));
        float endY =  radius*0.6f *(float) Math.sin(Math.toRadians(indictAngle));
        //绘制箭头
        float leftEndX = radius*0.5f * (float) Math.cos(Math.toRadians(indictAngle-5));
        float leftEndY =  radius*0.5f *(float) Math.sin(Math.toRadians(indictAngle-5));
        float rightEndX = radius*0.5f * (float) Math.cos(Math.toRadians(indictAngle+5));
        float rightEndY =  radius*0.5f *(float) Math.sin(Math.toRadians(indictAngle+5));
        linePath.moveTo(0,0);
        linePath.lineTo(endX,endY);
        linePath.lineTo(leftEndX,leftEndY);
        linePath.moveTo(endX,endY);
        linePath.lineTo(rightEndX,rightEndY);
        canvas.drawPath(linePath,paint);

        paint.setStrokeWidth(PixUtil.dpToPx(5));
        canvas.drawPoint(0,0,paint);



    }

}

