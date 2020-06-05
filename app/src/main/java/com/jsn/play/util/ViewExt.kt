package com.jsn.play.util

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.ViewParent
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.core.os.BuildCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume

@SuppressLint("NewApi") // Lint does not understand isAtLeastQ currently
fun DrawerLayout.shouldCloseDrawerFromBackPress(): Boolean {
    if (BuildCompat.isAtLeastQ()) {
        // If we're running on Q, and this call to closeDrawers is from a key event
        // (for back handling), we should only honor it IF the device is not currently
        // in gesture mode. We approximate that by checking the system gesture insets
        return rootWindowInsets?.let {
            val systemGestureInsets = it.systemGestureInsets
            return systemGestureInsets.left == 0 && systemGestureInsets.right == 0
        } ?: false
    }
    // On P and earlier, always close the drawer
    return true
}



@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun View.isSafeToRequestDirectly():Boolean {
    return if (isInLayout) {
        // when isInLayout == true and isLayoutRequested == true,
        // means that this layout pass will layout current view which will
        // make currentView.isLayoutRequested == false, and this will let currentView
        // ignored in process handling requests called during last layout pass.
        isLayoutRequested.not()
    } else {
        var ancestorLayoutRequested = false
        var p: ViewParent? = parent
        while (p != null) {
            if (p.isLayoutRequested) {
                ancestorLayoutRequested = true
                break
            }
            p = p.parent
        }
        ancestorLayoutRequested.not()
    }
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun View.safeRequestLayout() {
    if (
        isSafeToRequestDirectly()) {
        requestLayout()
    } else {
        post { requestLayout() }
    }
}



@RequiresApi(17)
inline fun View.updatePaddingRelative(
    @Px start: Int = paddingStart,
    @Px top: Int = paddingTop,
    @Px end: Int = paddingEnd,
    @Px bottom: Int = paddingBottom
) {
    setPaddingRelative(start, top, end, bottom)
}


@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, ViewPaddingState) -> Unit) {
    // Create a snapshot of the view's padding state
    val paddingState = createStateForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, paddingState)
        insets
    }
    requestApplyInsetsWhenAttached()
}

/**
 * Call [View.requestApplyInsets] in a safe away. If we're attached it calls it straight-away.
 * If not it sets an [View.OnAttachStateChangeListener] and waits to be attached before calling
 * [View.requestApplyInsets].
 */
@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

            override fun onViewAttachedToWindow(v: View) {
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
private fun createStateForView(view: View) = ViewPaddingState(view.paddingLeft,
    view.paddingTop, view.paddingRight, view.paddingBottom, view.paddingStart, view.paddingEnd)

data class ViewPaddingState(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
)

suspend fun WebView.awaitLoading()= suspendCancellableCoroutine<Unit>{ cont ->
    webChromeClient=object : WebChromeClient(){
        val lock= AtomicBoolean(true)
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if(newProgress>=100){
                if(lock.compareAndSet(true,false)){
                    cont.resume(Unit)
                    webChromeClient=null
                }
            }
        }
    }
}