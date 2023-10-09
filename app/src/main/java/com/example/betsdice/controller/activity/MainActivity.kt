package com.example.betsdice.controller.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.example.betsdice.R
import com.example.betsdice.controller.fragments.GameFragment
import com.example.betsdice.controller.fragments.SettingsFragment
import com.example.betsdice.databinding.ActivityMainBinding
import com.example.betsdice.model.constant.APP_PREFERENCES
import com.example.betsdice.model.constant.LAST_DAY
import com.example.betsdice.model.constant.MAIN
import com.example.betsdice.model.constant.MY_CASH_ACCOUNT
import com.example.betsdice.model.constant.MY_RECORD

class MainActivity : AppCompatActivity() {

    private val settingsFragment = SettingsFragment()
    private val gameFragment = GameFragment()

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //полноэкранный режим
        @Suppress("DEPRECATION")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        MAIN = this

        //смена фрагментов на экране путем нажатия на кнопки меню
        replaceFragment(gameFragment)
        @Suppress("DEPRECATION")
        binding.idBottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.id_menu_game ->replaceFragment(gameFragment)
                R.id.id_menu_settings ->replaceFragment(settingsFragment)
            }
            true
        }


    }

    //функция замены фрагмента на экране
    private fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container,fragment)
        transaction.commit()
    }

    //функция добавления денег к счету
    fun addMoneyInCashAccount(money:Int){
        val preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit()
            .putInt(MY_CASH_ACCOUNT,getMoneyInCashAccount()+money)
            .apply()
    }

    //функция получения рекорда в игре
    fun getRecordInGame(): Int {
        return getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE).getInt(MY_RECORD,0)
    }

    //функция обновления рекорда в игре
    fun updateRecordInGame(record:Int){
        val preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit()
            .putInt(MY_RECORD,record)
            .apply()
    }

    //функция получения денежного счета
    fun getMoneyInCashAccount(): Int {
        return getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE).getInt(MY_CASH_ACCOUNT,0)
    }

    //функция уменьшения денег на счете
    fun minusMoneyInCashAccount(money:Int){
        val preferences = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE)
        preferences.edit()
            .putInt(MY_CASH_ACCOUNT,getMoneyInCashAccount()-money)
            .apply()
    }

    //функция получения последнего дня,когда был пополнен счет
    fun getLastDay(): String {
        val preferences = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE).getString(LAST_DAY,"")
        return preferences ?: ""
    }

    //функция установки последнего дня,когда был пополнен счет
    fun setLastDay(day:String){
        val preferences = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE)
        preferences.edit()
            .putString(LAST_DAY,day)
            .apply()
    }

}