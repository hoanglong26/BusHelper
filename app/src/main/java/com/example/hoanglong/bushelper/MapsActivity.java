package com.example.hoanglong.bushelper;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.hoanglong.bushelper.POJO.AttributedPhoto;
import com.example.hoanglong.bushelper.POJO.googlemap.BusStop;
import com.example.hoanglong.bushelper.POJO.googlemap.PlaceDetail;
import com.example.hoanglong.bushelper.POJO.googlemap.RouteList;
import com.example.hoanglong.bushelper.POJO.googlemap.Step;
import com.example.hoanglong.bushelper.api.RetrofitUtils;
import com.example.hoanglong.bushelper.api.ServerAPI;
import com.example.hoanglong.bushelper.entities.Favorite;
import com.example.hoanglong.bushelper.entities.TheLocation;
import com.example.hoanglong.bushelper.ormlite.DatabaseManager;
import com.example.hoanglong.bushelper.utils.Utils;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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

//import android.location.TheLocation;

//import static com.example.hoanglong.bushelper.App.busStopListFromGoogle;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    ArrayList<LatLng> markerPoints;
    ArrayList<Polyline> lines = new ArrayList<Polyline>();
    ArrayList<Marker> routeName = new ArrayList<Marker>();
    Polyline line;

    android.location.Location mLocation;
    LocationManager locationManager;

    LatLng myLocation;

    Integer totalDistance = 0;
    Integer totalTime = 0;

    boolean checkMultipleClick = false;

    private ServerAPI serverAPI;

    GoogleApiClient mGoogleApiClient;

    private List<BusStop> busStopListFromGoogle = new ArrayList<>();

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

    @BindView(R.id.btnHome)
    FloatingActionButton btnHome;

    String textInstruction;

    boolean isMultipleMarker = false;

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
        mGoogleApiClient = Utils.getGoogleApiClient();
        DatabaseManager.init(getBaseContext());

        if (!Utils.checkInternetOn(getBaseContext())) {
            Utils.createNetErrorDialog(MapsActivity.this);
            return;
        }

        if (!Utils.isLocationEnabled(MapsActivity.this)) {
            Utils.createLocationErrorDialog(MapsActivity.this);
            return;
        }

//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            btnHome.setVisibility(View.GONE);
//        }

            Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.md_teal_400));

        textInstruction = "";

        btnAdd.setCount(1);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (markerPoints.size() != 0) {
                    btnAdd.increase();
                    isMultipleMarker = true;
                } else {
                    markerPoints.add(myLocation);
                    isMultipleMarker = false;

                }
            }
        });

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        final LatLngBounds bounds = Utils.toBounds(new LatLng(10.793093, 106.653773), 200);
        autocompleteFragment.setBoundsBias(bounds);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                resetMapMarker(place.getLatLng());

                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                if (isMultipleMarker) {
                    for (int i = 0; i <= markerPoints.size() - 2; i++) {
                        from_my_location("transit", markerPoints.get(i), markerPoints.get(i + 1));
                    }
                }
                from_my_location("transit", myLocation, markerPoints.get(0));
            }

            @Override
            public void onError(Status status) {
                Log.i("test", "An error occurred: " + status);
            }
        });


        btnAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);

                btnAdd.setCount(1);
                isMultipleMarker = false;
                markerPoints.clear();
                mMap.clear();
                lines.clear();
                routeName.clear();

                totalTime = 0;
                totalDistance = 0;
                textInstruction = "";
                checkMultipleClick = false;
                showDistanceDuration.setText("Please choose destination");
                tvInstruction.setText(textInstruction);

                MarkerOptions options2 = new MarkerOptions();
                options2.position(myLocation);
                options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                options2.title(getString(R.string.your_location));
                mMap.addMarker(options2);

                return false;
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Utils.checkLocationPermission(this);
//        }

        // Initializing
        markerPoints = new ArrayList<>();

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
        ViewCompat.setTranslationZ(expandableLayout1, 100);

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
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        //Setup to get location
        if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //create circle for my position
        myLocation = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myLocation).title(getString(R.string.your_location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        //Initialize the first stop
        final LatLng busStart;
        final TheLocation aBustop = getIntent().getParcelableExtra("busStop");
        if (aBustop == null) {
            busStart = new LatLng(myLocation.latitude, myLocation.longitude);
            markerPoints.add(busStart);
        } else {
            busStart = new LatLng(aBustop.getLatitude(), aBustop.getLongitude());
            markerPoints.add(busStart);
        }

        serverAPI = RetrofitUtils.get().create(ServerAPI.class);
        serverAPI = retrofit.create(ServerAPI.class);
        serverAPI.getPlaceID(busStart.latitude + "," + busStart.longitude).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                String addr = response.body().getAddr_comp().get(0).getAddress();
                MarkerOptions options1 = new MarkerOptions();
                options1.position(busStart);
                options1.title(addr.substring(0, addr.indexOf(",")));
                if (aBustop == null) {
                    options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                } else {
                    options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                }
                options1.draggable(true);
                mMap.addMarker(options1).setTag(busStart);
            }

            @Override
            public void onFailure(Call<PlaceDetail> call, Throwable t) {

            }
        });

        //Initialize your location
        MarkerOptions options2 = new MarkerOptions();
        options2.position(myLocation);
        options2.title(getString(R.string.your_location));
        options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(options2);

        //Search route from your location to the first stop
        from_my_location("transit", myLocation, markerPoints.get(0));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(final LatLng point) {
                checkMultipleClick = false;

                resetMapMarker(point);
            }
        });


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkMultipleClick) {
                    busStopListFromGoogle.clear();

                    if (isMultipleMarker) {
                        for (int i = 0; i <= markerPoints.size() - 2; i++) {
                            from_my_location("transit", markerPoints.get(i), markerPoints.get(i + 1));
                        }
                    }
                    from_my_location("transit", myLocation, markerPoints.get(0));
                    checkMultipleClick = true;
                }

            }
        });
    }

    private void from_my_location(String type, final LatLng from, final LatLng to) {
        if (myLocation != null) {
            serverAPI = RetrofitUtils.get().create(ServerAPI.class);
            serverAPI = retrofit.create(ServerAPI.class);

            serverAPI.getDistanceDuration("metric", from.latitude + "," + from.longitude, to.latitude + "," + to.longitude, type)
                    .enqueue(new Callback<RouteList>() {
                        @Override
                        public void onResponse(Call<RouteList> call, final Response<RouteList> response) {
                            try {
                                if (response.body().getRoutes().size() == 0) {
//
                                    Toast.makeText(getBaseContext(), R.string.no_route, Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < response.body().getRoutes().size(); i++) {
                                        List<Step> instruction = response.body().getRoutes().get(i).getLegs().get(i).getSteps();

                                        final int finalI = i;

                                        Observable<AttributedPhoto> observable = Observable.create(new Observable.OnSubscribe<AttributedPhoto>() {
                                            @Override
                                            public void call(Subscriber<? super AttributedPhoto> subscriber) {
                                                subscriber.onNext(Utils.getAttributedPhoto(response.body().getGeocodedWaypoints().get(finalI).getPlace_id()));
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
                                                    TheLocation endLocation = new TheLocation();
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
                                                        TheLocation firstLocation = DatabaseManager.getInstance().getAllLocations().get(0);
                                                        DatabaseManager.getInstance().deleteLocation(firstLocation);
                                                    }

                                                }
                                            }
                                        };

                                        observable.subscribeOn(Schedulers.io()).subscribe(observer);

                                        Integer distance = response.body().getRoutes().get(i).getLegs().get(i).getDistance().getValue();
                                        Integer time = response.body().getRoutes().get(i).getLegs().get(i).getDuration().getValue();
                                        totalTime += time;
                                        totalDistance += distance;

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

                                        int labelIndex = 0;
                                        String encodedString = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
                                        List<LatLng> list = Utils.decodePoly(encodedString);
                                        if (from == myLocation) {
                                            line = mMap.addPolyline(new PolylineOptions()
                                                    .addAll(list)
                                                    .width(15)
                                                    .color(Color.parseColor("#C62828"))
                                                    .geodesic(true)
                                            );
                                            lines.add(line);
                                            labelIndex = 1;
                                        } else {
                                            for (LatLng item : markerPoints) {
                                                if (item.longitude == from.longitude && item.latitude == from.latitude) {
                                                    labelIndex = markerPoints.indexOf(item) + 2;
                                                    if (markerPoints.indexOf(item) % 2 == 0) {
                                                        line = mMap.addPolyline(new PolylineOptions()
                                                                .addAll(list)
                                                                .width(15)
                                                                .color(Color.parseColor("#009688"))
                                                                .geodesic(true)
                                                        );
                                                        lines.add(line);

                                                        break;
                                                    } else {
                                                        line = mMap.addPolyline(new PolylineOptions()
                                                                .addAll(list)
                                                                .width(15)
                                                                .color(Color.parseColor("#C62828"))
                                                                .geodesic(true)
                                                        );
                                                        lines.add(line);
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        BusStop aBusStop = new BusStop();
                                        textInstruction += "============== ROUTE " + labelIndex + " ==============\n";
                                        for (Step tmp : instruction) {
                                            if (tmp.getTransit_detail() != null) {
                                                textInstruction += "\t• Get bus number \"" + tmp.getTransit_detail().getLine().getBusName() + "\"\n";
                                                textInstruction += "    - From " + tmp.getTransit_detail().getDeparture_stop().getStopName() + "\n";
                                                textInstruction += "    - To " + tmp.getTransit_detail().getArrival_stop().getStopName() + "\n";

                                                aBusStop.setStopName(tmp.getTransit_detail().getArrival_stop().getStopName());
                                                aBusStop.setLocation(tmp.getTransit_detail().getArrival_stop().getLocation());
                                            } else {
                                                textInstruction += "\t• " + tmp.getInstruction() + "\n";
                                            }
                                        }
                                        textInstruction += "\n";
                                        busStopListFromGoogle.add(aBusStop);

                                        LinearLayout distanceMarkerLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.poly_label, null);
                                        distanceMarkerLayout.setDrawingCacheEnabled(true);
                                        distanceMarkerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                                        distanceMarkerLayout.layout(0, 0, distanceMarkerLayout.getMeasuredWidth(), distanceMarkerLayout.getMeasuredHeight());
                                        distanceMarkerLayout.buildDrawingCache(true);

                                        TextView positionDistance = (TextView) distanceMarkerLayout.findViewById(R.id.positionDistance);
                                        positionDistance.setText("Route " + labelIndex);
                                        GradientDrawable bgShape = (GradientDrawable) positionDistance.getBackground();
                                        bgShape.setColor(line.getColor());


                                        Bitmap flagBitmap = Bitmap.createBitmap(distanceMarkerLayout.getDrawingCache());
                                        distanceMarkerLayout.setDrawingCacheEnabled(false);
                                        BitmapDescriptor flagBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(flagBitmap);

                                        double lat3 = line.getPoints().get((line.getPoints().size() - 1) / 2 + 2).latitude;
                                        double lon3 = line.getPoints().get((line.getPoints().size() - 1) / 2 + 2).longitude;

                                        Marker centerOneMarker = mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(lat3, lon3))
                                                .title("Route name")
                                                .icon(flagBitmapDescriptor));
                                        routeName.add(centerOneMarker);
                                    }
                                }

                                tvInstruction.setText(textInstruction);

//                                for (BusStopDB tmp : ((App) getApplication()).getBusStopListFromDB()) {
//                                    BusStop aBusStop = busStopListFromGoogle.get(busStopListFromGoogle.size() - 1);
//                                    double lat = Double.parseDouble(aBusStop.getLocation().getLatitude());
//                                    double lng = Double.parseDouble(aBusStop.getLocation().getLongitude());
//                                    if (Utils.almostEqual(tmp.getLatitude(), lat, 0.001) && Utils.almostEqual(tmp.getLongitude(), lng, 0.001)) {
//                                        Beacon aBeacon = new Beacon();
//                                        aBeacon.setUuid(tmp.getBeaconList().get(0).getBeacon().getUuid());
//                                        aBeacon.setMajor(tmp.getBeaconList().get(0).getBeacon().getMajor());
//                                        aBeacon.setMinor(tmp.getBeaconList().get(0).getBeacon().getMinor());
//                                        ((App) getApplication()).addBeaconToBeMonitered(aBeacon, tmp);
//                                        Toast.makeText(getBaseContext(), aBusStop.getStopName(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
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
        for (LatLng item : markerPoints) {
            if (item.longitude == marker.getPosition().longitude && item.latitude == marker.getPosition().latitude) {
                Toast.makeText(getBaseContext(), "positon: " + markerPoints.indexOf(item), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        for (Polyline line : lines) {
            line.remove();
        }
        lines.clear();
        for (Marker aMarker : routeName) {
            aMarker.remove();
        }
        routeName.clear();

        for (LatLng item : markerPoints) {
            if (marker.getTag().equals(item)) {
                markerPoints.set(markerPoints.indexOf(item), marker.getPosition());
            }
        }
        totalTime = 0;
        totalDistance = 0;
        textInstruction = "";
        checkMultipleClick = false;
        showDistanceDuration.setText("Distance: 0 km, duration: 0 secs");
        tvInstruction.setText(textInstruction);
    }


    @Override
    public void onBackPressed() {
        if (slidingPaneLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);
                        }
                    }).create().show();
        }
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (!marker.getTitle().equals("Route name")) {
            serverAPI = retrofit.create(ServerAPI.class);

            serverAPI.getPlaceID(marker.getPosition().latitude + "," + marker.getPosition().longitude).enqueue(new Callback<PlaceDetail>() {
                @Override
                public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
//                String place_id = response.body().getAddr_comp().get(0).getPlace_id();
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
                            switch (item.getItemId()) {
                                case R.id.addFavorite:
                                    List locations = DatabaseManager.getInstance().getAllFavorites();
                                    Favorite selectedLocation = new Favorite();
                                    selectedLocation.setLatitude(marker.getPosition().latitude);
                                    selectedLocation.setLongitude(marker.getPosition().longitude);
                                    selectedLocation.setName(addr);


                                    if (!locations.contains(selectedLocation)) {
                                        DatabaseManager.getInstance().addFavorite(selectedLocation);
                                        Toast.makeText(getBaseContext(), R.string.added_to_list, Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getBaseContext(), R.string.already_exist, Toast.LENGTH_SHORT).show();
                                    }

                                    if (DatabaseManager.getInstance().getAllFavorites().size() > 20) {
                                        Favorite firstLocation = DatabaseManager.getInstance().getAllFavorites().get(0);
                                        DatabaseManager.getInstance().deleteFavorite(firstLocation);
                                    }

                                    break;

                                case R.id.removeMarker:
                                    if (!marker.getTitle().equals(getString(R.string.your_location))) {
                                        int removeIndex = 0;
                                        for (LatLng item2 : markerPoints) {
                                            if (item2.longitude == marker.getPosition().longitude && item2.latitude == marker.getPosition().latitude) {
                                                removeIndex = markerPoints.indexOf(item2);
                                            }
                                        }
                                        markerPoints.remove(removeIndex);

                                        for (Polyline line : lines) {
                                            line.remove();
                                        }
                                        lines.clear();
                                        for (Marker aMarker : routeName) {
                                            aMarker.remove();
                                        }
                                        routeName.clear();

                                        totalTime = 0;
                                        totalDistance = 0;
                                        textInstruction = "";
                                        checkMultipleClick = false;
                                        showDistanceDuration.setText("Distance: 0 km, duration: 0 secs");
                                        tvInstruction.setText(textInstruction);

                                        marker.remove();
                                        Toast.makeText(getBaseContext(), R.string.marker_removed, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getBaseContext(), R.string.cannot_remove_marker, Toast.LENGTH_SHORT).show();

                                    }
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
        }
        return false;
    }

    private void resetMapMarker(final LatLng point) {
        slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        // clearing map and generating new marker points if user clicks on map
        if (markerPoints.size() >= 1) {
            mMap.clear();
            lines.clear();
            routeName.clear();

            LatLng tmp;
            if (markerPoints.size() > 1) {
                tmp = markerPoints.get(1);
            } else {
                tmp = markerPoints.get(0);
            }

            if (!isMultipleMarker) {
                markerPoints.clear();
                markerPoints = new ArrayList<>();
                markerPoints.add(tmp);
            } else {
                if (markerPoints.size() >= btnAdd.getCount()) {
                    markerPoints.remove(0);
                }
            }

            if (isMultipleMarker) {
                for (final LatLng item : markerPoints) {
                    serverAPI.getPlaceID(item.latitude + "," + item.longitude).enqueue(new Callback<PlaceDetail>() {
                        @Override
                        public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                            String addr = response.body().getAddr_comp().get(0).getAddress();
                            MarkerOptions options1 = new MarkerOptions();
                            options1.position(item);
                            options1.title(addr.substring(0, addr.indexOf(",")));
                            options1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                            options1.draggable(true);
                            mMap.addMarker(options1).setTag(item);

                            MarkerOptions options2 = new MarkerOptions();
                            options2.position(myLocation);
                            options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            options2.title(getString(R.string.your_location));
                            mMap.addMarker(options2);
                        }

                        @Override
                        public void onFailure(Call<PlaceDetail> call, Throwable t) {

                        }
                    });
                }
            }

            MarkerOptions options2 = new MarkerOptions();
            options2.position(myLocation);
            options2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            options2.title(getString(R.string.your_location));
            mMap.addMarker(options2);

            totalTime = 0;
            totalDistance = 0;
            textInstruction = "";
        }
        showDistanceDuration.setText("Distance: 0 km, duration: 0 secs");
        tvInstruction.setText(textInstruction);


        if (isMultipleMarker) {
            // Adding new item to the ArrayList
            markerPoints.add(point);
        } else {
            markerPoints.set(0, point);
        }

        serverAPI.getPlaceID(point.latitude + "," + point.longitude).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                String addr = response.body().getAddr_comp().get(0).getAddress();

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();
                // Setting the position of the marker
                options.position(point);
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                options.title(addr.substring(0, addr.indexOf(",")));
                options.draggable(true);
                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options).setTag(point);
            }

            @Override
            public void onFailure(Call<PlaceDetail> call, Throwable t) {

            }
        });
    }

}
