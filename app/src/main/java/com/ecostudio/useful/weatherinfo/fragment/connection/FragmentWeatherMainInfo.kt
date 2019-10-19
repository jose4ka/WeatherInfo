package com.ecostudio.useful.weatherinfo.fragment.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ecostudio.useful.weatherinfo.R
import kotlinx.android.synthetic.main.fragment_weather_main_info.view.*

class FragmentWeatherMainInfo : Fragment(){




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_weather_main_info, container, false)

        initializeScreenElements(view)

        return view
    }


    //Инициализация элементов экрана
    fun initializeScreenElements(view: View){
        view.tvWeatherName.text = arguments?.getString("name", "null")
        view.tvWeatherHumidility.text = arguments?.getInt("humidity").toString()
        view.tvWeatherPressure.text = arguments?.getInt("pressure").toString()
        view.tvWeatherTemp.text = arguments?.getDouble("temperature").toString()
        view.tvWeatherMaxT.text = arguments?.getDouble("max_temp").toString()
        view.tvWeatherMinT.text = arguments?.getDouble("min_temp").toString()
    }



    //Выполняет роль конструктора
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










}