package com.jsn.play.util

import android.content.Context
import android.widget.Toast

internal  fun Context.showToast(s:String)=Toast.makeText(this,s,Toast.LENGTH_SHORT).show()