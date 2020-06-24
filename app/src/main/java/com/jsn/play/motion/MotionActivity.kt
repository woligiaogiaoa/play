package com.jsn.play.motion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jsn.play.R
import com.jsn.play.databinding.ActivityMotionBinding
import kotlinx.android.synthetic.main.activity_motion.*

class MotionActivity : AppCompatActivity() {

    val viewModel by viewModels<MotionViewModel>()

    var text="1"

    lateinit var adapter: SearchAdapter

    lateinit var binding:ActivityMotionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView<ActivityMotionBinding>(this, R.layout.activity_motion)
        rv.adapter=SearchAdapter().also { adapter=it }
        binding.lifecycleOwner=this
        binding.viewModle=viewModel
        srl.setOnRefreshListener {
            text=text+"11"+"222"
            viewModel.data.add(0,text)
            srl.isRefreshing=false
        }
    }
}

@BindingAdapter("stringData")
fun bindStringList(view:RecyclerView, data:List<String>){
    (view.adapter as? SearchAdapter )?.submitList(data)
}
