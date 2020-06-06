package com.jsn.play.data

import android.content.SharedPreferences
import com.jsn.play.MApp

object PrefManager{

    val preferences by lazy {
        MApp.preferences
    }

    val firstOpen
            get()= preferences.getBoolean(APP_FIRST_OPEN_KEY,true)
                .also {
                    preferences.edit{
                        putBoolean(APP_FIRST_OPEN_KEY,false)
                    }
                }
}


const val APP_FIRST_OPEN_KEY="APP_FIRST_OPEN_KEY"




inline fun SharedPreferences.edit(action:SharedPreferences.Editor.() -> Unit){
    val edit = edit()
    edit.apply(){
        action.invoke(this)
        apply()
    }
}