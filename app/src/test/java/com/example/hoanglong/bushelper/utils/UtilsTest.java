package com.example.hoanglong.bushelper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.hoanglong.bushelper.BuildConfig;
import com.example.hoanglong.bushelper.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;


import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by hoanglong on 01-Jul-17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class UtilsTest {

    @Test
    public void stringToBitMap() throws Exception {
//        Activity activity = Robolectric.setupActivity(MainActivity.class);//
//        String manifestProperty = System.getProperty("android.manifest");
//        String resourcesProperty = System.getProperty("android.resources");
//        String assetsProperty = System.getProperty("android.assets");
        Bitmap thumbnail = BitmapFactory.decodeResource(RuntimeEnvironment.application.getResources(), R.drawable.thumbnail);
        String tmp = Utils.BitMapToString(thumbnail);
        assertEquals(true, compareBitmap(Utils.StringToBitMap(tmp),thumbnail));

    }

    private static boolean compareBitmap(Bitmap b1, Bitmap b2) {
        if (b1.getWidth() == b2.getWidth() && b1.getHeight() == b2.getHeight()) {
            int[] pixels1 = new int[b1.getWidth() * b1.getHeight()];
            int[] pixels2 = new int[b2.getWidth() * b2.getHeight()];
            b1.getPixels(pixels1, 0, b1.getWidth(), 0, 0, b1.getWidth(), b1.getHeight());
            b2.getPixels(pixels2, 0, b2.getWidth(), 0, 0, b2.getWidth(), b2.getHeight());
            return Arrays.equals(pixels1, pixels2);
        } else {
            return false;
        }
    }

    @Test
    public void bitMapToString() throws Exception {

    }

    @Test
    public void initialGoogleApiClient() throws Exception {

    }

    @Test
    public void toBounds() throws Exception {

        LatLng southwest = new LatLng(10.898201353892782, 106.29816832441604);
        LatLng northeast = new LatLng(10.901798635234098, 106.30183169772972);

        LatLngBounds sampleBounds = new LatLngBounds(southwest, northeast);

        assertEquals(sampleBounds, Utils.toBounds(new LatLng(10.9, 106.3), 200));

    }

    @Test
    public void getAttributedPhoto() throws Exception {

    }

}