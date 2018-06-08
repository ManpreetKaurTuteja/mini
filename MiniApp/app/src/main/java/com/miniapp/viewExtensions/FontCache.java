package com.miniapp.viewExtensions;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by bakshish on 5/14/15.
 */
public class FontCache {

    private static Hashtable<String, Typeface> fontCache = new Hashtable<>();

    public static Typeface getTypeface(Context context, String fontPath){
        Typeface typeface = fontCache.get(fontPath);
        if(typeface == null){
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
            fontCache.put(fontPath, typeface);
        }
        return typeface;
    }

}
