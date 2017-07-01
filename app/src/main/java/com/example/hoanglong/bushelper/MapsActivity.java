package com.example.hoanglong.bushelper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.hoanglong.bushelper.POJO.PlaceDetail;
import com.example.hoanglong.bushelper.POJO.RouteList;
import com.example.hoanglong.bushelper.POJO.Step;
import com.example.hoanglong.bushelper.api.ServerAPI;
import com.example.hoanglong.bushelper.dagger2.App;
import com.example.hoanglong.bushelper.model.AttributedPhoto;
import com.example.hoanglong.bushelper.model.Favorite;
import com.example.hoanglong.bushelper.model.Location;
import com.example.hoanglong.bushelper.ormlite.DatabaseManager;
import com.example.hoanglong.bushelper.utils.Utils;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    LatLng origin;
    LatLng dest;
    ArrayList<LatLng> MarkerPoints;
    //    TextView showDistanceDuration;
    Polyline line;

    Polyline line2;

    android.location.Location mLocation;
    LocationManager locationManager;

    LatLng myLocation;

    Integer totalDistance = 0;
    Integer totalTime = 0;

    boolean checkMultipleClick = false;

    private ServerAPI serverAPI;

    GoogleApiClient mGoogleApiClient;


    @Inject
    Retrofit retrofit;

    @BindView(R.id.expandableLayout1)
    ExpandableRelativeLayout expandableLayout1;

    @BindView(R.id.expandableButton1)
    Button showDistanceDuration;

    @BindView(R.id.btnGo)
    FloatingActionButton btnGo;

    @BindView(R.id.tvInstruction)
    TextView tvInstruction;

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingPaneLayout;

    @BindView(R.id.btnAdd)
    CounterFab btnAdd;

    String textInstruction;

    boolean isMutipleMarker = false;


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        ((App) getApplication()).getNetComponent().inject(this);

        textInstruction = "";

        btnAdd.setCount(1);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnAdd.getCount() == 1) {
                    btnAdd.setCount(2);
                    isMutipleMarker = true;
                } else {
                    btnAdd.setCount(1);
                    isMutipleMarker = false;
                }
            }
        });

//        Utils.initialGoogleApiClient(this);
        mGoogleApiClient = Utils.getGoogleApiClient();

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_teal_400));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utils.checkLocationPermission(this);
        }

        // Initializing
        MarkerPoints = new ArrayList<>();

        //show error dialog if Google Play Services not available
        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        expandableLayout1.toggle();


    }

    @OnClick(R.id.expandableButton1)
    public void onClick1() {
        expandableLayout1.toggle();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);


        //Setup to get location
        if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        myLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myLocation).title("Your location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


        //Initialize the first stop
        final Location aBustop = getIntent().getParcelableExtra("busStop");
        LatLng busStart = new LatLng(aBustop.getLatitude(), aBustop.getLongitude());
        MarkerPoints.add(busStart);
        MarkerOptions options1 = new MarkerOptions();
        options1.position(busStart);
        options1.title("Final stop");
        options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        options1.draggable(true);
        mMap.addMarker(options1);

        origin = busStart;
        dest = busStart;

        //Initialize your location
        MarkerOptions options2 = new MarkerOptions();
        options2.position(myLocation);
        options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(options2);

        //Search route from your location to the first stop
        from_my_location("transit", myLocation, origin);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                checkMultipleClick = false;
                slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);


                // clearing map and generating new marker points if user clicks on map
                if (MarkerPoints.size() >= 1) {
                    mMap.clear();

                    LatLng tmp;
                    if (MarkerPoints.size() > 1) {
                        tmp = MarkerPoints.get(1);
                    } else {
                        tmp = MarkerPoints.get(0);
                    }
                    MarkerPoints.clear();
                    MarkerPoints = new ArrayList<>();

                    if (isMutipleMarker) {
                        MarkerPoints.add(tmp);
                        MarkerOptions options1 = new MarkerOptions();
                        options1.position(tmp);
                        options1.title("Middle stop");
                        options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        options1.draggable(true);
                        mMap.addMarker(options1);
                    }

                    MarkerOptions options2 = new MarkerOptions();
                    options2.position(myLocation);
                    options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    mMap.addMarker(options2);

                    totalTime = 0;
                    totalDistance = 0;
                    textInstruction = "";

                }

                showDistanceDuration.setText("Please choose destination");

                // Adding new item to the ArrayList
                MarkerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                options.title("Final stop");
                options.draggable(true);

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (isMutipleMarker) {
                    if (MarkerPoints.size() >= 2) {
                        origin = MarkerPoints.get(0);
                        dest = MarkerPoints.get(1);
                    } else {
                        origin = myLocation;
                        dest = MarkerPoints.get(0);
                    }
                } else {
                    origin = myLocation;
                    dest = MarkerPoints.get(0);
                }


            }
        });


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkMultipleClick) {
                    from_my_location("transit", myLocation, origin);
                    from_my_location("transit", origin, dest);
                    checkMultipleClick = true;


                }

            }
        });
    }

    private void from_my_location(String type, final LatLng from, LatLng to) {

        if (myLocation != null) {
//            serverAPI = RetrofitUtils.get().create(ServerAPI.class);

            serverAPI = retrofit.create(ServerAPI.class);

            serverAPI.getDistanceDuration("metric", from.latitude + "," + from.longitude, to.latitude + "," + to.longitude, type)
                    .enqueue(new Callback<RouteList>() {
                        @Override
                        public void onResponse(Call<RouteList> call, final Response<RouteList> response) {
                            try {
                                //Remove previous line from middle stop to final stop from map
                                if (line != null && from != myLocation) {
                                    line.remove();
                                }


                                if (response.body().getRoutes().size() == 0) {
                                    origin = myLocation;
                                    dest = myLocation;
                                    if (MarkerPoints.size() == 2) {
//                                        origin = MarkerPoints.get(1, origin);
                                        dest = origin;
                                        MarkerPoints.set(1, dest);

                                    } else {
                                        MarkerPoints.set(0, origin);

                                    }
                                    Toast.makeText(getBaseContext(), "No route", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                                        List<Step> instruction = response.body().getRoutes().get(i).getLegs().get(i).getSteps();


                                        final int finalI = i;

                                        Observable<AttributedPhoto> observable = Observable.create(new Observable.OnSubscribe<AttributedPhoto>() {
                                            @Override
                                            public void call(Subscriber<? super AttributedPhoto> subscriber) {

                                                subscriber.onNext(Utils.getAttributedPhoto(mGoogleApiClient, response.body().getGeocodedWaypoints().get(finalI).getPlace_id()));
                                                subscriber.onCompleted();


                                            }
                                        });


                                        Observer<AttributedPhoto> observer = new Observer<AttributedPhoto>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onNext(AttributedPhoto attributedPhoto) {
                                                if (attributedPhoto != null) {
                                                    Location endLocation = new Location();
                                                    endLocation.setName(response.body().getRoutes().get(finalI).getLegs().get(finalI).getEnd_address());
                                                    endLocation.setLatitude(Double.parseDouble(response.body().getRoutes().get(finalI).getLegs().get(finalI).getEnd_location().getLatitude()));
                                                    endLocation.setLongitude(Double.parseDouble(response.body().getRoutes().get(finalI).getLegs().get(finalI).getEnd_location().getLongitude()));

                                                    if (attributedPhoto.bitmap != null) {
                                                        endLocation.setPlace_image(Utils.BitMapToString(attributedPhoto.bitmap));
                                                    } else {
                                                        endLocation.setPlace_image(null);
                                                    }

                                                    List locations = DatabaseManager.getInstance().getAllLocations();
                                                    if (!locations.contains(endLocation)) {
                                                        DatabaseManager.getInstance().addLocation(endLocation);
                                                    }

                                                    if (DatabaseManager.getInstance().getAllLocations().size() > 10) {
                                                        Location firstLocation = DatabaseManager.getInstance().getAllLocations().get(0);
                                                        DatabaseManager.getInstance().deleteLocation(firstLocation);
                                                    }

                                                }
                                            }
                                        };

                                        observable.subscribeOn(Schedulers.io()).subscribe(observer);


                                        for (Step tmp : instruction) {
                                            if (tmp.getTransit_detail() != null) {

                                                textInstruction += "\t• Get bus number \"" + tmp.getTransit_detail().getLine().getBusName() + "\"\n";
                                                textInstruction += "    - From " + tmp.getTransit_detail().getDeparture_stop().getStopName() + "\n";
                                                textInstruction += "    - To " + tmp.getTransit_detail().getArrival_stop().getStopName() + "\n";

                                            } else {
                                                textInstruction += "\t• " + tmp.getInstruction() + "\n";
                                            }


                                        }
//                                    String instruction="";
                                        Integer distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getValue();
                                        Integer time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getValue();
                                        totalTime += time;
                                        totalDistance += distance;
//                                    textInstruction += instruction;


                                        if (totalDistance / 1000 <= 0 && totalTime / 60 <= 0) {
                                            showDistanceDuration.setText("Distance: " + totalDistance + " m, Duration: " + totalTime + " secs");
                                        }

                                        if (totalDistance / 1000 <= 0) {
                                            showDistanceDuration.setText("Distance: " + totalDistance + " m, Duration: " + totalTime / 60 + " mins");
                                        }

                                        if (totalTime / 60 <= 0) {
                                            showDistanceDuration.setText("Distance: " + totalDistance / 1000 + " km, Duration: " + totalTime + " secs");
                                        }


                                        if (totalDistance / 1000 > 0 && totalTime / 60 > 0) {
                                            showDistanceDuration.setText("Distance: " + totalDistance / 1000 + " km, Duration: " + totalTime / 60 + " mins");
                                        }

                                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                                        List<LatLng> list = decodePoly(encodedString);

                                        if (from != myLocation) {
                                            line = mMap.addPolyline(new PolylineOptions()
                                                    .addAll(list)
                                                    .width(15)
                                                    .color(Color.parseColor("#009688"))
                                                    .geodesic(true)
                                            );
                                        } else {
                                            line2 = mMap.addPolyline(new PolylineOptions()
                                                    .addAll(list)
                                                    .width(15)
                                                    .color(Color.parseColor("#C62828"))
                                                    .geodesic(true)
                                            );
                                        }

                                    }
                                }


                                tvInstruction.setText(textInstruction);

                            } catch (Exception e) {
                                Log.d("onResponse", "There is an error");
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<RouteList> call, Throwable t) {
                            Log.d("onFailure", t.toString());
                        }
                    });
        }

    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    // Checking if Google Play Services Available or not
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (MarkerPoints.size() == 2) {
            if (marker.getTitle().equals("Middle stop")) {
                origin = marker.getPosition();
                MarkerPoints.set(0, origin);
            }

            if (marker.getTitle().equals("Final stop")) {
                dest = marker.getPosition();
                MarkerPoints.set(1, dest);

            }

            if (line != null) {
                line.remove();

            }

            if (line2 != null) {
                line2.remove();
            }
        } else {
            if (marker.getTitle().equals("Final stop")) {
                dest = marker.getPosition();
                origin = myLocation;
                MarkerPoints.set(0, dest);

            }

            if (line2 != null) {
                line2.remove();
            }
        }
        totalTime = 0;
        totalDistance = 0;
        textInstruction = "";
        checkMultipleClick = false;

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        serverAPI = retrofit.create(ServerAPI.class);


        serverAPI.getPlaceID(marker.getPosition().latitude+","+marker.getPosition().longitude).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                String place_id = response.body().getAddr_comp().get(0).getPlace_id();
                final String addr = response.body().getAddr_comp().get(0).getAddress();


                PopupMenu popup = new PopupMenu(MapsActivity.this, expandableLayout1);

                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.getMenu().getItem(0).setTitle(addr);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.addFavorite:
                                List locations = DatabaseManager.getInstance().getAllFavorites();
                                Favorite selectedLocation = new Favorite();
                                selectedLocation.setLatitude(marker.getPosition().latitude);
                                selectedLocation.setLongitude(marker.getPosition().longitude);
                                selectedLocation.setName(addr);


                                if (!locations.contains(selectedLocation)) {
                                    DatabaseManager.getInstance().addFavorite(selectedLocation);
                                    Toast.makeText(getBaseContext(),"Added", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(getBaseContext(),"This place already in the list", Toast.LENGTH_SHORT).show();
                                }

                                if (DatabaseManager.getInstance().getAllFavorites().size() > 20) {
                                    Favorite firstLocation = DatabaseManager.getInstance().getAllFavorites().get(0);
                                    DatabaseManager.getInstance().deleteFavorite(firstLocation);
                                }

                                break;

                            case R.id.removeMarker:
                                if(!marker.getTitle().equals("Your location")) {
                                    Toast.makeText(getBaseContext(), "Marker has been removed", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getBaseContext(), "Cannot remove this marker", Toast.LENGTH_SHORT).show();

                                }
                                if (MarkerPoints.size() == 2) {
                                    if (marker.getTitle().equals("Middle stop")) {
                                        origin = myLocation;
                                        MarkerPoints.set(0, origin);
                                    }

                                    if (marker.getTitle().equals("Final stop")) {
//                                        dest = myLocation;
//                                        MarkerPoints.set(1, dest);

                                        dest = MarkerPoints.get(0);
                                        origin = myLocation;
                                        MarkerPoints.remove(1);
                                        MarkerPoints.set(0,dest);

                                        mMap.clear();

                                        MarkerOptions options = new MarkerOptions();
                                        options.position(myLocation);
                                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                        mMap.addMarker(options);


                                        MarkerOptions options2 = new MarkerOptions();
                                        options2.position(dest);
                                        options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                                        mMap.addMarker(options2);


                                    }

                                    if (line != null) {
                                        line.remove();

                                    }

                                    if (line2 != null) {
                                        line2.remove();
                                    }

                                    btnAdd.setCount(1);
                                    isMutipleMarker = false;
                                } else {
                                    if (marker.getTitle().equals("Final stop")) {
                                        dest = myLocation;
                                        origin = myLocation;
                                        MarkerPoints.set(0, dest);

                                    }

                                    if (line2 != null) {
                                        line2.remove();
                                    }
                                }
                                totalTime = 0;
                                totalDistance = 0;
                                textInstruction = "";
                                checkMultipleClick = false;
                                marker.remove();
                                break;
                        }

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }

            @Override
            public void onFailure(Call<PlaceDetail> call, Throwable t) {

            }
        });






        return false;
    }


}
