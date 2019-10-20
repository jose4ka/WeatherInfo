package com.ecostudio.useful.weatherinfo.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.ecostudio.useful.weatherinfo.R
import kotlinx.android.synthetic.main.activity_settings.*
import java.text.FieldPosition

class ActivitySettings : AppCompatActivity(){

    val SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES"


    override fun onCreate(savedInstanceState: Bundle?) {
        checkTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        loadSettings()
    }

    //Create action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_bar, menu)
        return true
    }

    //Action bar listener
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings_back -> {
                save()
                var restartApp = Intent(this, StartActivity::class.java)
                startActivity(restartApp)
            }
        }
        return true
    }


    fun checkTheme(){
        val settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        var isNightModeEnadled = settingsPreferences.getBoolean("NIGHT_MODE", false)

        if(isNightModeEnadled){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(R.style.AppTheme)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.AppThemeNight)
        }
    }



    /*
    Save data
    units - name of units
    units_points - position of selected element (need for load settings)
     */
    private fun putUnits (units:String, position: Int){
        val settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        val editor = settingsPreferences.edit()
        editor.putString("units", units)
        editor.putInt("units_position", position)
        editor.apply()
    }

    //Save city is shared preferences
    private fun putCity (city:String){
        val settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        val editor = settingsPreferences.edit()
        editor.putString("main_city", city)
        editor.apply()
    }


    //Check field for void, and save data
    private fun save(){
        if(!etSettingsMainCity.text.toString().isEmpty()){
            putCity(etSettingsMainCity.text.toString())
            putUnits(unitsSpinner.selectedItem.toString(), unitsSpinner.selectedItemPosition)
        }
        else{Toast.makeText(this, R.string.enter_city_name, Toast.LENGTH_SHORT).show()}
    }




    /*
    Load saved data
    For city - string
    For spinner - Int, cause ve set selected position
     */
    private fun loadSettings(){
        var settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

        var city = settingsPreferences.getString("main_city", null)
        var units = settingsPreferences.getInt("units_position", 0)

        //Set values for screen elements
        etSettingsMainCity.setText(city)
        unitsSpinner.setSelection(units)
    }
}






