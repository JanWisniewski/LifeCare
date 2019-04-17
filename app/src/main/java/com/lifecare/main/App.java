package com.lifecare.main;

import android.app.Application;
import android.content.Intent;

import com.lifecare.main.Services.MyService;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, MyService.class));
    }

}
