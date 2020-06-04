package com.jsn.play

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class MApp:Application(){

    companion object{
        @JvmStatic
        lateinit var app:Application
    }

    override fun onCreate() {
        super.onCreate()
        app=this
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}