package com.sn.develop.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TagLayout extends ViewGroup {


    //存储每个子view的边界值，即相对父view中的位置
    private List<Rect> childRects = new ArrayList<>();
    private List<View> matchParentViews = new ArrayList<>();
    private Rect rect;


    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int useWidth = 0;
        int useHeight = 0;
        int lineWidth = 0;
        int lineHeight = 0;
        int width = 0;
        int height = 0;
        for(int i =0;i<getChildCount();i++){
            View child = getChildAt(i);
            LayoutParams layoutParams = child.getLayoutParams();
            //将child是MATCH_PARENT单独放起来，因为此时自己的大小没有确定，当自己可能是warp-content时，就无法确定child的宽度
            if(layoutParams.width == LayoutParams.MATCH_PARENT){
                matchParentViews.add(child);
            }
            measureChildWithMargins(child,widthMeasureSpec,0,heightMeasureSpec,0);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //换行设置，
            if (lineWidth + childWidth > widthSize){
                lineWidth = 0;
                useHeight += lineHeight;
            }

            //set the child bound rect
            if(childRects.size() < getChildCount()){
                childRects.add(new Rect());
            }
            childRects.get(i).set(lineWidth,useHeight,lineWidth+childWidth,useHeight+childHeight);



            lineWidth = lineWidth + childWidth;
            lineHeight = Math.max(lineHeight,childHeight);



            useWidth = useWidth+child.getMeasuredWidth();


        }
        useHeight = useHeight + lineHeight;
        setMeasuredDimension(widthSize,useHeight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int length = getChildCount();
        for (int i = 0; i < length; i++) {
            View child = getChildAt(i);

            child.layout(childRects.get(i).left,childRects.get(i).top,childRects.get(i).right,childRects.get(i).bottom);
        }
    }



    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }
}
