package com.example.hoanglong.bushelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.hoanglong.bushelper.BuildConfig;
import com.example.hoanglong.bushelper.R;
import com.example.hoanglong.bushelper.utils.Utils;
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
    private static boolean compareBitmap(Bitmap b1, Bitmap b2) {
        try {
            if (b1.getWidth() == b2.getWidth() && b1.getHeight() == b2.getHeight()) {
                int[] pixels1 = new int[b1.getWidth() * b1.getHeight()];
                int[] pixels2 = new int[b2.getWidth() * b2.getHeight()];
                b1.getPixels(pixels1, 0, b1.getWidth(), 0, 0, b1.getWidth(), b1.getHeight());
                b2.getPixels(pixels2, 0, b2.getWidth(), 0, 0, b2.getWidth(), b2.getHeight());
                return Arrays.equals(pixels1, pixels2);
            } else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    @Test
    public void stringToBitMapTrue() throws Exception {
//        Activity activity = Robolectric.setupActivity(MainActivity.class);//
        Bitmap expectedThumbnail = BitmapFactory.decodeResource(RuntimeEnvironment.application.getResources(), R.drawable.thumbnail);
        String tmp = Utils.BitMapToString(expectedThumbnail);
        assertEquals(true, compareBitmap(expectedThumbnail,Utils.StringToBitMap(tmp)));
    }

    @Test
    public void stringToBitMapNull() throws Exception {
//        Activity activity = Robolectric.setupActivity(MainActivity.class);//
        Bitmap expectedThumbnail = null;
//        String tmp = Utils.BitMapToString(expectedThumbnail);
        assertEquals(false, compareBitmap(expectedThumbnail,Utils.StringToBitMap(null)));
    }

    @Test
    public void toBoundsTrue() throws Exception {
        LatLng southwest = new LatLng(10.898201353892782, 106.29816832441604);
        LatLng northeast = new LatLng(10.901798635234098, 106.30183169772972);

        LatLngBounds sampleBounds = new LatLngBounds(southwest, northeast);
        assertEquals(sampleBounds, Utils.toBounds(new LatLng(10.9, 106.3), 200));
    }

    @Test
    public void toBoundsRadiusZero() throws Exception {
        LatLngBounds sampleBounds = null;
        assertEquals(sampleBounds, Utils.toBounds(new LatLng(10.9, 106.3), 0));
    }

    @Test
    public void toBoundsRadiusNegative() throws Exception {
        LatLngBounds sampleBounds = null;
        assertEquals(sampleBounds, Utils.toBounds(new LatLng(10.9, 106.3), -5));
    }

    @Test
    public void toBoundsLatLongNull() throws Exception {
        LatLngBounds sampleBounds = null;
        assertEquals(sampleBounds, Utils.toBounds(null, 50));
    }

    @Test
    public void decodePolyTrue() throws Exception {
//        LatLngBounds sampleBounds = null;
//        assertEquals(sampleBounds, Utils.toBounds(null, 50));
    }

}