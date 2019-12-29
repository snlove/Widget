package com.sn.develop.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sn.develop.utils.PixUtil;

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
        int lineWidthUsed = 0;
        int lineMaxHeight = 0;
        int lineMaxMargin = 0;

        for(int i =0;i<getChildCount();i++){
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int leftMargin = (int) PixUtil.dpToPx(5);
            int rightMargin = (int) PixUtil.dpToPx(5);
            int topMargin = (int) PixUtil.dpToPx(5);
            int bottomMargin = (int) PixUtil.dpToPx(5);
            //将child是MATCH_PARENT单独放起来，因为此时自己的大小没有确定，当自己可能是warp-content时，就无法确定child的宽度
            if(layoutParams.width == LayoutParams.MATCH_PARENT){
                matchParentViews.add(child);
            }
            measureChildWithMargins(child,widthMeasureSpec,0,heightMeasureSpec,0);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //换行设置，

            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED && lineWidthUsed + childWidth+leftMargin > widthSize){
                lineWidthUsed = 0;
                useHeight += lineMaxHeight + lineMaxMargin;
                lineMaxMargin = 0;
                lineMaxHeight = 0;
            }

            //set the child bound rect
            if(childRects.size() < getChildCount()){
                childRects.add(new Rect());
            }
            childRects.get(i).set(lineWidthUsed+leftMargin,useHeight+topMargin,lineWidthUsed+childWidth+leftMargin,useHeight+childHeight+bottomMargin);



            lineWidthUsed = lineWidthUsed + childWidth+leftMargin+rightMargin ;
            useWidth = Math.max(useWidth,lineWidthUsed);
            lineMaxHeight = Math.max(lineMaxHeight,childHeight);
            lineMaxMargin = Math.max(lineMaxMargin,topMargin+bottomMargin);
        }

        int width = useWidth;
        int height = useHeight + lineMaxHeight;
        setMeasuredDimension(width,height);

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
