package com.example.proyecto

import android.app.Notification.Action
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.proyecto.Recycler.dataWordProvider
import java.io.FileInputStream
import java.io.InputStreamReader
import kotlin.properties.Delegates
import kotlin.random.Random

class ToastService(): Service(){
    private lateinit var mainHandler: Handler
    private lateinit var runn: Runnable
    var milisecundos by Delegates.notNull<Int>()

    var mapWords: MutableMap<String, String> = mutableMapOf()
    var numword = 0;


    init {
        Log.d("datos","Service Toast running ")
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mainHandler = Handler(Looper.getMainLooper())
        //para la lectura del .txt
        val fileInputStream: FileInputStream = openFileInput("myfile.txt")
        val inputReader = InputStreamReader(fileInputStream)
        val output = inputReader.readText().trimEnd()
        val words = output.split(", ")

        //↑
        val numPickHour = intent?.getIntExtra("numberPickerHour", 0)
        val numPicMinutes = intent?.getIntExtra("numberPickerMinutes", 0)


        //↓para broadcast

        val intent = Intent("sendMili")



        //↑
        //handler y runnable for Toast

        for (i in words.indices) {
            try {
                mapWords[words[numword]] = words[numword + 1]
                numword += 2
            } catch (_: IndexOutOfBoundsException) {

            }
        }
        runn = object : Runnable {
            override fun run() {

                try{
                    PairWordGenerate()

                    mainHandler.postDelayed(this, milisecundos.toLong())
                    Log.d("datos", milisecundos.toString())
                }catch (_ : Exception){
                    Toast.makeText(baseContext,"No se encontraron palabras", Toast.LENGTH_LONG).show()
                }
            }
        }
        val numHora = numPickHour?.times(60)

        val numMinute = numPicMinutes

        val minutosTotal = numHora?.let { numMinute?.plus(it) }
        milisecundos = minutosTotal!! * 1000
        if(milisecundos !=0){

            mainHandler.postDelayed(runn, milisecundos.toLong())
        }else{
            Toast.makeText(this, "Tiempo igual a 0 no valido", Toast.LENGTH_LONG).show()
        }


        //intent para enviar valores del serve a la actividad
        intent.putExtra("DATE", milisecundos)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        return super.onStartCommand(intent, flags, startId)

    }


    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(runn)

    }
    private fun PairWordGenerate(){

        val list = dataWordProvider.dataWords
        val data = list.shuffled().take(1)[0]
        Toast.makeText(baseContext, "${data.wordOrg} - ${data.wordTrad}".uppercase().replace(",",""), Toast.LENGTH_LONG).show()


        Log.d("TAGSwDAta", data.toString())


    }

}