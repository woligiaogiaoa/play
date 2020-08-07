package com.jsn.play

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.jsn.play.data.PrefManager
import com.jsn.play.util.determineAdvertisingInfo
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MApp:Application(){

    companion object{
        @JvmStatic
        lateinit var app:MApp
        @JvmStatic
        val preferences by lazy {
            PreferenceManager.getDefaultSharedPreferences(app)
        }
    }

    val firstOpen
    get() = PrefManager.firstOpen

    override fun onCreate() {
        super.onCreate()
        app=this

        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        determineAdvertisingInfo()
    }
}

