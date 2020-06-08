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

    var lastStatus:Result<Weather>?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv.doOnApplyWindowInsets { view, windowInsetsCompat, viewPaddingState ->
            tv.updatePaddingRelative(top=viewPaddingState.top+windowInsetsCompat.systemWindowInsetTop)
        }


        lifecycleScope.launchWhenStarted {

            viewModel.weatheFlow.collect { currentStatus ->

                tv.text=currentStatus.toString()
                pb.isVisible=currentStatus is Result.Loading
                tv.isVisible=!(currentStatus is Result.Loading)
                val color=when(currentStatus){
                    Result.Loading -> return@collect
                    is Result.Error -> context!!.getColor(android.R.color.holo_red_dark)
                    is Result.Success -> context!!.getColor(android.R.color.holo_blue_dark)
                }

                when(lastStatus){
                    Result.Loading -> {}
                    is Result.Error ->{}
                    is Result.Success -> {}
                    null -> {}
                }
                val a =(tv.tag as? Int)?.equals(color)
                a ?: tv.setTextColor(color)
                if(a !=null && !(tv.tag as Int)!!.equals(color)) tv.setTextColor(color)
                tv.tag=color
                lastStatus=currentStatus
            }
        }
    }
}