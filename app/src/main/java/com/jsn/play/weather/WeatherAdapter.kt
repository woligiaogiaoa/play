package com.jsn.play.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jsn.play.databinding.ItemSearchResultBinding
import com.jsn.play.databinding.ItemWeatherBinding

val callback=object : DiffUtil.ItemCallback<Weather.Result>(){
    override fun areItemsTheSame(oldItem: Weather.Result, newItem: Weather.Result): Boolean {
        return oldItem===newItem
    }

    override fun areContentsTheSame(oldItem: Weather.Result, newItem: Weather.Result): Boolean {
        return oldItem.equals(newItem)
    }
}


class WeatherAdapter : ListAdapter<Weather.Result, WeatherAdapter.WeatherViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class WeatherViewHolder(val binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item:Weather.Result){
            binding.tv.text=item.now.temperature
        }
    }
}