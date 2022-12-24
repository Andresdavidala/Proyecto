package com.example.proyecto.Service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.proyecto.R
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.SettingActivity
import kotlin.properties.Delegates

class ToastService(): Service(){
    private lateinit var mainHandler: Handler
    private lateinit var runn: Runnable
    var milisecundos by Delegates.notNull<Int>()


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mainHandler = Handler(Looper.getMainLooper())
        //para la lectura del .txt


        //↑
        val numPickHour = intent?.getIntExtra("numberPickerHour", 0)
        val numPicMinutes = intent?.getIntExtra("numberPickerMinutes", 0)


//        //↓para broadcast
//
//        val intent = Intent("sendMili")
//
//
//
//        //↑
        //handler y runnable for Toast


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


        runn = object : Runnable {
            override fun run() {

                try{
                    PairWordGenerate()

                    mainHandler.postDelayed(this, milisecundos.toLong())
                }catch (_ : Exception){
                    Toast.makeText(baseContext,R.string.toastServicefloat, Toast.LENGTH_SHORT).show()
                }
            }
        }
        val numHora = numPickHour?.times(60)

        val numMinute = numPicMinutes

        val minutosTotal = numHora?.let { numMinute?.plus(it) }
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
        START_STICKY
        return super.onStartCommand(intent, flags, startId)

    }


    override fun onDestroy() {
        super.onDestroy()
        mainHandler.removeCallbacks(runn)
        stopForeground(true)
        stopSelf()

    }
    private fun PairWordGenerate(){
        val list = dataWordProvider.dataWords
        val data = list.shuffled().take(1)[0]
        Toast.makeText(baseContext, "${data.wordOrg} - ${data.wordTrad}".uppercase().replace("☼○",""), Toast.LENGTH_LONG).show()

    }

    private fun createNotification(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notificationChanel = NotificationChannel("channel1","Foreground Service", NotificationManager.IMPORTANCE_DEFAULT)
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChanel)

        }
    }
}