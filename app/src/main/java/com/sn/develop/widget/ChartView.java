package com.sn.develop.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class ChartView extends View {




        private Paint paint;
        private RectF rectF;
        private float startAngle = 240;
        private float addAngle = 0;
        private float[] angles = {30,60,40,70,80,120,45};
        private int[] colors = {Color.RED,Color.YELLOW,Color.BLUE,Color.GREEN,Color.CYAN,Color.BLACK};
        float radius;

        public ChartView(Context context) {
            super(context);
        }

        public ChartView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        {
            float shortSlideLength = 400;
            radius = 200;
            rectF = new RectF( - radius, -radius, radius, radius);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //设置中心为坐标系原点
            canvas.translate(getWidth()>>1,getHeight()>>1);

            float currentDegree = startAngle;
            int i = 0;
            while (true) {
                addAngle = angles[i];
                paint.setColor(colors[i%colors.length]);
                paint.setStyle(Paint.Style.FILL);
                if((currentDegree + addAngle) >= (startAngle+360)){
                    addAngle = startAngle+360 - currentDegree;
                    canvas.save();
                    float locationAngle = currentDegree + addAngle/2;
                    canvas.translate(20*((float)Math.cos(Math.toRadians(locationAngle))),20*((float)Math.sin(Math.toRadians(locationAngle))));
                    canvas.drawArc(rectF, currentDegree, addAngle, true, paint);
                    drawLineText(canvas,currentDegree,addAngle);
                    canvas.restore();
                    break;
                }
                canvas.drawArc(rectF, currentDegree, addAngle, true, paint);
                //绘制线
                drawLineText(canvas,currentDegree,addAngle);
                currentDegree = currentDegree + addAngle;
                i++;
            }


        }

        private void  drawLineText(Canvas canvas,float currentDegree,float addAngle){
            float textLocationAngle = currentDegree + addAngle/2;
            float endLine = radius + 100;
            float textStartX = (float) (radius*Math.cos(Math.toRadians(textLocationAngle)));
            float textStartY = (float) (radius*Math.sin(Math.toRadians(textLocationAngle)));
            float textEndX = (float) (endLine*Math.cos(Math.toRadians(textLocationAngle)));
            float textEndY = (float) (endLine*Math.sin(Math.toRadians(textLocationAngle)));
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            Log.d("Line", textStartX + "  " + textStartY);

            //判断象限，上面还是下面，设置坐标符号，currgent + swapDegree/2
            canvas.drawLine(textStartX,textStartY,textEndX,textEndY,paint);
            float addTextMargin = 50;
            float lineToTextMargin = 20 + paint.measureText("Lollipopo")/2;
            float compuateAngle = (currentDegree+addAngle) - (currentDegree+addAngle) % 360 * 360;
            if( textEndX< 0){
                addTextMargin = addTextMargin*(-1);
                lineToTextMargin = lineToTextMargin * (-1);
            }
            canvas.drawLine(textEndX,textEndY,textEndX+addTextMargin,textEndY,paint);
            paint.setTextSize(25);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Lollipopo",textEndX+addTextMargin+lineToTextMargin,textEndY,paint);
        }
    }

