package com.sn.develop.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sn.develop.R;
import com.sn.develop.utils.PixUtil;

/**
 * 多点触控,交替操控，
 * event.getX 默认是0的
 * id可以复用，
 * index永远是连续的
 * 查找用id
 */
public class MultiTouch extends View {

    private Paint paint;
    private Bitmap bitmap;

    private float offsetX = 0;
    private float offsetY = 0;
    private float orignalOffsetX = 0;
    private float originalOffsetY = 0;
    private float downX;
    private float downY;

    public MultiTouch(Context context) {
        super(context);
    }

    public MultiTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultiTouch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = PixUtil.getBitmap(getResources(), R.drawable.girl, 300);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }


    int tractPointId = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                tractPointId = event.getPointerId(0);
                downX = event.getX();
                downY = event.getY();
                orignalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int actionIndex = event.getActionIndex();
                tractPointId = event.getPointerId(actionIndex);
                downX = event.getX(actionIndex);
                downY = event.getY(actionIndex);
                orignalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                int index = event.findPointerIndex(tractPointId);
                offsetX = orignalOffsetX + event.getX(index) - downX;
                offsetY = originalOffsetY + event.getY(index) - downY;
                invalidate();
                break;

            case MotionEvent.ACTION_POINTER_UP:
                int activeIndex = event.getActionIndex();
                //如果控制的离开，交由最后的控制，如果
                int pointerId = event.getPointerId(activeIndex);
                if (pointerId == tractPointId) {
                    int newIndex = 0;
                    if (activeIndex == event.getPointerCount() - 1) {
                        newIndex = event.getPointerCount() - 2;
                    } else {
                        newIndex = event.getPointerCount() - 1;
                    }
                    tractPointId = event.getPointerId(newIndex);
                    downX = event.getX(newIndex);
                    downY = event.getY(newIndex);
                    orignalOffsetX = offsetX;
                    originalOffsetY = offsetY;

                }


                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }
}
