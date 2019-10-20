package com.ecostudio.useful.weatherinfo.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.ecostudio.useful.weatherinfo.R
import com.ecostudio.useful.weatherinfo.fragment.connection.FragmentConnectionStatus
import com.ecostudio.useful.weatherinfo.fragment.connection.ConnectionStatus
import com.ecostudio.useful.weatherinfo.retrofit.NetworkService
import com.ecostudio.useful.weatherinfo.retrofit.model.weather.WeatherMain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.ecostudio.useful.weatherinfo.fragment.FragmentWeatherMainInfo
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    val SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES"
    val COUNTER_TAG = "COUNTER_TAG"
    val NETWORK_TAG = "NETWORK_TAG"

    var onScreen:Boolean = false // For fragmentTransaction

    var lCityName: String = "" //Load from shared preferences
    var lUnits: String = "" //Load from shared preferences


    var canRemoveConnectionBar:Boolean = false //For counter thread
    var isNightModeEnabled:Boolean = false //For change appTheme


    override fun onCreate(savedInstanceState: Bundle?) {
        checkTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onScreen = true
        loadSettings() //Load settings from shared preferences
        getDataFromOW(lCityName) //Load data from server

    }



    //Create elements on action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_menu, menu)
        return true
    }



    //Action bar items listener
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_update -> {
                getDataFromOW(lCityName)
            }
            R.id.action_settings -> {
                onScreen = false
                var intentSettings = Intent(this, ActivitySettings::class.java)
                startActivity(intentSettings)
            }
            R.id.action_about_app -> {}

            R.id.action_dark_mode -> {

                val settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
                val editor = settingsPreferences.edit()

                if(isNightModeEnabled){
                    editor.putBoolean("NIGHT_MODE", false)
                    editor.apply()
                }
                else {
                    editor.putBoolean("NIGHT_MODE", true)
                    editor.apply()
                }
                restartActivity()
            }
        }
        return true
    }



    private fun checkTheme(){
        val settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        isNightModeEnabled = settingsPreferences.getBoolean("NIGHT_MODE", false)

        if(isNightModeEnabled){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(R.style.AppTheme)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeNight)
        }
    }



    private fun restartActivity(){
        var intent = Intent(this, MainActivity::class.java)
        canRemoveConnectionBar = false
        onScreen = false
        finish()
        startActivity(intent)
    }


    //Place fragment with main data about weather
    fun setWeatherMainInfo(weatherMain:WeatherMain){

        if(onScreen){
            if(weatherMain != null){
                //That like constructor
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
        }

    }



    /*
    Method for place connection status bar on the screen
    We use ConnectionStatus.class to show signal which we need
     */
    fun setConnectionStatusBar(connectionStatus: ConnectionStatus){
        if(onScreen){
            var fragmentConnectionStatus: FragmentConnectionStatus = FragmentConnectionStatus.newInstance(connectionStatus)
            var ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.connectionFrame, fragmentConnectionStatus)
            ft.commit()
        }

    }




    //If we have internet connection - download data
    fun getDataFromOW(cityName: String){
        if(verifyAvailableNetwork(this) == true){
            var loadingAsync = LoadingAsync(cityName)
            loadingAsync.execute()
        }
        else{Toast.makeText(applicationContext, R.string.check_internet_connection, Toast.LENGTH_SHORT).show()}
    }




    //Check internet connection
    private fun verifyAvailableNetwork(activity:AppCompatActivity):Boolean{
        val connectivityManager=activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }




    //load settings from shared preferences
    private fun loadSettings(){
        var settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

        var city = settingsPreferences.getString("main_city", "Moscow")
        var units = settingsPreferences.getString("units", "metric")

        if (city != null && units != null) {
            lCityName = city
            lUnits = units
        }
    }




    //Just method for comfortable start counter
    private fun startRemoveThread(){
       var closeThread = CounterThread()
        closeThread.run()
    }




    //Async для загрузки погоды с сервера
    internal inner class LoadingAsync (var cityName: String) : AsyncTask<Void, Void, Void>() {

        lateinit var apiKey:String


        override fun onPreExecute() {
            super.onPreExecute()
            apiKey = resources.getString(R.string.api_key)
            setConnectionStatusBar(ConnectionStatus.LOADING)
        }

        override fun doInBackground(vararg voids: Void): Void? {

            var networkService: NetworkService = NetworkService()

            networkService.getInstance()
                .getApiWeather()
                .getWeather(lCityName, apiKey, lUnits)
                .enqueue(object: Callback<WeatherMain> {

                    override fun onResponse(call: Call<WeatherMain>, response: Response<WeatherMain>) {
                        val gettingObject = response.body()

                        Log.d(NETWORK_TAG, "REQUEST: " + call.request().toString())
                        Log.d(NETWORK_TAG, "IS EXECUTED: " + call.isExecuted())
                        Log.d(NETWORK_TAG, "IS CANCELED: " + call.isCanceled())

                        if (gettingObject != null) {
                            Log.d(NETWORK_TAG, "SUCCESSFUL!")
                            Log.d(NETWORK_TAG, gettingObject!!.toString())

                            setWeatherMainInfo(gettingObject)
                            setConnectionStatusBar(ConnectionStatus.IS_CONNECTED)
                            canRemoveConnectionBar = true

                        } else {
                            setConnectionStatusBar(ConnectionStatus.IS_NOT_CONNECTED)
                        }
                    }

                    override fun onFailure(call: Call<WeatherMain>, t: Throwable) {
                        setConnectionStatusBar(ConnectionStatus.IS_NOT_CONNECTED)
                        Log.d(NETWORK_TAG, t.message)
                    }

                })

            //'doInBackground' runs in other thread -> starts counter thread in that method (that do not stop main thread)
            startRemoveThread()

            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
        }

    }




    //Counter which remove connection status bar
    inner class CounterThread:Thread(){
        override fun run() {
            super.run()
            try {
                if(onScreen){
                    //Wait while variable 'canRemoveConnectionBar' is false
                    while (!canRemoveConnectionBar && onScreen){/*Log.d(COUNTER_TAG, "WAIT")*/}
                    sleep(3000)  //Remove connection bar after 3 seconds
                    setConnectionStatusBar(ConnectionStatus.GONE)
                    canRemoveConnectionBar = true //Return 'true' variable, for successful repeat
                }

            }
            catch (e:Exception){}
        }
    }






}
