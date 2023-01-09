package com.example.proyecto.Service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import com.example.proyecto.R
import com.example.proyecto.Recycler.MemoriWords
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.SettingActivity
import kotlin.properties.Delegates

class memoriasService: Service() {
    private lateinit var mainHandler: Handler
    private lateinit var runn: Runnable
    var milisecundos by Delegates.notNull<Int>()
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private lateinit var cardMemorView: ViewGroup
    private lateinit var cardMemViewParams: WindowManager.LayoutParams
    private var LAYOUT_TYPE: Int? = null
    private lateinit var windowManager: WindowManager
    private lateinit var tvMemorias: TextView
    private lateinit var cardContain: CardView
    private lateinit var btnExit: ImageView


    @SuppressLint("InflateParams")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        val metrics = applicationContext.resources.displayMetrics
        val width =  metrics.widthPixels
        val height = metrics.heightPixels

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        cardMemorView = inflater.inflate(R.layout.cardmemori, null) as ViewGroup
        cardContain = cardMemorView.findViewById(R.id.cardMemorias)
        tvMemorias = cardMemorView.findViewById(R.id.tvMemorias)
        btnExit = cardMemorView.findViewById(R.id.btnExit)
        btnExit.setImageResource(R.drawable.closecardmem)



        LAYOUT_TYPE = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else WindowManager.LayoutParams.TYPE_TOAST
        cardMemViewParams = WindowManager.LayoutParams(

            (width *0.9f).toInt(),
            (height *0.29f).toInt(),
            LAYOUT_TYPE!!,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT,
        )

        cardMemViewParams.gravity = Gravity.CENTER





    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        mainHandler = Handler(Looper.getMainLooper())
        tvMemorias.movementMethod = ScrollingMovementMethod()

        val numPickHour = intent?.getIntExtra("numberPickerHourWMem", 0)
        val numPicMinutes = intent?.getIntExtra("numberPickerMinutesWMem", 0)


        createNotification()
        val intentForeGroun = Intent(this, SettingActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intentForeGroun, PendingIntent.FLAG_MUTABLE)
        val notificacion: Notification = NotificationCompat.Builder(this, "channel1")
            .setContentText(getText(R.string.ejecutServ))
            .setContentTitle(getText(R.string.notifyMemServ))
            .setSmallIcon(R.drawable.iconsvm)
            .setContentIntent(pendingIntent).build()

        btnExit.setOnClickListener {
            windowManager.removeView(cardMemorView)


            //↓codigo para volver a llamar al postdelayed debido a que se lo cancela una vez aparece la ventana flotante
            mainHandler.postDelayed(runn, milisecundos.toLong())
        }

        //↑

        runn = object : Runnable{
            override fun run() {
                try{
                   valorMemoriRam(dataWordProvider.memorisWords, tvMemorias)
                    windowManager.addView(cardMemorView, cardMemViewParams)

                }catch (_: Exception){
                    Toast.makeText(baseContext,R.string.toastServicefloat, Toast.LENGTH_LONG).show()
                }
                mainHandler.postDelayed(this,milisecundos.toLong())
                mainHandler.removeCallbacks(runn)
            }

        }



        val numHora = numPickHour?.times(60)

        val numMinute = numPicMinutes

        val minutosTotal = numHora?.let { numMinute?.plus(it) }
        milisecundos = minutosTotal!! * 60000

        if(milisecundos != 0){
            if(dataWordProvider.memorisWords.size != 0){
                mainHandler.postDelayed(runn, milisecundos.toLong())
                startForeground(4, notificacion)
            }else{
                Toast.makeText(this, R.string.toastServicefloat, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, R.string.toastServicefloat2, Toast.LENGTH_LONG).show()
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
            windowManager.removeView(cardMemorView)
        }catch (_: Exception){

        }
    }

    private fun valorMemoriRam(valList:MutableList<MemoriWords>, editEvaluar: TextView): String {
        val list = valList.shuffled().take(1)[0]
        val wordReturn = list.memorias.replace("☼○", "")
        editEvaluar.text = wordReturn
        editEvaluar.scrollTo(0,0)
        return wordReturn
    }

    private fun createNotification(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notificationChanel = NotificationChannel("channel1","Foreground Service", NotificationManager.IMPORTANCE_DEFAULT)
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChanel)

        }
    }
}