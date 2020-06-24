package com.jsn.play.util

import android.util.Log
import androidx.ads.identifier.AdvertisingIdClient
import androidx.ads.identifier.AdvertisingIdInfo
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures.addCallback
import com.jsn.play.MApp
import java.util.concurrent.Executors

fun determineAdvertisingInfo() {
    if (AdvertisingIdClient.isAdvertisingIdProviderAvailable(MApp.app)) {
        val advertisingIdInfoListenableFuture =
                AdvertisingIdClient.getAdvertisingIdInfo(MApp.app)

        addCallback(advertisingIdInfoListenableFuture,
                object : FutureCallback<AdvertisingIdInfo> {
                    override fun onSuccess(adInfo: AdvertisingIdInfo?) {
                        val id: String? = adInfo?.id.also { Log.e("id",it) }
                        val providerPackageName: String? = adInfo?.providerPackageName.also { Log.e("providerPackageName",it) }
                        val isLimitTrackingEnabled: Boolean? =
                                adInfo?.isLimitAdTrackingEnabled.also { Log.e("limitEnabled",it.toString()) }
                    }

                    override fun onFailure(t: Throwable) {
                        Log.e("MY_APP_TAG",
                                "Failed to connect to Advertising ID provider.")
                        // Try to connect to the Advertising ID provider again, or fall
                        // back to an ads solution that doesn't require using the
                        // Advertising ID library.
                    }
                }, Executors.newSingleThreadExecutor())
    } else {
        // The Advertising ID client library is unavailable. Use a different
        // library to perform any required ads use cases.

        Log.e("MY_APP_TAG",
            "The Advertising ID client library is unavailable. Use a different\n" +
                    "        library to perform any required ads use cases.")
    }
}