package com.ecostudio.useful.weatherinfo.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class StartActivity : AppCompatActivity() {

    val SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nextActivity(checkSettingsFile())
    }

    //Check city variable, cause by that makes download
    private fun checkSettingsFile():Boolean{
        var settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        var city = settingsPreferences.getString("main_city", null)

        return city != null
    }


    //Starts next activity, settings or main
    private fun nextActivity(result: Boolean){
        if(!result){
            var settings = Intent(this, ActivitySettings::class.java)
            startActivity(settings)
        }
        else{
            var main = Intent(this, MainActivity::class.java)
            startActivity(main)
        }
    }

}
