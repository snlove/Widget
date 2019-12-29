package com.sn.develop.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.OverScroller;

import androidx.core.view.GestureDetectorCompat;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import com.sn.develop.R;
import com.sn.develop.utils.PixUtil;

public class ScaleableImage extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private Bitmap bitmap;
    private Paint paint;
    private float smallScale;
    private float bigScale;
    private boolean big = false;
    private GestureDetectorCompat gestureDetectorCompat;
    private float fraction;
    private float translateFraction;
    private ObjectAnimator animator;
    private ObjectAnimator reverseAnimator;
    private static final float OVER_SCALE_FACTOR = 1.5f;
    private float offsetX = 0;
    private float offsetY = 0;
    private OverScroller scroller;
    private float arch;
    private float offsetRadius = 0;
    private int sign = 1;
    public ScaleableImage(Context context) {
        super(context);
    }

    public ScaleableImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleableImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        bitmap = PixUtil.getBitmap(getResources(), R.drawable.timg, 600);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gestureDetectorCompat = new GestureDetectorCompat(getContext(),this);
        gestureDetectorCompat.setOnDoubleTapListener(this);
        scroller = new OverScroller(getContext());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if ((float) bitmap.getWidth() / bitmap.getHeight() > (float) getWidth() / getHeight()) {
            smallScale = (float) getWidth() / bitmap.getWidth();
            bigScale = (float) getHeight() / bitmap.getHeight() * OVER_SCALE_FACTOR;
        } else {
            smallScale = (float) getHeight() / bitmap.getHeight();
            bigScale = (float) getWidth() / bitmap.getWidth() * OVER_SCALE_FACTOR;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getWidth()>>1,getHeight()>>1);
        float scale = smallScale + (bigScale - smallScale) * fraction;
        if(!big){
            sign = offsetX > 0 ? -1 : 1;
            offsetX = (float) (offsetRadius*Math.sin(arch))* translateFraction;
            offsetX = Math.abs(offsetX)*sign;
            sign = offsetY > 0 ? -1 : 1;
            offsetY = (float) (offsetRadius*Math.cos(arch))* translateFraction;
            offsetY = Math.abs(offsetY)*sign;

            canvas.translate(offsetX, offsetY);
            canvas.scale(scale,scale,0,0);
        } else {
            canvas.translate(offsetX, offsetY);
            canvas.scale(scale,scale,0,0);

        }
        canvas.drawBitmap(bitmap,-bitmap.getWidth()/2f,-bitmap.getHeight()/2f,paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(big){
            offsetX += -distanceX;
            offsetY += -distanceY;
            fixedOffset();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            }, 100);
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(big) {
            int maxX = (int) ((bitmap.getWidth() * bigScale - getWidth()) / 2);
            int minX = (int) (-(bitmap.getWidth() * bigScale - getWidth()) / 2);
            int minY = (int) (-(bitmap.getHeight() * bigScale - getHeight()) / 2);
            int maxY = (int) ((bitmap.getHeight() * bigScale - getHeight()) / 2);
            scroller.fling((int) offsetX, (int) offsetY, (int) velocityX, (int) velocityY, minX, maxX, minY, maxY);
            postOnAnimation(new FlingRunner());
        }
        return false;
    }

    private void  refresh(){

    }

    public float getTranslateFraction() {
        return translateFraction;
    }

    public void setTranslateFraction(float translateFraction) {
        this.translateFraction = translateFraction;
    }

    private class FlingRunner implements Runnable {

        @Override
        public void run() {
            if(scroller.computeScrollOffset()){

                offsetX = scroller.getCurrX();
                offsetY = scroller.getCurrY();
                invalidate();
                postOnAnimation(this);

            }
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        big = !big;
        if(big){
            generateScaleAnimation().start();
        } else {
            arch = (float) Math.atan(offsetY / offsetX);
            offsetRadius = (float) ((float) Math.pow(offsetX, 2) + Math.pow(offsetY, 2));
            offsetRadius = (float) Math.sqrt(offsetRadius);
            recoveryAnimator().start();
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    private ObjectAnimator generateScaleAnimation(){

        if(animator == null){
            animator = ObjectAnimator.ofFloat(this,"fraction",0,1);
            animator.setDuration(500);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
        }
        return  animator;
    }
    private ObjectAnimator recoveryAnimator(){

        if(reverseAnimator == null){
            PropertyValuesHolder translateFractionHolder = PropertyValuesHolder.ofFloat("translateFraction",1,0);
            PropertyValuesHolder scaleFractonHolder = PropertyValuesHolder.ofFloat("fraction",1,0);
            reverseAnimator = ObjectAnimator.ofPropertyValuesHolder(this,scaleFractonHolder,translateFractionHolder);
            reverseAnimator.setDuration(500);
            reverseAnimator.setInterpolator(new FastOutLinearInInterpolator());
        }
        return  reverseAnimator;
    }

    public float getFraction() {
        return fraction;
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
        invalidate();
    }

    private void fixedOffset(){
        offsetX = Math.min(offsetX, (bitmap.getWidth() * bigScale - getWidth()) / 2);
        offsetX = Math.max(offsetX, - (bitmap.getWidth() * bigScale - getWidth()) / 2);
        offsetY = Math.min(offsetY, (bitmap.getHeight() * bigScale - getHeight()) / 2);
        offsetY = Math.max(offsetY, - (bitmap.getHeight() * bigScale - getHeight()) / 2);
    }
}