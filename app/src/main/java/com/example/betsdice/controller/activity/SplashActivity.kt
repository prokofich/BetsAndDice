package com.example.betsdice.controller.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import coil.size.Scale
import com.example.betsdice.databinding.ActivitySplashBinding
import com.example.betsdice.model.Repository
import com.example.betsdice.model.ResponseWebView
import com.example.betsdice.model.constant.APP_PREFERENCES
import com.example.betsdice.model.constant.ID
import com.example.betsdice.model.constant.url_image_splash
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.Locale
import java.util.UUID

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var binding: ActivitySplashBinding? = null
    private var repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        //загрузка фоновой картинки
        binding?.idSplashImg?.load(url_image_splash){ scale(Scale.FILL) }

        val namePhone = Build.MODEL.toString()
        val locale = Locale.getDefault().displayLanguage.toString()
        val id: String

        if (getMyId()!=""){
            id = getMyId()
        }else{
            id = UUID.randomUUID().toString()
            setMyId(id)
        }

        val client = OkHttpClient()
        val url = "http://37.27.9.28/splash.php"

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = repository.createJsonFile(namePhone, locale, id).toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        //отправка POST запроса на сервер
        client.newCall(request).enqueue(object :Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val responseFromServer = Gson().fromJson(responseData,ResponseWebView::class.java)
                when(responseFromServer.url){
                    "no" ->     { goToMainPush() }
                    "nopush" -> { goToMainNoPush() }
                    else ->     { goToWeb(responseFromServer.url) }
                }
            }
        })

    }

    //функция получения ID
    private fun getMyId():String{
        val preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).getString(ID,"")
        return preferences ?: ""
    }

    //функция установки ID
    private fun setMyId(id:String){
        val preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit()
            .putString(ID,id)
            .apply()
    }

    private fun goToMainPush() {
        startActivity(Intent(this@SplashActivity,MainActivity::class.java))
    }

    private fun goToMainNoPush() {
        startActivity(Intent(this@SplashActivity,MainActivity::class.java))
    }

    private fun goToWeb(url:String) {
        val intent = Intent(this@SplashActivity,WebViewActivity::class.java)
        intent.putExtra("url",url)
        startActivity(intent)
    }

}