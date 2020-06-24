package com.jsn.play.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.jsn.play.MainNavigationFragment
import com.jsn.play.NavigationDestination
import com.jsn.play.data.Result
import com.jsn.play.databinding.FragmentWeatherBinding
import com.jsn.play.util.doOnApplyWindowInsets
import com.jsn.play.util.safeRequestLayout
import com.jsn.play.util.updatePaddingRelative
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlinx.coroutines.flow.collect

class WeatheStreamFragment :MainNavigationFragment(){

    lateinit var binding: FragmentWeatherBinding

    val viewModel by viewModels<WeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var adapter: WeatherAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        status_bar_scrim.doOnApplyWindowInsets { view, windowInsetsCompat, viewPaddingState ->
            status_bar_scrim.run {
                layoutParams.height = windowInsetsCompat.systemWindowInsetTop
                isVisible = layoutParams.height > 0
                safeRequestLayout()
            }
        }
        rv.adapter = WeatherAdapter().also { adapter = it }

        lifecycleScope.launchWhenStarted {
            viewModel.weatheFlow.collect { currentStatus ->
                pb.isVisible = currentStatus is Result.Loading
                when (currentStatus) {
                    Result.Loading -> {
                    }
                    is Result.Error -> {
                    }
                    is Result.Success -> {
                        adapter.submitList(currentStatus.data.results)
                    }
                }
            }
        }

    }
}