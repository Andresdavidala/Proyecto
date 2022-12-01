package com.example.proyecto

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.NumberPicker
import android.widget.Toast
import java.io.FileInputStream
import java.io.InputStreamReader
import kotlin.properties.Delegates
import kotlin.random.Random

class ToastService(): Service(){
    private lateinit var mainHandler: Handler
    private lateinit var runn: Runnable
    var milisecundos by Delegates.notNull<Int>()



    init {
        Log.d("datos","Service Toast running ")
    }

    override fun onCreate() {
        super.onCreate()

        mainHandler = Handler(Looper.getMainLooper())

//        for (i in words.indices) {
//            try {
//                mapWords[words[numword]] = words[numword + 1]
//                numword += 2
//            } catch (_: IndexOutOfBoundsException) {
//
//            }
//        }
//        runn = object : Runnable {
//            override fun run() {
//                Toast.makeText(baseContext, "Hello", Toast.LENGTH_SHORT).show()
//                mainHandler.postDelayed(this, milisecundos.toLong())
//                Log.d("datos", milisecundos.toString())
//            }
//        }
//        val numHora = npTH.value * 60
//
//        val numMinute = npTM.value

//        val minutosTotal =  numMinute + numHora
//        milisecundos = 1 * 1000
//
//        mainHandler.removeCallbacks(runn)
//        mainHandler.postDelayed(runn, milisecundos.toLong())

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
        var words = output.split(", ")

        //↑
        var numPickHour = intent?.getIntExtra("numberPickerHour", 0)
        var numPicMinutes = intent?.getIntExtra("numberPickerMinutes", 0)

        //handler y runnable for Toast
        var mapWords: MutableMap<String, String> = mutableMapOf()
        var numword = 0;
        for (i in words.indices) {
            try {
                mapWords[words[numword]] = words[numword + 1]
                numword += 2
            } catch (_: IndexOutOfBoundsException) {

            }
        }
        runn = object : Runnable {
            override fun run() {
                switchChecked(mapWords)
                mainHandler.postDelayed(this, milisecundos.toLong())
                Log.d("datos", milisecundos.toString())
            }
        }
        val numHora = numPickHour?.times(60)

        val numMinute = numPicMinutes

        val minutosTotal = numHora?.let { numMinute?.plus(it) }
        milisecundos = minutosTotal!! * 1000

        mainHandler.removeCallbacks(runn)
        mainHandler.postDelayed(runn, milisecundos.toLong())
        return super.onStartCommand(intent, flags, startId)

    }


    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(runn)

    }

    private fun switchChecked(mapWords: Map<String,String>){

        var randoMap = mapWords.entries.elementAt(Random.nextInt(mapWords.size))
        Log.d("TAGSw", mapWords.toString())
        Toast.makeText(baseContext, "${randoMap.key.uppercase()} → ${randoMap.value.uppercase()}", Toast.LENGTH_LONG).show()

        Log.d("datos", "funcionando")

    }
}