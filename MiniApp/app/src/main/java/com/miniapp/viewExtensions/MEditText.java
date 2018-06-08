package com.miniapp.viewExtensions;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.miniapp.R;


/**
 * Created by bakshish on 5/19/15.
 */
public class MEditText extends EditText {

    private OnEditorActionListener onEditorActionListener;

    public MEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public MEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setCustomFont(context, attrs);
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

    @Override
    public void setOnEditorActionListener(OnEditorActionListener l) {
        this.onEditorActionListener = l;
        super.setOnEditorActionListener(l);

    }

    public OnEditorActionListener getOnEditorActionListener() {
        return onEditorActionListener;
    }
}
