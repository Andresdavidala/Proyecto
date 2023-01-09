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
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.SettingActivity
import kotlin.properties.Delegates

class ToastService : Service(){
    private lateinit var mainHandler: Handler
    private lateinit var runn: Runnable

    var milisecundos by Delegates.notNull<Int>()
    private lateinit var cardToast: ViewGroup
    private lateinit var cardToastParams: WindowManager.LayoutParams
    private var LAYOUT_TYPE: Int? = null
    private lateinit var windowManager: WindowManager
    private lateinit var tvToast: TextView
    private lateinit var cardContain: CardView
    private lateinit var btnExit: ImageView
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        val metrics = applicationContext.resources.displayMetrics
        val width =  metrics.widthPixels
        val height = metrics.heightPixels

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        cardToast = inflater.inflate(R.layout.toascard, null) as ViewGroup
        cardContain = cardToast.findViewById(R.id.cardToast)
        tvToast = cardToast.findViewById(R.id.tvToast)
        btnExit = cardToast.findViewById(R.id.btnExitToast)
        btnExit.setImageResource(R.drawable.closecardmem)

        LAYOUT_TYPE = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else WindowManager.LayoutParams.TYPE_TOAST
        cardToastParams = WindowManager.LayoutParams(

            (width *0.9f).toInt(),
            (height *0.29f).toInt(),
            LAYOUT_TYPE!!,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT,
        )

        cardToastParams.gravity = Gravity.BOTTOM
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mainHandler = Handler(Looper.getMainLooper())
        //para la lectura del .txt


        //↑
        val numPickHour = intent?.getIntExtra("numberPickerHour", 0)
        val numPicMinutes = intent?.getIntExtra("numberPickerMinutes", 0)

        //handler y runnable for Toast


        //↓ForegroundService

        createNotification()
        val intentForeGroun = Intent(this, SettingActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intentForeGroun, PendingIntent.FLAG_MUTABLE)
        val notificacion: Notification = NotificationCompat.Builder(this, "channel1")
            .setContentText(getText(R.string.ejecutServ))
            .setContentTitle(getText(R.string.notifyToast))
            .setSmallIcon(R.drawable.iconsvm).setOngoing(true)
            .setContentIntent(pendingIntent).build()



        //↑
        btnExit.setOnClickListener {
            windowManager.removeView(cardToast)


            //↓codigo para volver a llamar al postdelayed debido a que se lo cancela una vez aparece la ventana flotante
            mainHandler.postDelayed(runn, milisecundos.toLong())
        }

        runn = object : Runnable {
            override fun run() {

                try{
                    PairWordGenerate(tvToast)
                    windowManager.addView(cardToast, cardToastParams)
                    mainHandler.postDelayed(this, milisecundos.toLong())

                }catch (_ : Exception){
                    Toast.makeText(baseContext,R.string.toastServicefloat, Toast.LENGTH_SHORT).show()
                }
                mainHandler.postDelayed(this,milisecundos.toLong())
                mainHandler.removeCallbacks(runn)
            }
        }



        val numHora = numPickHour?.times(60)

        val minutosTotal = numHora?.let { numPicMinutes?.plus(it) }
        milisecundos = minutosTotal!! * 60000
        if(milisecundos !=0){

            if(dataWordProvider.dataWords.size != 0){
                mainHandler.postDelayed(runn, milisecundos.toLong())
                startForeground(1, notificacion)
            }else{
                Toast.makeText(this, R.string.toastServicefloat, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, R.string.toastServicefloat2, Toast.LENGTH_LONG).show()
        }


        //intent para enviar valores del serve a la actividad
//        intent.putExtra("DATE", milisecundos)
//
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//        START_STICKY
//        return super.onStartCommand(intent, flags, startId)

        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(runn)
        stopForeground(true)
        stopSelf()

    }
    @SuppressLint("SetTextI18n")
    private fun PairWordGenerate(edtTvToast: TextView){
        val list = dataWordProvider.dataWords
        val data = list.shuffled().take(1)[0]
//        Toast.makeText(baseContext, "${data.wordOrg} - ${data.wordTrad}".uppercase().replace("☼○",""), Toast.LENGTH_LONG).show()
        edtTvToast.text = "${data.wordOrg} - ${data.wordTrad}".uppercase().replace("☼○","")
    }

    private fun createNotification(){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notificationChanel = NotificationChannel("channel1","Foreground Service", NotificationManager.IMPORTANCE_DEFAULT)
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChanel)

        }
    }
}