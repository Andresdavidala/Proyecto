package com.example.proyecto

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import java.io.InputStreamReader
import kotlin.random.Random

class FloatingWindow: Service() {
    private lateinit var floatView: ViewGroup
    private  lateinit var floatWindowLayoutParams: WindowManager.LayoutParams
    private var LAYOUT_TYPE: Int? = null
    private lateinit var windowManager: WindowManager
    private lateinit var edtDes: EditText
    private lateinit var edtDes2: TextView
    private lateinit var cardView: CardView
    private lateinit var btnMax: Button

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val metrics = applicationContext.resources.displayMetrics
        val width =  metrics.widthPixels
        val height = metrics.heightPixels

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        floatView = inflater.inflate(R.layout.floatingwindow, null) as ViewGroup
        btnMax = floatView.findViewById(R.id.btnMax)
        edtDes = floatView.findViewById(R.id.edt_description)
        cardView = floatView.findViewById(R.id.cardContainer)
        edtDes2 = floatView.findViewById(R.id.edt_description2)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }
        else LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST

        floatWindowLayoutParams = WindowManager.LayoutParams(
            (width * 0.6f).toInt(),
            (height * 0.23f).toInt(),
            LAYOUT_TYPE!!,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        floatWindowLayoutParams.gravity = Gravity.CENTER

        floatWindowLayoutParams.x = 0
        floatWindowLayoutParams.y = 0


        windowManager.addView(floatView, floatWindowLayoutParams)

//random words ↓

        val fileInputStream = openFileInput("myfile.txt")
        val inputReader = InputStreamReader(fileInputStream)
        val output = inputReader.readText().trimEnd()


        var words = output.split(", ")



        var mapWords: MutableMap<String, String> =mutableMapOf()
        var numword = 0;
        for(i in words.indices){
            try {
                mapWords[words[numword]] = words[numword + 1]
                numword += 2
            }catch (e:IndexOutOfBoundsException){

            }
        }
        Log.d("TAG4", mapWords.toString())

        var wordtras = mapWords[valorRandom(mapWords, edtDes2).toString()]
        Log.d("MAP", wordtras.toString())
        btnMax.setOnClickListener {

            if(wordtras == edtDes.text.toString().trim()){
                stopSelf()
                windowManager.removeView(floatView)

                Log.d("MAP", wordtras.toString())
            }else{

            }
            edtDes.setText("")


        }

        //↑
        floatView.setOnTouchListener(object : View.OnTouchListener {
            val updatedFloatWindowLayoutParams = floatWindowLayoutParams
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event!!.action){
                    MotionEvent.ACTION_DOWN -> {
                        x = updatedFloatWindowLayoutParams.x.toDouble()
                        y = updatedFloatWindowLayoutParams.y.toDouble()
                        px = event.rawX.toDouble()
                        py = event.rawY.toDouble()

                    }
                    MotionEvent.ACTION_MOVE -> {
                        updatedFloatWindowLayoutParams.x = (x + event.rawX - px).toInt()
                        updatedFloatWindowLayoutParams.y = (y + event.rawY - py).toInt()

                        windowManager.updateViewLayout(floatView, updatedFloatWindowLayoutParams)

                    }
                }
                return false
            }

        })

        edtDes.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                edtDes.isCursorVisible = true
                val updatedFloatParamsFlag  = floatWindowLayoutParams
                updatedFloatParamsFlag.flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN

                windowManager.updateViewLayout(floatView, updatedFloatParamsFlag)
                return false
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()

        windowManager.removeView(floatView)
    }

    private fun valorRandom(mapValor: Map<String,String>, editEvaluar: TextView):String{
        var randoMap = mapValor.entries.elementAt(Random.nextInt(mapValor.size))
        editEvaluar.setText(randoMap.key)
        return randoMap.key
    }
}