package com.example.hoanglong.bushelper;

import android.app.Application;
import android.content.Context;
import android.os.RemoteException;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.example.hoanglong.bushelper.POJO.Beacon;
import com.example.hoanglong.bushelper.POJO.BusInfo;
import com.example.hoanglong.bushelper.POJO.BusInfoBusStop;
import com.example.hoanglong.bushelper.POJO.BusStopDB;
import com.example.hoanglong.bushelper.api.RetrofitUtils;
import com.example.hoanglong.bushelper.api.ServerAPI;
import com.example.hoanglong.bushelper.dagger2.component.DaggerNetComponent;
import com.example.hoanglong.bushelper.dagger2.component.NetComponent;
import com.example.hoanglong.bushelper.dagger2.module.AppModule;
import com.example.hoanglong.bushelper.dagger2.module.NetModule;
import com.example.hoanglong.bushelper.utils.SendNotificationTask;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoanglong.bushelper.api.Constant.MAP_URL;

/**
 * Created by hoanglong on 26-Jun-17.
 */

public class App extends Application {
//        implements BootstrapNotifier {
//    private RegionBootstrap regionBootstrap;
//    private BackgroundPowerSaver backgroundPowerSaver;
//    private BeaconManager mBeaconmanager;
//    final BootstrapNotifier notifier = this;
//    ArrayList<Region> regionList = new ArrayList();

    private NetComponent mNetComponent;

//    public List<BusStopDB> busStopListFromDB = new ArrayList<>();
//    private ServerAPI serverAPI;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(MAP_URL))
                .build();

//        serverAPI = RetrofitUtils.get().create(ServerAPI.class);

//        serverAPI.getAllBusStop().enqueue(new Callback<List<BusStopDB>>() {
//            @Override
//            public void onResponse(Call<List<BusStopDB>> call, Response<List<BusStopDB>> response) {
//                busStopListFromDB = response.body();
//            }
//
//            @Override
//            public void onFailure(Call<List<BusStopDB>> call, Throwable t) {
//                Log.d("test", t.getMessage());
//            }
//        });

//        mBeaconmanager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(getBaseContext());
//        mBeaconmanager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
//
//        mBeaconmanager.setBackgroundMode(true);
//        backgroundPowerSaver = new BackgroundPowerSaver(getBaseContext());
//
//        mBeaconmanager.setBackgroundBetweenScanPeriod(25000l);
//        mBeaconmanager.setBackgroundScanPeriod(20000l);

    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

//    public List<BusStopDB> getBusStopListFromDB(){
//        serverAPI.getAllBusStop().enqueue(new Callback<List<BusStopDB>>() {
//            @Override
//            public void onResponse(Call<List<BusStopDB>> call, Response<List<BusStopDB>> response) {
//                busStopListFromDB = response.body();
//            }
//
//            @Override
//            public void onFailure(Call<List<BusStopDB>> call, Throwable t) {
//                Log.d("test", t.getMessage());
//
//            }
//        });
//
//        return busStopListFromDB;
//    }

//    @Override
//    public void didEnterRegion(Region region) {
//        SendNotificationTask.sendNotification(getBaseContext(),"You are going to arrive "+ region.getUniqueId());
//        for (Region tmp : mBeaconmanager.getMonitoredRegions()) {
//            try {
//                mBeaconmanager.stopMonitoringBeaconsInRegion(tmp);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @Override
//    public void didExitRegion(Region region) {
//
//    }
//
//    @Override
//    public void didDetermineStateForRegion(int i, Region region) {
//
//    }

//    public void addBeaconToBeMonitered(Beacon aBeacon, BusStopDB busStop){
//        String uuid = aBeacon.getUuid();
//        Identifier identifier = Identifier.parse(uuid);
//        Identifier identifier2 = Identifier.parse(String.valueOf(aBeacon.getMajor()));
//        Identifier identifier3 = Identifier.parse(String.valueOf(aBeacon.getMinor()));
//
//        String uniqueName = busStop.getName()+" with bus number:";
//
//        for(BusInfoBusStop tmp : busStop.getBusInfoBusStops()){
//            uniqueName+=" "+tmp.getBusInfo().getNumber();
//        }
//
//        Region region = new Region(uniqueName, identifier, identifier2, identifier3);
//
//        regionList.add(region);
//
//        regionBootstrap = new RegionBootstrap(notifier, regionList);
//    }
}
