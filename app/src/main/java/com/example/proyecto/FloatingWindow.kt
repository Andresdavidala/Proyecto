package com.example.proyecto

import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.Recycler.dataWordProvider
import java.io.InputStreamReader
import kotlin.properties.Delegates

class FloatingWindow: Service() {
    private lateinit var mainHandler: Handler
    private lateinit var runn: Runnable
    var milisecundos by Delegates.notNull<Int>()

    private lateinit var floatView: ViewGroup
    private  lateinit var floatWindowLayoutParams: WindowManager.LayoutParams
    private var LAYOUT_TYPE: Int? = null
    private lateinit var windowManager: WindowManager
    private lateinit var edtDes: EditText
    private lateinit var edtDes2: TextView
    private lateinit var cardView: CardView
    private lateinit var btnMax: Button

    private lateinit var wordTrad: String

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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mainHandler = Handler(Looper.getMainLooper())
        edtDes2.movementMethod = ScrollingMovementMethod()



        val numPickHour = intent?.getIntExtra("numberPickerHourW", 0)
        val numPicMinutes = intent?.getIntExtra("numberPickerMinutesW", 0)

        val fileInputStream = openFileInput("myfile.txt")
        val inputReader = InputStreamReader(fileInputStream)
        val output = inputReader.readText().trimEnd()
        val words = output.split("☼○ ")


        //↓ForegroundService

        createNotification()
        val intentForeGroun = Intent(this, SettingActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intentForeGroun, 0)
        val notificacion: Notification = NotificationCompat.Builder(this, "channel1")
            .setContentText("Esta ejecutandose Proyecto")
            .setContentTitle("Servicio en ejecución")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent).build()


        //↑


        val mapWords: MutableMap<String, String> =mutableMapOf()
        var numword = 0;
        for(i in words.indices){
            try {
                mapWords[words[numword]] = words[numword + 1]
                numword += 2
            }catch (_:IndexOutOfBoundsException){

            }
        }

        runn = object : Runnable{
            override fun run() {
                try{
                    wordTrad = mapWords[valorRam(dataWordProvider.dataWords,edtDes2)]!!
                    windowManager.addView(floatView, floatWindowLayoutParams)

                }catch (_: Exception){
                    Toast.makeText(baseContext,"No se encontraron palabras", Toast.LENGTH_LONG).show()
                }
//                Toast.makeText(baseContext, "Hola", Toast.LENGTH_SHORT).show()
                mainHandler.postDelayed(this,milisecundos.toLong())
                mainHandler.removeCallbacks(runn)
                Log.d("Tagmili", milisecundos.toString())
            }

        }

//        var wordtras = mapWords[valorRandom(mapWords, edtDes2)]

//        Log.d("datosWordTras", wordtras.toString())
        btnMax.setOnClickListener {

            if(wordTrad.replace("☼○", "") == edtDes.text.toString().trim()){
//                stopSelf()

                windowManager.removeView(floatView)

                //↓codigo para volver a llamar al postdelayed debido a que se lo cancela una vez aparece la ventana flotante 
                mainHandler.postDelayed(runn, milisecundos.toLong())
//                Log.d("MAP", wordtras.toString())
            }else{

            }
            edtDes.setText("")


        }

        val numHora = numPickHour?.times(60)

        val numMinute = numPicMinutes

        val minutosTotal = numHora?.let { numMinute?.plus(it) }
        milisecundos = minutosTotal!! * 1000

        if(milisecundos != 0){
            mainHandler.postDelayed(runn, milisecundos.toLong())
            startForeground(1, notificacion)
        }else{
            Toast.makeText(this, "Tiempo igual a 0 no valido", Toast.LENGTH_LONG).show()
        }




        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        stopSelf()
        mainHandler.removeCallbacks(runn)
//        stopSelf()
        try{
            windowManager.removeView(floatView)
        }catch (_: Exception){

        }
    }


    private fun valorRam(valList:MutableList<DataWordsBase>, editEvaluar: TextView): String {
        val list = valList.shuffled().take(1)[0]
        val wordReturn = list.wordOrg
        editEvaluar.text = wordReturn

        Log.d("datosMap", wordReturn)
        return wordReturn
    }

    private fun createNotification(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notificationChanel = NotificationChannel("channel1","Foreground Service", NotificationManager.IMPORTANCE_DEFAULT)
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChanel)

        }
    }
//    private fun valorRandom(mapValor: Map<String,String>, editEvaluar: TextView):String{
//
//        var randoMap = mapValor.entries.elementAt(Random.nextInt(mapValor.size))
//        editEvaluar.setText(randoMap.key)
//        Log.d("datosMap", randoMap.key)
//        return randoMap.key
//    }
}