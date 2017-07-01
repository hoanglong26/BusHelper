package com.example.hoanglong.bushelper.dagger2.component;


import com.example.hoanglong.bushelper.MapsActivity;
import com.example.hoanglong.bushelper.dagger2.module.AppModule;
import com.example.hoanglong.bushelper.dagger2.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hoanglong on 26-Jun-17.
 */


@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface    NetComponent {
    void inject(MapsActivity activity);

}

