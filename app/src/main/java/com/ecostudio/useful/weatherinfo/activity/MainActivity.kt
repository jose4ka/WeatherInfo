package com.ecostudio.useful.weatherinfo.activity

import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.ecostudio.useful.weatherinfo.R
import com.ecostudio.useful.weatherinfo.retrofit.NetworkService
import com.ecostudio.useful.weatherinfo.retrofit.model.weather.WeatherMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ecostudio.useful.weatherinfo.fragment.FragmentWeatherMainInfo


class MainActivity : AppCompatActivity() {

    lateinit var weatherMain:WeatherMain


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Загружаем дату с сервера
        getDataFromOW("Vladivostok")

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_update -> {
                getDataFromOW("Vladivostok")
            }

            R.id.action_search -> {
                var searchView = item.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

                    override fun onQueryTextChange(p0: String?): Boolean {
                        return true
                    }

                    override fun onQueryTextSubmit(p0: String): Boolean {
                        getDataFromOW(p0)
                        return true
                    }
                })
            }
            R.id.action_settings -> {}
            R.id.action_about_app -> {}
        }
        return true
    }




    //Установка фрагмента с основной информацией
    fun setWeatherInfo(){
        var fragmentWeatherMainInfo = FragmentWeatherMainInfo.newInstance(
            weatherMain.name,
            weatherMain.main.temp,
            weatherMain.main.temp_max,
            weatherMain.main.temp_min,
            weatherMain.main.pressure,
            weatherMain.main.humidity)

        var ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.mainFrame, fragmentWeatherMainInfo)
        ft.commit()

    }



    //Если есть соединение с интернетом - загружаем данные
    fun getDataFromOW(cityName: String){
        if(verifyAvailableNetwork(this) == true){
            var loadingAsync = LoadingAsync(cityName)
            loadingAsync.execute()
        }
        else{Toast.makeText(applicationContext, "Check internet connection or try later!", Toast.LENGTH_SHORT).show()}
    }

    //Проверка на наличие соединения с интернетом
    fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }




    //Async для загрузки погоды с сервера
    internal inner class LoadingAsync (var cityName: String) : AsyncTask<Void, Void, Void>() {

        lateinit var apiKey:String

        override fun onPreExecute() {
            super.onPreExecute()
            apiKey = resources.getString(R.string.api_key)
        }

        override fun doInBackground(vararg voids: Void): Void? {

            var networkService: NetworkService = NetworkService()

            networkService.getInstance()
                .getApiWeather()
                .getWeather(cityName, apiKey, "metric")
                .enqueue(object: Callback<WeatherMain> {

                    override fun onResponse(call: Call<WeatherMain>, response: Response<WeatherMain>) {
                        val gettingObject = response.body()

                        Log.d("NETWORK_TAG", "REQUEST: " + call.request().toString())
                        Log.d("NETWORK_TAG", "IS EXECUTED: " + call.isExecuted())
                        Log.d("NETWORK_TAG", "IS CANCELED: " + call.isCanceled())

                        if (gettingObject != null) {
                            Log.d("NETWORK_TAG", "SUCCESSFUL!")
                            Log.d("NETWORK_TAG", gettingObject!!.toString())

                            Toast.makeText(getApplicationContext(), "Successful!", Toast.LENGTH_SHORT).show()
                            weatherMain = gettingObject
                            setWeatherInfo()


                        } else {
                            Log.d("NETWORK_TAG", "DATA IS NULL!")
                            Toast.makeText(getApplicationContext(), "DATA IS NULL!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<WeatherMain>, t: Throwable) {
                        Toast.makeText(getApplicationContext(), "ERROR!", Toast.LENGTH_SHORT).show()
                        Log.d("NETWORK_TAG", t.message)
                    }
                })

            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
        }

    }






}
