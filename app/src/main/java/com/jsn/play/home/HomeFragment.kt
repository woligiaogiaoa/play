package com.jsn.play.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jsn.play.MainNavigationFragment
import com.jsn.play.ScrollStateFragment
import com.jsn.play.SimpleStirngListAdapter
import com.jsn.play.databinding.FragmentHomeBinding
import com.jsn.play.util.doOnApplyWindowInsets
import com.jsn.play.util.updatePaddingRelative
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.IllegalStateException

class HomeFragment : ScrollStateFragment() {

    lateinit var listAdapter: SimpleStirngListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val inflate = FragmentHomeBinding.inflate(inflater, container, false)
        inflate.rv.adapter= SimpleStirngListAdapter().also { listAdapter=it }
        return inflate.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv.doOnApplyWindowInsets{view, windowInsetsCompat, viewPaddingState ->
            rv.updatePaddingRelative(
                bottom = viewPaddingState.bottom+windowInsetsCompat.systemWindowInsetBottom)
        }
        listAdapter.submitList(mutableListOf<String>().apply {
            repeat(100){
                add("${it}")
            }
        })

        rv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrollStateObserver.receiveState(
                    if(dy>0) ScrollDirection.SCROLL_UP else if(dy==0) ScrollDirection.SCROLL_NON_DIR else ScrollDirection.SCROLL_DOWN
                )
            }
        })

    }
}



