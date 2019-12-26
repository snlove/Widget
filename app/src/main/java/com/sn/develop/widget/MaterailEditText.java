package com.sn.develop.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.sn.develop.R;
import com.sn.develop.utils.PixUtil;

import java.util.Objects;

/**
 * 兼容材料设计浮动label
 */
public class MaterailEditText extends AppCompatEditText {

    private Paint paint;
    private int TEXT_HINT_PADDING = 0;
    private Paint.FontMetrics fontMetrics;
    private int configPaddingTop = 0;
    private int textTop = 0;
    private int textLeft = 0;
    private float hintAlpha = 1;
    private float hintEnY = 0;
    private float hintStartY = 0;
    private float hintY = 0;
    private String content = "";
    private boolean userFloatLabel = true;
    private ObjectAnimator animator = null;
    public MaterailEditText(Context context) {
        super(context);
    }

    public MaterailEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.MaterailEditText);
        userFloatLabel = typedArray.getBoolean(R.styleable.MaterailEditText_userFloatLabel, true);
        init();
        initListener();
        typedArray.recycle();

    }

    public MaterailEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){

        //config editText
        configPaddingTop = getPaddingTop();
        textTop = getBackground().getBounds().top;
        textLeft = getBackground().getBounds().left;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(PixUtil.dpToPx(16));
        paint.setAlpha(0);
        fontMetrics = new Paint.FontMetrics();
        paint.getFontMetrics(fontMetrics);
        hintEnY = textTop - fontMetrics.ascent + configPaddingTop;
        hintStartY = textTop - fontMetrics.ascent + configPaddingTop + paint.getFontSpacing() - 10;
        hintY = hintEnY;
        TEXT_HINT_PADDING = (int) (paint.getFontSpacing() - fontMetrics.ascent);
        if(userFloatLabel) {
            setPadding(getPaddingLeft(), textTop + configPaddingTop + TEXT_HINT_PADDING, getPaddingRight(), getPaddingBottom());
        }
    }


    private void  changeTextPadding(){
        int padding = textTop;
        if(userFloatLabel){
            padding = textTop + configPaddingTop +  TEXT_HINT_PADDING;
        }
        setPadding(getPaddingLeft(),padding,getPaddingRight(),getPaddingBottom());
    }

    private void  initListener(){
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                content = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(userFloatLabel){
                    //无到有,显示顶部label
                    if( content.length() == 0 && !TextUtils.isEmpty(s)){
                        getHintAnimator().start();

                    } else if( content.length() > 0 && TextUtils.isEmpty(s)){   //有到无
                        getHintAnimator().reverse();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private ObjectAnimator getHintAnimator() {
        if (animator == null){
            PropertyValuesHolder hintAlphaHolder = PropertyValuesHolder.ofFloat("hintAlpha", 0, 1);
            PropertyValuesHolder hintYHolder = PropertyValuesHolder.ofFloat("hintY", hintStartY, hintEnY);
            animator = ObjectAnimator.ofPropertyValuesHolder(MaterailEditText.this, hintAlphaHolder, hintYHolder);
            animator.setDuration(100);
        }

        return animator;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(userFloatLabel){
            canvas.drawText(getHint().toString(),getPaddingStart(),hintY,paint);
        }
    }

    public float getHintAlpha() {
        return hintAlpha;
    }

    public void setHintAlpha(float hintAlpha) {
        this.hintAlpha = hintAlpha;
        paint.setAlpha((int) (255*hintAlpha));
        invalidate();
    }

    public float getHintY() {
        return hintY;
    }

    public void setHintY(float hintY) {
        this.hintY = hintY;
        invalidate();
    }

    public void setUserFloatLabel(boolean userFloatLabel) {
        boolean before = this.userFloatLabel;
        this.userFloatLabel = userFloatLabel;
        if(before != userFloatLabel){
            changeTextPadding();
        }
    }
}
