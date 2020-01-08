package com.sn.develop.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sn.develop.utils.PixUtil;

import java.lang.ref.SoftReference;

/**
 * 多点触控，每个独立控制
 */
public class DrawPaintView extends View {

    private Paint paint;
    private ArrayMap<Integer, Path> pathArrayMap = new ArrayMap<>();
    SoftReference<ArrayMap<Integer, Path>> reference = new SoftReference<>(pathArrayMap);
    public DrawPaintView(Context context) {
        super(context);
    }

    public DrawPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawPaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(PixUtil.dpToPx(5));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                int tractPointId = event.getPointerId(0);
                Path path = null;
                if(pathArrayMap.get(tractPointId) == null){
                    path = new Path();
                    pathArrayMap.put(tractPointId,path);
                } else {
                    path = pathArrayMap.get(tractPointId);
                }
                path.moveTo(event.getX(0),event.getY(0));
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int acitveIndex = event.getActionIndex();
                tractPointId = event.getPointerId(acitveIndex);
                if(pathArrayMap.get(tractPointId) == null){
                     path = new Path();
                     pathArrayMap.put(tractPointId,path);
                } else {
                    path = pathArrayMap.get(tractPointId);
                }
                path.moveTo(event.getX(acitveIndex),event.getY(acitveIndex));


                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i <event.getPointerCount(); i++) {
                    int index = event.getPointerId(i);
                    int pointId = event.getPointerId(index);
                    if(pathArrayMap.containsKey(pointId)){
                        pathArrayMap.get(pointId).lineTo(event.getX(index),event.getY(index));
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                acitveIndex = event.getActionIndex();
                tractPointId = event.getPointerId(acitveIndex);
//                if(pathArrayMap.get(tractPointId) == null){
//
//                } else {
//                    pathArrayMap.remove(tractPointId);
//                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }



        return  true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i <pathArrayMap.size() ; i++) {
            canvas.drawPath(pathArrayMap.valueAt(i),paint);
        }
    }
}
