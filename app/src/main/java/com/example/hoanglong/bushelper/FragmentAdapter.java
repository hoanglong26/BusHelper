package com.example.hoanglong.bushelper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by hoanglong on 06-Dec-16.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    String userRole;

    public FragmentAdapter(FragmentManager fm, String userRole) {
        super(fm);
        this.userRole = userRole;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HistoryFragment.newInstance("");
            case 1:
                return FavoriteFragment.newInstance("");
//            case 2:
//                return RelativesFragment.newInstance("");
            default:
                return HistoryFragment.newInstance("");

        }

    }


//    @Override
//    public int getCount() {
//        if (userRole.equals("5")) {
//            return 2;
//        }
//        return 3;
//    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }


    public static class OpenEvent {
        public final int position;
        public final double lat;
        public final double lng;

        public OpenEvent(int position, double lat, double lng) {
            this.position = position;
            this.lat = lat;
            this.lng = lng;
        }
    }
}
