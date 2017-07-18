package com.example.hoanglong.bushelper.ormlite;

import android.content.Context;
import android.database.SQLException;

import com.example.hoanglong.bushelper.entities.Favorite;
import com.example.hoanglong.bushelper.entities.Location;

import java.util.List;

/**
 * Created by hoanglong on 21-Mar-17.
 */

public class DatabaseManager {

    static private DatabaseManager instance;

    static public void init(Context ctx) {
        if (null==instance) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseHelper helper;
    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    public List<Location> getAllLocations() {
        List<Location> locations = null;
        try {
            locations = getHelper().getLocationDao().queryForAll();
        }
         catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }


    public void addLocation(Location l) {
        try {
            getHelper().getLocationDao().create(l);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLocation(Location subject) {
        try {
            getHelper().getLocationDao().delete(subject);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllLocations() {
        try {
            getHelper().getLocationDao().delete(getAllLocations());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Favorite> getAllFavorites() {
        List<Favorite> locations = null;
        try {
            locations = getHelper().getFavoriteDao().queryForAll();
        }
        catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }


    public void addFavorite(Favorite l) {
        try {
            getHelper().getFavoriteDao().create(l);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFavorite(Favorite subject) {
        try {
            getHelper().getFavoriteDao().delete(subject);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

//    public List<Subject> getSubjectWithSubjectArea(String wishListId) {
//        List<Subject> wishList = null;
//        try {
//            wishList = getHelper().getSubjectDao().queryForEq("subject_area", wishListId);
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//        return wishList;
//    }

//    public SubjectDateTime newSubjectDateTimeItem() {
//        SubjectDateTime subjectDateTimeItem = new SubjectDateTime();
//        try {
//            getHelper().getSubjectDateTimeDao().create(subjectDateTimeItem);
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//        return subjectDateTimeItem;
//    }
//
//    public void updateSubjectDateTimeItem(SubjectDateTime item) {
//        try {
//            getHelper().getSubjectDateTimeDao().update(item);
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//    }



}
