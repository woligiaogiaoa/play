package com.jsn.play.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.jsn.play.MainNavigationFragment
import com.jsn.play.SimpleStirngAdapter
import com.jsn.play.databinding.FragmentHomeBinding
import com.jsn.play.util.doOnApplyWindowInsets
import com.jsn.play.util.updatePaddingRelative
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : MainNavigationFragment() {

    lateinit var adapter: SimpleStirngAdapter

    lateinit var viewModle: MainViewModle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModle= (requireActivity() as MainActivity).viewModle
        val inflate = FragmentHomeBinding.inflate(inflater, container, false)
        inflate.rv.adapter= SimpleStirngAdapter().also { adapter=it }

        return inflate.root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv.doOnApplyWindowInsets{view, windowInsetsCompat, viewPaddingState ->
            rv.updatePaddingRelative(
                bottom = viewPaddingState.bottom+windowInsetsCompat.systemWindowInsetBottom)
        }
        adapter.submitList(mutableListOf<String>().apply {
            repeat(100){
                add("${it}")
            }
        })
        rv.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                viewModle.scrollChannel.offer(
                    if(dy>0) ScrollDirection.SCROLL_UP else if(dy==0) ScrollDirection.SCROLL_NON_DIR else ScrollDirection.SCROLL_DOWN
                )
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

    }
}


