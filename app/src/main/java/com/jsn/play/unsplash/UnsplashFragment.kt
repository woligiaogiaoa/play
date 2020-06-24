package com.jsn.play.unsplash

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.jsn.play.MainNavigationFragment
import com.jsn.play.ScrollStateFragment
import com.jsn.play.databinding.FragmentUnsplashBinding
import com.jsn.play.home.MainActivity
import com.jsn.play.home.ScrollDirection
import com.jsn.play.util.doOnApplyWindowInsets
import com.jsn.play.util.safeRequestLayout
import kotlinx.android.synthetic.main.fragment_unsplash.*
import java.lang.RuntimeException

class UnsplashFragment: ScrollStateFragment(){

    lateinit var binding:FragmentUnsplashBinding

    lateinit var webView: WebView

    val viewModel by viewModels<UnsplashViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnsplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.doOnApplyWindowInsets{view, insets, viewPaddingState ->
            binding.vStatusBar.run{
                layoutParams.height = insets.systemWindowInsetTop
                isVisible = layoutParams.height > 0
                post { requestLayout() }
            }
        }
        buildWebview()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun buildWebview() {
        webView=webview
        webView.post {
            webView.loadUrl("https://unsplash.com/")
        }
        webview.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val dy=scrollY-oldScrollY
            scrollStateObserver.receiveState(
                if(dy>0) ScrollDirection.SCROLL_UP else if(dy==0) ScrollDirection.SCROLL_NON_DIR else ScrollDirection.SCROLL_DOWN
            )
            viewModel.position.value=scrollY
        }
        webView.setDownloadListener{url, userAgent, contentDisposition, mimetype, contentLength ->
            Intent(Intent.ACTION_VIEW).also {
                it.data=Uri.parse(url)
                startActivity(it)
            }
        }
        webView.webViewClient=object :WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }
    }

}

/*mWebView.setDownloadListener(new DownloadListener() {
    public void onDownloadStart(String url, String userAgent,
                String contentDisposition, String mimetype,
                long contentLength) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
});*/

