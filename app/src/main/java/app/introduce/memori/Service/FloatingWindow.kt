package app.introduce.memori.Service

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.NotificationCompat
import com.example.proyecto.R
import app.introduce.memori.Recycler.DataWordsBase
import app.introduce.memori.Recycler.dataWordProvider
import app.introduce.memori.SettingActivity
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
    private lateinit var btnMax: ImageView
    private lateinit var btnClose: ImageView

    private lateinit var wordTrad: String

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    @SuppressLint("InflateParams", "ClickableViewAccessibility")
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
        btnMax.setImageResource(R.drawable.send)
        btnClose = floatView.findViewById(R.id.btnC)
        btnClose.setImageResource(R.drawable.closecardmem)
        edtDes = floatView.findViewById(R.id.edt_description)
        cardView = floatView.findViewById(R.id.cardContainer)
        edtDes2 = floatView.findViewById(R.id.edt_description2)


        LAYOUT_TYPE = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else WindowManager.LayoutParams.TYPE_TOAST

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

            @SuppressLint("ClickableViewAccessibility")
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

        edtDes.setOnTouchListener { _, _ ->
            edtDes.isCursorVisible = true
            val updatedFloatParamsFlag = floatWindowLayoutParams
            updatedFloatParamsFlag.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN

            windowManager.updateViewLayout(floatView, updatedFloatParamsFlag)
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mainHandler = Handler(Looper.getMainLooper())
        edtDes2.movementMethod = ScrollingMovementMethod()



        val numPickHour = intent?.getIntExtra("numberPickerHourW", 0)
        val numPicMinutes = intent?.getIntExtra("numberPickerMinutesW", 0)

        val fileInputStream = openFileInput("myfile.txt")
        val inputReader = InputStreamReader(fileInputStream)
        val output = inputReader.readText().trimEnd()
        val words = output.split("☼ ")


        //↓ForegroundService

        createNotification()
        val intentForeGroun = Intent(this, SettingActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intentForeGroun, PendingIntent.FLAG_MUTABLE)
        val notificacion: Notification = NotificationCompat.Builder(this, "channel1")
            .setContentText(getText(R.string.ejecutServ))
            .setContentTitle(getText(R.string.floatingWindow))
            .setSmallIcon(R.drawable.iconsvm)
            .setContentIntent(pendingIntent).build()

        //↑


        val mapWords: MutableMap<String, String> =mutableMapOf()
        var numword = 0
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
                    Toast.makeText(baseContext,R.string.toastServicefloat, Toast.LENGTH_LONG).show()
                }
                mainHandler.postDelayed(this,milisecundos.toLong())
                mainHandler.removeCallbacks(runn)
            }

        }

        btnMax.setOnClickListener {

            if(wordTrad.replace("☼", "") == edtDes.text.toString().trim()){
//                stopSelf()

                windowManager.removeView(floatView)

                //↓codigo para volver a llamar al postdelayed debido a que se lo cancela una vez aparece la ventana flotante 
                mainHandler.postDelayed(runn, milisecundos.toLong())

            }
            edtDes.setText("")


        }

        btnClose.setOnClickListener {

            windowManager.removeView(floatView)
            mainHandler.postDelayed(runn, milisecundos.toLong())

        }
        //↑


        val numHora = numPickHour?.times(60)

        val minutosTotal = numHora?.let { numPicMinutes?.plus(it) }
        milisecundos = minutosTotal!! * 60000

        if(milisecundos != 0){
            if(dataWordProvider.dataWords.size != 0){
                mainHandler.postDelayed(runn, milisecundos.toLong())
                startForeground(2, notificacion)
            }else{
                Toast.makeText(this, R.string.toastServicefloat, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, R.string.toastServicefloat2, Toast.LENGTH_LONG).show()
        }




        return START_STICKY
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