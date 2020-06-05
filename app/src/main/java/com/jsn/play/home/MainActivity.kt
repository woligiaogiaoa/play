package com.jsn.play.home

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.updatePadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jsn.play.*
import com.jsn.play.util.awaitEnd
import com.jsn.play.util.doOnApplyWindowInsets
import com.jsn.play.util.shouldCloseDrawerFromBackPress
import com.jsn.play.util.updatePaddingRelative
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.collect


val NAV_ID_NONE = -1



class MainActivity : AppCompatActivity(), NavigationHost,ScrollStateObserver {

    val viewModle: MainViewModle by viewModels<MainViewModle>()

    private lateinit var navController: NavController

    private var currentNavId = NAV_ID_NONE

    private val TOP_LEVEL_DESTINATIONS = setOf(
        R.id.homeFragment,
        R.id.unsplashFragment
        //todo: github ,wikipadia
    )

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        content_container.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        content_container.setOnApplyWindowInsetsListener { v, insets -> insets }

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

        status_bar_scrim.setOnApplyWindowInsetsListener { v, insets ->
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

        fab_container.doOnApplyWindowInsets { _, insets, padding ->
            fab_container.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }

        val isNight = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        fab.setImageDrawable(
            if (isNight == Configuration.UI_MODE_NIGHT_YES) getDrawable(
                R.drawable.ic_wb_sunny_black_24dp
            ) else getDrawable(R.drawable.ic_brightness_3_black_24dp)
        )

        fab.setOnClickListener {
            val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (isNightTheme) {
                Configuration.UI_MODE_NIGHT_YES ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Configuration.UI_MODE_NIGHT_NO ->
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            fab.setImageDrawable(
                if (isNightTheme == Configuration.UI_MODE_NIGHT_YES) getDrawable(
                    R.drawable.ic_wb_sunny_black_24dp
                ) else getDrawable(R.drawable.ic_brightness_3_black_24dp)
            )
        }


        animateByScrollingState()


    }

    lateinit var animator: Animator

    //if we have fragment that contains recyclerview for example,we can react to view's scrolling state
    fun animateByScrollingState() {
        lifecycleScope.launchWhenStarted {
            viewModle.scrollChannel.sendBlocking(ScrollDirection.SCROLL_NON_DIR)
            viewModle.ScrollStateFlow.collect {
                var targetAlpha: Float = -1f
                when (it) {
                    ScrollDirection.SCROLL_NON_DIR -> {
                    }
                    ScrollDirection.SCROLL_UP -> {
                        targetAlpha = 0f
                    }
                    ScrollDirection.SCROLL_DOWN -> {
                        targetAlpha = 1f
                    }
                }
                if (targetAlpha == -1f) return@collect
                if (fab.alpha == targetAlpha) return@collect
                animator = ObjectAnimator.ofFloat(fab, View.ALPHA, fab.alpha, targetAlpha)

                animator.run {
                    fab.isClickable = false
                    duration = 500
                    interpolator = DecelerateInterpolator()
                    start()
                    awaitEnd()
                    fab.isClickable =( fab.alpha==1f)
                }
            }
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

    override fun receiveState(state: ScrollDirection) {
        viewModle.scrollChannel.offer(state)
    }
}



