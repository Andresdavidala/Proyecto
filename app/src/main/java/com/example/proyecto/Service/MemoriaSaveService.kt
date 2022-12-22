package com.example.proyecto.Service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import android.view.*
import android.view.animation.LinearInterpolator
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import com.example.proyecto.R
import com.example.proyecto.Recycler.MemoriWords
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.SettingActivity
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MemoriaSaveService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
    private lateinit var bubbleView: ViewGroup
    private lateinit var bubbleViewParams: WindowManager.LayoutParams
    private lateinit var cardView: ViewGroup
    private lateinit var cardViewParams: WindowManager.LayoutParams


    private var LAYOUT_TYPE: Int? = null
    private lateinit var windowManager: WindowManager
    private lateinit var bubbleIv: ImageView
    private lateinit var cardContain: CardView
    private lateinit var btnSave: ImageView
    private lateinit var etWo: EditText

    val velocityTracker = VelocityTracker.obtain()


    override fun onCreate() {
        super.onCreate()

        val metrics = applicationContext.resources.displayMetrics
        val width =  metrics.widthPixels
        val height = metrics.heightPixels
        val widthC = metrics.widthPixels
        val heightC = metrics.heightPixels

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val inflaterCard = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        bubbleView= inflater.inflate(R.layout.bubble, null) as ViewGroup
        bubbleIv = bubbleView.findViewById(R.id.ivBubble)
        bubbleIv.setImageResource(R.drawable.iconsvm)

        cardView = inflaterCard.inflate(R.layout.savememori, null) as ViewGroup //create new layout for problems
        cardContain = cardView.findViewById(R.id.crdSaveMem)
        btnSave = cardView.findViewById(R.id.btnSmemo)
        btnSave.setImageResource(R.drawable.save)
        etWo = cardView.findViewById(R.id.etSaveMem)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        }else{
            LAYOUT_TYPE = WindowManager.LayoutParams.TYPE_TOAST
        }

        //params del bubble
        bubbleViewParams = WindowManager.LayoutParams(
            (width * 0.2f).toInt(),
            (height * 0.10f).toInt(),
            LAYOUT_TYPE!!,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        //↑

        //params del card
        cardViewParams = WindowManager.LayoutParams(
            (widthC),
            (heightC * 0.23f).toInt(),
            LAYOUT_TYPE!!,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        //↑
        bubbleViewParams.gravity = Gravity.TOP
        bubbleViewParams.x = 0
        bubbleViewParams.y = 0


        //cardView
        cardViewParams.gravity = Gravity.CENTER
        cardViewParams.x = 0
        cardViewParams.y = 0

        //↑

        //↓

        bubbleView.setOnTouchListener(object : View.OnTouchListener {
            val updatedFloatWindowLayoutParams = bubbleViewParams
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0


            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                cardView.alpha = 0f
                cardView.visibility = View.VISIBLE
                velocityTracker.addMovement(event)
                velocityTracker.computeCurrentVelocity(1000)
                val yVelocity = velocityTracker.yVelocity


                cardView.animate()
                    .alpha(1f)
                    .setInterpolator(LinearInterpolator())
                    .setDuration(500)
                    .start()


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

                        if(yVelocity>= 13823.675){

                            windowManager.removeView(bubbleView)
                            stopForeground(true)
                            try {
                                windowManager.removeView(cardView)
                            }catch (_: Exception){}
                        }
                        windowManager.updateViewLayout(bubbleView, updatedFloatWindowLayoutParams)

                    }
                }
                return false
            }

        })

        cardView.setOnTouchListener(object: View.OnTouchListener{
            val updateFloatWindows = cardViewParams
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when(p1!!.action){
                    MotionEvent.ACTION_DOWN -> {
                        x = updateFloatWindows.x.toDouble()
                        y = updateFloatWindows.y.toDouble()
                        px = p1.rawX.toDouble()
                        py = p1.rawY.toDouble()

                    }
                    MotionEvent.ACTION_MOVE -> {
                        updateFloatWindows.x = (x + p1.rawX - px).toInt()
                        updateFloatWindows.y = (y + p1.rawY - py).toInt()

                        windowManager.updateViewLayout(cardView, updateFloatWindows)

                    }
                }
                return false
            }


        })
        //

        etWo.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                etWo.isCursorVisible = true
                val updatedFloatParamsFlag  = cardViewParams
                updatedFloatParamsFlag.flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN

                windowManager.updateViewLayout(cardView, updatedFloatParamsFlag)
                return false
            }

        })

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        windowManager.addView(bubbleView, bubbleViewParams)
        bubbleView.setOnClickListener {
            bubbleIv.isSelected = !bubbleIv.isSelected
            if(bubbleIv.isSelected){
                windowManager.addView(cardView, cardViewParams)
            }else{

                windowManager.removeView(cardView)


            }
        }

        //Broadcast
        class NotificationReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
            }
        }

        //↓ForegroundService

        createNotification()
        val intentForeGroun = Intent(this, SettingActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intentForeGroun, 0)
        val notificacion: Notification = NotificationCompat.Builder(this, "channel1")
            .setContentText("Esta ejecutandose Proyecto")
            .setContentTitle("Burbuja esta en ejecución")
            .setSmallIcon(R.drawable.iconsvm)
            .setContentIntent(pendingIntent).build()

        val receiver = NotificationReceiver()
        val filter = IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED)
        registerReceiver(receiver, filter)

        //↑

        dataWordProvider.memorisWords.clear()
        var txtFile = openFileOutput("memorias.txt", MODE_APPEND)

        val openFile = openFileInput("memorias.txt")
        val inputReader = InputStreamReader(openFile)
        val data = inputReader.readText().trimEnd()
        val datatoList = data.split("☼○ ")
        var contWord = 0

        if (data.isNotEmpty()) {
            for (i in datatoList.indices) {

                dataWordProvider.memorisWords.add(
                    MemoriWords(
                        datatoList[contWord]
                    )
                )

                contWord += 1

            }
        } else {
        }
        startForeground(1, notificacion)
        fun saveWord(){
            val campoMem = etWo.text.toString().trim()

            try {

                if(etWo.text?.isEmpty()==true || TextUtils.isEmpty(campoMem) ){
                    Toast.makeText(baseContext, "Debe llenar el campo de texto", Toast.LENGTH_SHORT).show()
                }else {
                    dataWordProvider.memorisWords.add(MemoriWords(campoMem))
                    Toast.makeText(baseContext, "Palabra guardada correctamente!", Toast.LENGTH_SHORT)
                        .show()
                    etWo.setText("")

                }
                //guardar en un textfile integrado dentro de la app↓

                txtFile = openFileOutput("memorias.txt", Context.MODE_PRIVATE)
                val outputWriter = OutputStreamWriter(txtFile)

                //escritura de datos ↓

                for (i in dataWordProvider.memorisWords.indices) {
                    outputWriter.write("${dataWordProvider.memorisWords[i].memorias.trim()}☼○ ")


                }

                outputWriter.flush()
                outputWriter.close()

                etWo.clearFocus()





            } catch (e: java.lang.Exception) {
                Toast.makeText(baseContext, "Something Wrong", Toast.LENGTH_SHORT).show()

            }

        }

        btnSave.setOnClickListener {
            saveWord()
        }

        etWo.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                saveWord()

            }
            false
        })
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        stopForeground(true)

        try {
            velocityTracker.recycle()
            windowManager.removeView(bubbleView)
            windowManager.removeView(cardView)

        }catch (_: Exception){

        }
    }

    private fun createNotification(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notificationChanel = NotificationChannel("channel1","Foreground Service", NotificationManager.IMPORTANCE_DEFAULT)
            val manager: NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notificationChanel)

        }
    }
}