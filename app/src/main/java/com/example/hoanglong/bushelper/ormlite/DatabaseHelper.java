package com.example.hoanglong.bushelper.ormlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hoanglong.bushelper.model.Favorite;
import com.example.hoanglong.bushelper.model.Location;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoanglong on 21-Mar-17.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "BusHelper.sqlite";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<Location, Integer> locationDao = null;

    private Dao<Favorite, Integer> favoriteDao = null;


    public DatabaseHelper(Context context) {


        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Location.class);
            TableUtils.createTable(connectionSource, Favorite.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            List<String> allSql = new ArrayList<String>();
            switch(oldVersion)
            {
                case 1:
                    //allSql.add("alter table AdData add column `new_col` VARCHAR");
                    //allSql.add("alter table AdData add column `new_col2` VARCHAR");
            }
            for (String sql : allSql) {
                db.execSQL(sql);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        }

    }

    public Dao<Location, Integer> getLocationDao() {
        if (null == locationDao) {
            try {
                locationDao = getDao(Location.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return locationDao;
    }


    public Dao<Favorite, Integer> getFavoriteDao() {
        if (null == favoriteDao) {
            try {
                favoriteDao = getDao(Favorite.class);
            }catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return favoriteDao;
    }




}
