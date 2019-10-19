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
import com.ecostudio.useful.weatherinfo.R
import kotlinx.android.synthetic.main.activity_settings.*
import java.text.FieldPosition

class ActivitySettings : AppCompatActivity(){

    val SETTINGS_PREFERENCES = "SETTINGS_PREFERENCES"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initializeElements()
        loadSettings()
    }

    //Создаём action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_bar, menu)
        return true
    }

    //Обработчик нажатий на элементы бара
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings_back -> {
                var restartApp = Intent(this, StartActivity::class.java)
                startActivity(restartApp)
            }
        }
        return true
    }


    //Инициализируются элементы экрана
    private fun initializeElements(){
        btnSave.setOnClickListener { save(); }
    }

    /*
    Сохраняем переданные данные
    units - само название единиц измерений
    units_points - номер выбранной позиции (нужен для загрузки настроек)
     */
    private fun putUnits (units:String, position: Int){
        val settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        val editor = settingsPreferences.edit()
        editor.putString("units", units)
        editor.putInt("units_position", position)
        editor.apply()
    }

    //Сохраняем город в настройки
    private fun putCity (city:String){
        val settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
        val editor = settingsPreferences.edit()
        editor.putString("main_city", city)
        editor.apply()
    }


    //Проверяем поле ввода на пустоту, и сохраняем
    private fun save(){
        if(!etSettingsMainCity.text.toString().isEmpty()){
            putCity(etSettingsMainCity.text.toString())
            putUnits(unitsSpinner.selectedItem.toString(), unitsSpinner.selectedItemPosition)
        }
        else{Toast.makeText(this, "Enter city name!", Toast.LENGTH_SHORT).show()}
    }

    /*
    Загружаем сохраненные значения
    Для города - string
    Для спиннера - Int, т.к. мы устанавливаем позицию выбранного элемента
     */
    private fun loadSettings(){
        var settingsPreferences = getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)

        var city = settingsPreferences.getString("main_city", null)
        var units = settingsPreferences.getInt("units_position", 0)

        //Устанавливаем элементам экрана нужные значения
        etSettingsMainCity.setText(city)
        unitsSpinner.setSelection(units)
    }
}






