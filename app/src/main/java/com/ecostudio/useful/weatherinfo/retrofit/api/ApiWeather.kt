package com.ecostudio.useful.weatherinfo.retrofit.api

import com.ecostudio.useful.weatherinfo.retrofit.model.weather.WeatherMain
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiWeather{



    @GET("/data/2.5/weather")
    fun getWeather(@Query("q") query:String,
                   @Query("appid") appid:String,
                   @Query("units") units:String): Call<WeatherMain>
}