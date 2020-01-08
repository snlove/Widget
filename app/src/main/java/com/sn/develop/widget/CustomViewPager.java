package com.sn.develop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public class CustomViewPager extends ViewGroup {

    public static final String TAG = "ViewPager";


    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
          int count = getChildCount();
          for(int i = 0; i< count;i++){
              View child = getChildAt(i);
              child.layout(l + i* getWidth(),t,l+getWidth()+i* getWidth(),b);
          }
    }

    private float lastX = 0;
    private float lastY = 0;

    /**
     * 冲突处理，默认子View消耗事件down，如果不消化,没有冲突一说
     * View,TextView,ImageView,onTouchEvent默认不消耗事件
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean result = false;
        float x = 0;
        float y = 0;
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN :
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x = ev.getX();
                y = ev.getY();
                float distantX = x - lastX;
                float distantY = y - lastY;
                //判断是否大于最小滑动距离，横向滑动大于竖向滑动，拦截，此时，子View收到Cancel取消事件，后续的事件都有拦截的parent处理
                //cancel后，会将mFirstTarget清空后续的事件都有拦截的parent处理
                if(Math.abs(distantX) > ViewConfiguration.get(getContext()).getScaledTouchSlop() && Math.abs(distantX) > Math.abs(distantY)){
                    Log.d(TAG, "onInterceptTouchEvent: ");
                    result = true;
                    requestDisallowInterceptTouchEvent(false);  //禁止父级拦截
                }
                break;
            case MotionEvent.ACTION_UP:
                lastX = ev.getX();
                lastY = ev.getY();
                break;
        }

        return  result;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float x = 0;
        float y = 0;
        switch (ev.getActionMasked()){
            //这里与onInterceptTouchEvent一样，这是因为部分区域没有child，此时事件坐标就不会在onInterceptTouchEvent记录
            case MotionEvent.ACTION_DOWN :
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x = ev.getX();
                y = ev.getY();
                float distantX = x - lastX;
                float distantY = y - lastY;
                //判断是否大于最小滑动距离，横向滑动大于竖向滑动，拦截，此时，子View收到Cancel取消事件，后续的事件都有拦截的parent处理
                //cancel后，会将mFirstTarget清空后续的事件都有拦截的parent处理
                if(Math.abs(distantX) > ViewConfiguration.get(getContext()).getScaledTouchSlop() && Math.abs(distantX) > Math.abs(distantY)){
                    Log.d(TAG, "onTouchEvent: " +distantX);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: " +0);
                lastX = ev.getX();
                lastY = ev.getY();
                break;
        }

        return  true;
    }
}
