package com.example.hoanglong.bushelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hoanglong.bushelper.POJO.EmailInfo;
import com.example.hoanglong.bushelper.POJO.PlacePrediction;
import com.example.hoanglong.bushelper.entities.TheLocation;
import com.example.hoanglong.bushelper.ormlite.DatabaseManager;
import com.example.hoanglong.bushelper.utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "test";

    private CursorAdapter mAdapter;

    GoogleApiClient mGoogleApiClient;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.btnMylocation)
    FloatingActionButton myLocationBtn;

    @BindView(R.id.searchBtn)
    Button searchBtn;


    FragmentPagerAdapter adapterViewPager;

    TheLocation result = null;

    android.location.Location mLocation;
    LocationManager locationManager;

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().setElevation(0);
//        DatabaseManager.init(getBaseContext());

        Utils.initialGoogleApiClient(this);
        mGoogleApiClient = Utils.getGoogleApiClient();


        final String[] from = new String[]{"busStopName"};
        final int[] to = new int[]{android.R.id.text1};
        mAdapter = new SimpleCursorAdapter(getBaseContext(),
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


        final SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.setSuggestionsAdapter(mAdapter);

        searchView.setQueryHint("Search location");
        searchView.setSuggestionsAdapter(mAdapter);


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.onActionViewExpanded();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.onActionViewCollapsed();
                if (!Utils.checkInternetOn(getBaseContext())) {
                    Utils.createNetErrorDialog(MainActivity.this);
                }

                if (!Utils.isLocationEnabled(MainActivity.this)) {
                    Utils.createLocationErrorDialog(MainActivity.this);
                }

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Utils.checkLocationPermission(MainActivity.this)) {
                        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                        if (result != null) {
                            Toast.makeText(getBaseContext(), "Starting Map", Toast.LENGTH_SHORT).show();

                            intent.putExtra("busStop", result);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getBaseContext(), R.string.please_choose_location, Toast.LENGTH_SHORT).show();

                        }
                    }
                }else{
                    Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                    if (result != null) {
                        Toast.makeText(getBaseContext(), "Starting Map", Toast.LENGTH_SHORT).show();

                        intent.putExtra("busStop", result);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), R.string.please_choose_location, Toast.LENGTH_SHORT).show();

                    }
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                populateAdapter(newText);

                return false;
            }
        });


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.onActionViewCollapsed();
                if (!Utils.checkInternetOn(getBaseContext())) {
                    Utils.createNetErrorDialog(MainActivity.this);
                }


                if (!Utils.isLocationEnabled(MainActivity.this)) {
                    Utils.createLocationErrorDialog(MainActivity.this);
                }
                Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                if (result != null) {
                    Toast.makeText(getBaseContext(), "Starting Map", Toast.LENGTH_SHORT).show();

                    intent.putExtra("busStop", result);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), R.string.please_choose_location, Toast.LENGTH_SHORT).show();

                }
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setup to get location
                if (!Utils.checkInternetOn(getBaseContext())) {
                    Utils.createNetErrorDialog(MainActivity.this);
                }

                if (!Utils.isLocationEnabled(MainActivity.this)) {
                    Utils.createLocationErrorDialog(MainActivity.this);
                }


                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Utils.checkLocationPermission(MainActivity.this)) {

//                        mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        result = new TheLocation(0, "My location", mLocation.getLatitude(), mLocation.getLongitude());
                        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
//                        intent.putExtra("busStop", result);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    Intent intent = new Intent(getBaseContext(), MapsActivity.class);
                    startActivity(intent);
                    finish();
                }

//                if (ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {

                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = searchView.getSuggestionsAdapter().getCursor();
                cursor.moveToPosition(position);
                String suggestion = cursor.getString(1);//0 is the index of col containing suggestion name.
                result = new TheLocation(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        Double.parseDouble(cursor.getString(2)),
                        Double.parseDouble(cursor.getString(3)));
                Toast.makeText(getBaseContext(), suggestion, Toast.LENGTH_SHORT).show();
                searchView.setQuery(suggestion, false);//setting suggestion

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                return true;

            }
        });

        adapterViewPager = new FragmentAdapter(getSupportFragmentManager(), "0");
        viewPager.setAdapter(adapterViewPager);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, adapterViewPager.getItem(0)).commit();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, adapterViewPager.getItem(0)).commit();
//                        getSupportActionBar().setTitle("History");
                        return true;
                    case R.id.navigation_about:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, adapterViewPager.getItem(1)).commit();
//                        getSupportActionBar().setTitle("Favorite");
                        return true;
                }
                return false;
            }
        });


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Utils.checkLocationPermission(this);
        }

        if (!Utils.checkInternetOn(this)) {
            Utils.createNetErrorDialog(this);
        }

        if (!Utils.isLocationEnabled(MainActivity.this)) {
            Utils.createLocationErrorDialog(MainActivity.this);
        }
    }


    private void populateAdapter(final String query) {
        final List<TheLocation> locations = new ArrayList<>();

        //Southwest corner to Northeast corner.
//        final LatLngBounds bounds = new LatLngBounds(new LatLng(10.719123, 106.602913), new LatLng(10.885713, 106.646757));
        final LatLngBounds bounds = Utils.toBounds(new LatLng(10.793093, 106.653773), 200);

        Observable<PlacePrediction> observable = Observable.create(new Observable.OnSubscribe<PlacePrediction>() {
            @Override
            public void call(Subscriber<? super PlacePrediction> subscriber) {
                PendingResult<AutocompletePredictionBuffer> results =
                        Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, query,
                                bounds, null);

                AutocompletePredictionBuffer autocompletePredictions = results
                        .await(60, TimeUnit.SECONDS);

                final Status status = autocompletePredictions.getStatus();
                if (!status.isSuccess()) {
                    autocompletePredictions.release();
                    subscriber.onError(null);
                } else {
                    for (AutocompletePrediction autocompletePrediction : autocompletePredictions) {
                        subscriber.onNext(
                                new PlacePrediction(
                                        autocompletePrediction.getPlaceId(),
                                        autocompletePrediction.getFullText(new StyleSpan(Typeface.BOLD)
                                        )
                                ));
                    }
                    autocompletePredictions.release();
                    subscriber.onCompleted();
                }
            }
        });

        Observer<PlacePrediction> observer = new Observer<PlacePrediction>() {
            @Override
            public void onNext(PlacePrediction value) {
                Log.e(TAG, "onNext: " + value);

                Places.GeoDataApi.getPlaceById(mGoogleApiClient, value.getPlaceId() + "")
                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                    final Place myPlace = places.get(0);

                                    TheLocation aLocation = new TheLocation(0, myPlace.getName() + ", " + myPlace.getAddress(), myPlace.getLatLng().latitude, myPlace.getLatLng().longitude);
                                    locations.add(aLocation);
                                    final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "busStopName", "lat", "long"});
                                    for (int i = 0; i < locations.size(); i++) {
                                        if (locations.get(i).getName().toLowerCase().startsWith(query.toLowerCase()))

                                            c.addRow(new Object[]{locations.get(i).getId(), locations.get(i).getName(), locations.get(i).getLatitude(), locations.get(i).getLongitude()});
                                    }

                                    mAdapter.changeCursor(c);
                                }
                                places.release();
                            }
                        });
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError: ");
            }


        };

        observable.subscribeOn(Schedulers.io()).subscribe(observer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.origin, menu);

        Gson gson = new Gson();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String jsonPatients = sharedPref.getString("userAccount", "");

        if (!jsonPatients.equals("")) {
            Type type = new TypeToken<EmailInfo>() {
            }.getType();
            EmailInfo account = gson.fromJson(jsonPatients, type);

            MenuItem item1 = menu.findItem(R.id.userDisplay);
            //Here, you get access to the view of your item, in this case, the layout of the item has a FrameLayout as root view but you can change it to whatever you use
            LinearLayout rootView = (LinearLayout) item1.getActionView();
            TextView username = (TextView) rootView.findViewById(R.id.username);
            username.setText(account.getName());
            CircleImageView cvImg = (CircleImageView) rootView.findViewById(R.id.profile_image);
            Glide.with(this).load(account.getAvatarUrl()).into(cvImg);
        }

        MenuItem item = menu.findItem(R.id.action_logout);
        SpannableStringBuilder builder = new SpannableStringBuilder("*  Logout");
        // replace "*" with icon
        builder.setSpan(new ImageSpan(this, R.drawable.ic_power_settings_new_black_24dp), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                                //Remove all info of current account
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("userToken", "");
                                editor.putString("userID", "");

                                editor.commit();

                                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);

        super.onResume();
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(intent);
        finish();

    }

    //Subscribe action for Event Bus
    @Subscribe
    public void onEvent(FragmentAdapter.OpenEvent event) {
        Toast.makeText(getBaseContext(), "Starting Map", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getBaseContext(), MapsActivity.class);
        TheLocation aLocation = new TheLocation(event.position, "aLocation", event.lat, event.lng);
        intent.putExtra("busStop", aLocation);
        startActivity(intent);
        finish();
    }


}
