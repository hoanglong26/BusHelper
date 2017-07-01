package com.example.hoanglong.bushelper.api;


import com.example.hoanglong.bushelper.POJO.PlaceDetail;
import com.example.hoanglong.bushelper.POJO.RouteList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hoanglong on 10/08/2016.
 */

public interface ServerAPI {
    @GET("api/directions/json?key=AIzaSyAl_-UNqx3oY3vVPB21ieVHaGeenes2Hb8&transit_mode=bus&transit_routing_preference=less_walking")
    Call<RouteList> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);


    @GET("api/geocode/json?sensor=true&key=AIzaSyAl_-UNqx3oY3vVPB21ieVHaGeenes2Hb8")
    Call<PlaceDetail> getPlaceID(@Query("latlng") String latlng);


}

