package com.jsn.play

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.jsn.play.databinding.FragmentPlayBinding
import com.jsn.play.util.doOnApplyWindowInsets
import com.jsn.play.util.updatePaddingRelative
import kotlinx.android.synthetic.main.fragment_play.*
import kotlinx.android.synthetic.main.fragment_test.*
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

    }

    override fun onStart() {
        super.onStart()
        //collapding_tool_bar.setStatusBarScrimColor(ContextCompat.getColor(requireContext(),R.color.status_bar_scrim))
    }

}


