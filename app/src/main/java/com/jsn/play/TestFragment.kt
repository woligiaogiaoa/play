package com.jsn.play

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.annotation.Px
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.jsn.play.databinding.FragmentTestBinding
import com.jsn.play.util.doOnApplyWindowInsets
import com.jsn.play.util.safeRequestLayout
import com.jsn.play.util.showToast
import com.jsn.play.util.updatePaddingRelative
import kotlinx.android.synthetic.main.fragment_test.*

class TestFragment:MainNavigationFragment(){

    lateinit var binding:FragmentTestBinding

    lateinit var adapter: SimpleStirngAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.doOnApplyWindowInsets{view, insets, viewPaddingState ->
            binding.vStatusBar.run {
                layoutParams.height = insets.systemWindowInsetTop
                isVisible = layoutParams.height > 0
                safeRequestLayout()
            }
        }

        rv.adapter=SimpleStirngAdapter().also { adapter=it }

        rv.doOnApplyWindowInsets{view, windowInsetsCompat, viewPaddingState ->
            view.updatePaddingRelative(bottom = viewPaddingState.bottom+windowInsetsCompat.systemWindowInsetBottom)
        }
        adapter.submitList(mutableListOf<String>().apply {
            repeat(100){
                add("${it}")
            }
        })
    }
}
