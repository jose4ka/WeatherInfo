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

    //Проверка по большей степени идёт по городу, т.к. по нему идёт вся загрузка
    private fun checkSettingsFile():Boolean{
        var settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        var city = settingsPreferences.getString("main_city", null)

        return city != null
    }


    //Запускает нужную активность, в зависимости от переданного значения
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
