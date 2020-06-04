package com.jsn.play

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.jsn.play.databinding.FragmentPlayBinding
import com.jsn.play.util.doOnApplyWindowInsets
import com.jsn.play.util.updatePaddingRelative
import kotlinx.android.synthetic.main.fragment_test.rv

class PlayFragment :MainNavigationFragment() {

    lateinit var adapter: SimpleStirngAdapter

    lateinit var viewModle: MainViewModle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModle= (requireActivity() as PlayActivity ).viewModle
        val inflate = FragmentPlayBinding.inflate(inflater, container, false)
        inflate.rv.adapter=SimpleStirngAdapter().also { adapter=it }

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

    override fun onStart() {
        super.onStart()
        //collapding_tool_bar.setStatusBarScrimColor(ContextCompat.getColor(requireContext(),R.color.status_bar_scrim))
    }

}


