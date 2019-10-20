package com.ecostudio.useful.weatherinfo.fragment

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.ecostudio.useful.weatherinfo.R
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.fragment_weather_main_info.view.*

class FragmentWeatherMainInfo : Fragment(){

    val SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES"
    var unitsPoints: String = "C"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_weather_main_info, container, false)
        setScaleAnimation(view.mainInfoCardView)

        getUnits(view)
        initializeScreenElements(view)

        return view
    }


    //Initialize screen elements
    fun initializeScreenElements(view: View){
        view.tvWeatherName.text = arguments?.getString("name", "null")
        view.tvWeatherHumidility.text = arguments?.getInt("humidity").toString()+" %"
        view.tvWeatherPressure.text = arguments?.getInt("pressure").toString()+" "+resources.getString(R.string.millimeters)
        view.tvWeatherTemp.text = arguments?.getDouble("temperature").toString()+" "+unitsPoints
        view.tvWeatherMaxT.text = arguments?.getDouble("max_temp").toString()+" "+unitsPoints
        view.tvWeatherMinT.text = arguments?.getDouble("min_temp").toString()+" "+unitsPoints
    }



    //That like constructor
    companion object {

        @JvmStatic
        fun newInstance(name: String,
                        temp: Double,
                        maxTemp: Double,
                        minTemp: Double,
                        pressure: Int,
                        humidity: Int) = FragmentWeatherMainInfo().apply {
            arguments = Bundle().apply {
                putDouble("temperature", temp)
                putDouble("max_temp", maxTemp)
                putDouble("min_temp", minTemp)
                putInt("pressure", pressure)
                putInt("humidity", humidity)
                putString("name", name)
            }
        }
    }


    private fun setScaleAnimation(view: View){
        var anim: Animation = AnimationUtils.loadAnimation(context, R.anim.animation_scale)
        view.startAnimation(anim)
    }

    fun getUnits(view: View){
        if(context != null){
            var settingsPreferences = context!!.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

            var units = settingsPreferences.getInt("units_position", 0)

            var array = resources.getStringArray(R.array.array_units_t)
            unitsPoints = array[units]
        }
    }



}