package com.example.hoanglong.bushelper.POJO;

import android.graphics.Bitmap;

/**
 * Created by hoanglong on 29-Jun-17.
 */

public class AttributedPhoto {

    public CharSequence attribution;

    public Bitmap bitmap;

    public AttributedPhoto(CharSequence attribution, Bitmap bitmap) {
        this.attribution = attribution;
        this.bitmap = bitmap;
    }

    public CharSequence getAttribution() {
        return attribution;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setAttribution(CharSequence attribution) {
        this.attribution = attribution;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
