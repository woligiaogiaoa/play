package com.jsn.play

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

fun View.setOnApplyWindowInsetsListenerCompat(apply:(View,WindowInsetsCompat) ->Unit){

    ViewCompat.setOnApplyWindowInsetsListener(this){ v: View, insets: WindowInsetsCompat ->
        apply(v,insets)
        insets
    }
}

class MainActivity : AppCompatActivity() {



    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        content_container.setOnApplyWindowInsetsListenerCompat { v, insets ->
            // Let the view draw it's navigation bar divider
            // Consume any horizontal insets and pad all content in. There's not much we can do
            // with horizontal insets
            v.updatePadding(
                left = insets.systemWindowInsetLeft,
                right = insets.systemWindowInsetRight
            )
            insets.replaceSystemWindowInsets(
                0, insets.systemWindowInsetTop,
                0, insets.systemWindowInsetBottom
            )
        }

        content.setOnApplyWindowInsetsListener(NoopWindowInsetsListener)
        status_bar_scrim.setOnApplyWindowInsetsListener(HeightTopWindowInsetsListener)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus) window.decorView.systemUiVisibility=
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE  or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION


    }
}

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
object NoopWindowInsetsListener : View.OnApplyWindowInsetsListener {
    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
        return insets
    }
}

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
object HeightTopWindowInsetsListener : View.OnApplyWindowInsetsListener {
    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
        val topInset = insets.systemWindowInsetTop
        if (v.layoutParams.height != topInset) {
            v.layoutParams.height = topInset
            v.requestLayout()
        }
        return insets
    }
}

inline fun View.updatePadding(
    @Px left: Int = paddingLeft,
    @Px top: Int = paddingTop,
    @Px right: Int = paddingRight,
    @Px bottom: Int = paddingBottom
) {
    setPadding(left, top, right, bottom)
}


