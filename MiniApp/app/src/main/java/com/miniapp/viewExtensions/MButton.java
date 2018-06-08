package com.miniapp.viewExtensions;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

import com.miniapp.R;

public class MButton extends Button {


    public MButton(Context context) {
        super(context);

    }

    public MButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFontAndBgColor();
//        setCustomFont(context, attrs);
    }

    public MButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFontAndBgColor();
//        setCustomFont(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setFontAndBgColor();
//        setCustomFont(context, attrs);
    }

    private void setFontAndBgColor() {
        this.setTextColor(getResources().getColor(R.color.app_button_color));
        this.setBackground(getResources().getDrawable(R.drawable.button_bg));
    }
    private void setCustomFont(Context context, AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MTextView);
        String fontPath = array.getString(R.styleable.MTextView_custFont);
        setCustomFont(context, fontPath);
        array.recycle();
    }

    public boolean setCustomFont(Context context, String fontPath){
        Typeface typeface = FontCache.getTypeface(context, fontPath);
        setTypeface(typeface);
        return true;
    }


}
