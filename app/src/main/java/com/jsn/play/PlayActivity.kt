package com.jsn.play

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.jsn.play.util.doOnApplyWindowInsets
import com.jsn.play.util.shouldCloseDrawerFromBackPress
import com.jsn.play.util.showToast
import com.jsn.play.util.updatePaddingRelative
import kotlinx.android.synthetic.main.activity_play.*
import kotlinx.android.synthetic.main.fragment_play.*


val NAV_ID_NONE=-1
class PlayActivity : AppCompatActivity(),NavigationHost {

    val   viewModle: MainViewModle by viewModels<MainViewModle>()

    private lateinit var navController: NavController

    private var currentNavId = NAV_ID_NONE

    private val TOP_LEVEL_DESTINATIONS = setOf(
        R.id.playFragment,R.id.testFragment
    )

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        content_container.systemUiVisibility=
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        content_container.setOnApplyWindowInsetsListener{v, insets -> insets }

        drawer_container.setOnApplyWindowInsetsListener { v, insets ->
            // Let the view draw it's navigation bar divider

            println(insets)
            v.onApplyWindowInsets(insets)

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

        status_bar_scrim.setOnApplyWindowInsetsListener{v, insets ->
            val topInset = insets.systemWindowInsetTop
            if (v.layoutParams.height != topInset) {
                v.layoutParams.height = topInset
                v.requestLayout()
            }
            insets
        }

        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentNavId = destination.id
            val isTopLevelDestination = TOP_LEVEL_DESTINATIONS.contains(destination.id)
            val lockMode = if (isTopLevelDestination) {
                DrawerLayout.LOCK_MODE_UNLOCKED
            } else {
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            }
            drawer_layout.setDrawerLockMode(lockMode)
        }

        nav_view.setupWithNavController(navController)

        fab_container.doOnApplyWindowInsets{_,insets,padding ->
            fab_container.updatePaddingRelative(bottom = padding.bottom+insets.systemWindowInsetBottom)
        }

        val isNight = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        fab.setImageDrawable(if(isNight == Configuration.UI_MODE_NIGHT_YES) getDrawable(R.drawable.ic_wb_sunny_black_24dp) else getDrawable(R.drawable.ic_brightness_3_black_24dp))

        fab.setOnClickListener {
            val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (isNightTheme) {
                Configuration.UI_MODE_NIGHT_YES ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Configuration.UI_MODE_NIGHT_NO ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            fab.setImageDrawable(if(isNightTheme == Configuration.UI_MODE_NIGHT_YES) getDrawable(R.drawable.ic_wb_sunny_black_24dp) else getDrawable(R.drawable.ic_brightness_3_black_24dp))
        }
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS, drawer_layout)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        getCurrentFragment()?.onUserInteraction()
    }

    private fun getCurrentFragment(): MainNavigationFragment? {
        return nav_host_fragment
            ?.childFragmentManager
            ?.primaryNavigationFragment as? MainNavigationFragment
    }

    private fun navigateTo(navId: Int) {
        if (navId == currentNavId) {
            return // user tapped the current item
        }
        navController.navigate(navId)
    }


    override fun onBackPressed() {
        /**
         * If the drawer is open, the behavior changes based on the API level.
         * When gesture nav is enabled (Q+), we want back to exit when the drawer is open.
         * When button navigation is enabled (on Q or pre-Q) we want to close the drawer on back.
         */
        if (drawer_layout.isDrawerOpen(nav_view) && drawer_layout.shouldCloseDrawerFromBackPress()) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    private fun closeDrawer() {
        drawer_layout.closeDrawer(GravityCompat.START)
    }
}



