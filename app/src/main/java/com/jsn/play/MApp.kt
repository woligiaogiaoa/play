package com.jsn.play

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MApp:Application(){
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}