package com.example.betsdice.model

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.example.betsdice.model.constant.MAIN
import com.example.betsdice.model.constant.listMoneyForReplenish
import com.example.betsdice.model.constant.listUrlImageDice
import com.example.betsdice.model.constant.mapDice
import kotlinx.coroutines.Job
import java.util.Calendar
import java.util.Locale

class Repository {

    var listDice = mutableListOf("","","","")

    var job:Job = Job()

    var winMoney = 0
    var level = 0

    fun createJsonFile(namePhone:String,locale:String,id:String): String {
        val json = """
                      {
                       "phone_name": "$namePhone",
                       "locale": "$locale",
                       "unique": "$id"
                      }
                      """.trimIndent()
        return json
    }

    //функция проверки победы левых костей
    fun checkLeftDiceWin(): Boolean {
        return mapDice[listDice[0]]!! + mapDice[listDice[1]]!! > mapDice[listDice[2]]!! + mapDice[listDice[3]]!!
    }

    //функция проверки победы правых костей
    fun checkRightDiceWin(): Boolean {
        return mapDice[listDice[0]]!! + mapDice[listDice[1]]!! < mapDice[listDice[2]]!! + mapDice[listDice[3]]!!
    }

    //функция проверки ничейного результата
    fun checkDrawDice(): Boolean {
        return mapDice[listDice[0]]!! + mapDice[listDice[1]]!! == mapDice[listDice[2]]!! + mapDice[listDice[3]]!!
    }

    fun checkNewRecord(){
        if(level > getRecordInGame()){
            updateRecordInGame(level)
        }
    }

    //функция получения денежного счета
    fun getMoneyInCashAccount(): Int {
        return MAIN.getMoneyInCashAccount()
    }

    //функция добавления денег к счету
    fun addMoneyInCashAccount(money:Int){
        MAIN.addMoneyInCashAccount(money = money)
    }

    //функция уменьшения денег на счете
    fun minusMoneyInCashAccount(money:Int){
        MAIN.minusMoneyInCashAccount(money = money)
    }

    //функция получения последнего дня, когда был пополнен счет
    private fun getLastDay(): String {
        return MAIN.getLastDay()
    }

    //функция получения текущей даты
    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$day.$month.$year"
    }

    //функция установки последнего дня, когда был пополнен счет
    fun setLastDay(day:String){
        MAIN.setLastDay(day = day)
    }

    //функция показа короткого всплывающего сообщения
    fun shortToast(context: Context, message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    //функция показа длительного всплывающего сообщения
    fun longToast(context: Context,message:String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    //функция проверки прошел ли день после последнего пополнения счета
    @SuppressLint("NewApi")
    fun checkTodayAndLastDay(): Boolean {
        return getCurrentDate() != getLastDay()
    }

    //функция выхода из приложения
    fun exitingTheApplication(){
        MAIN.finishAffinity()
    }

    //функция получения рекорда в игре
    fun getRecordInGame(): Int {
        return MAIN.getRecordInGame()
    }

    //функция обновления рекорда в игре
    fun updateRecordInGame(record:Int){
        MAIN.updateRecordInGame(record = record)
    }

    //функция получения денег во время пополнения
    fun getRandomMoneyForReplenish(): Int {
        return listMoneyForReplenish.shuffled()[0]
    }

    //функция получения 4 случайных игральных костей
    fun getRandomUrlImageDice(){
        listDice[0] = listUrlImageDice.shuffled()[0]
        listDice[1] = listUrlImageDice.shuffled()[0]
        listDice[2] = listUrlImageDice.shuffled()[0]
        listDice[3] = listUrlImageDice.shuffled()[0]
    }

}