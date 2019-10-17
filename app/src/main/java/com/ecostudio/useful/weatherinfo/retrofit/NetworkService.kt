package com.ecostudio.useful.weatherinfo.retrofit

import com.ecostudio.useful.weatherinfo.R
import com.ecostudio.useful.weatherinfo.retrofit.api.ApiWeather
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService {

    val API_KEY:String = R.string.api_key.toString()
    private var mInstance:NetworkService? = null
    private val BASE_URL = "https://api.openweathermap.org"
    private var mRetrofit: Retrofit


    constructor() {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }





    fun getInstance(): NetworkService {
        if (mInstance == null) {
            mInstance = NetworkService()
        }
        return mInstance as NetworkService
    }






    fun getApiWeather(): ApiWeather {
        return mRetrofit.create(ApiWeather::class.java!!)
    }


}
