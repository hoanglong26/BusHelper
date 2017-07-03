package com.example.hoanglong.bushelper;

import android.app.Application;

import com.example.hoanglong.bushelper.dagger2.component.DaggerNetComponent;
import com.example.hoanglong.bushelper.dagger2.component.NetComponent;
import com.example.hoanglong.bushelper.dagger2.module.AppModule;
import com.example.hoanglong.bushelper.dagger2.module.NetModule;

import static com.example.hoanglong.bushelper.api.Constant.MAP_URL;

/**
 * Created by hoanglong on 26-Jun-17.
 */

public class App extends Application {

    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(MAP_URL))
                .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

}
