package com.example.proyecto

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.proyecto.Service.BubbleService
import com.example.proyecto.Service.FloatingWindow
import com.example.proyecto.Service.ToastService
import com.example.proyecto.Service.memoriasService
import com.example.proyecto.databinding.ActivitySettingBinding
import kotlin.properties.Delegates

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var dialog: AlertDialog

    private lateinit var mainHandler: Handler
    private lateinit var runn: Runnable
//    private lateinit var br: BroadcastReceiver

    private var miliDate by Delegates.notNull<Int>()

    //
//    override fun onDestroy() {
//        super.onDestroy()
//        mainHandler.removeCallbacksAndMessages(null)
//
//    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val clTitle1 = binding.clTitle1
        val clHS = binding.clShow

        val clTitle2 = binding.clTitle2
        val clHS2 = binding.clShow2

        val clTitle3 = binding.clTitleCard
        val clHS3 = binding.clShow2Card

        //DECLARE SWITCH
        val switchBlb = binding.switchBurbj


        //backbtn
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


        //↑


        //sharedP
        val sharedPreferencesToast = getSharedPreferences("openCardToast", MODE_PRIVATE)
        clHS.isVisible = sharedPreferencesToast!!.getBoolean("visCardToast", false)

        val sharedPreferencesWindow = getSharedPreferences("openCardWindows", MODE_PRIVATE)
        clHS2.isVisible = sharedPreferencesWindow!!.getBoolean("visCardWindows", false)

        val sharedPreferencesMem = getSharedPreferences("openCardMem", MODE_PRIVATE)
        clHS3.isVisible = sharedPreferencesMem.getBoolean("visCardMem", false)



    //SharedPBtnEstablecer--

    //EstBtnToast
    val buttonEstablecerToast = getSharedPreferences("buttonEstToast", MODE_PRIVATE)
    binding.btnEstablecer.isSelected =
        buttonEstablecerToast!!.getBoolean("btnEToastSP", binding.btnEstablecer.isSelected)

    //EstBtnWin
    val buttonEstablecerWin = getSharedPreferences("buttonEstWin", MODE_PRIVATE)
    binding.btnEstablecerW.isSelected =
        buttonEstablecerWin!!.getBoolean("btnEWinSP", binding.btnEstablecer.isSelected)


    //EstBtnWin
        val buttonEstablecerMem = getSharedPreferences("buttonEstMem", MODE_PRIVATE)
        binding.btnEstablecerWCard.isSelected =
            buttonEstablecerMem.getBoolean("btnEMem", binding.btnEstablecerWCard.isSelected)



    //SharedPBtnEstablecerText--

    //ToastBtnText
    val buttonTextToast = getSharedPreferences("buttonEstTextToast", MODE_PRIVATE)
    binding.btnEstablecer.text = buttonTextToast!!.getString("btnETextToastSP", "Establecer")


    //WinBtnText

    val buttonTextWin = getSharedPreferences("buttonEstTextWin", MODE_PRIVATE)
    binding.btnEstablecerW.text = buttonTextWin!!.getString("btnETextWinSP", "Establecer")



    //btnMemoria

    val buttonTextMemo = getSharedPreferences("buttonEstMemoria", MODE_PRIVATE)
    binding.btnEstablecerWCard.text = buttonTextMemo.getString("btnETextMemo","Establecer")


    //SPBE

        //↑SP


        val numberPickerminutes = binding.numberPicker
        val numberPickerHour = binding.numberPicker2
        val numberPickerHourW = binding.numberPicker3
        val numberPickerMinutesW = binding.numberPicker4
        val numberPickerHourMem = binding.numberPicker3Card
        val numberPickerMinutesMem = binding.numberPicker4Card


        numberPickerHour.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPickerminutes.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPickerHourW.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPickerMinutesW.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPickerHourMem.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        numberPickerMinutesMem.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS


        numberPickerHour.minValue = 0
        numberPickerHour.maxValue = 24
        numberPickerminutes.minValue = 0
        numberPickerminutes.maxValue = 59


        numberPickerMinutesW.minValue = 0
        numberPickerMinutesW.maxValue = 59
        numberPickerHourW.maxValue = 24
        numberPickerHourW.minValue = 0

        numberPickerHourMem.minValue = 0
        numberPickerHourMem.maxValue = 24
        numberPickerMinutesMem.minValue = 0
        numberPickerMinutesMem.maxValue = 59

        //SharedPNumberPicker--
        //numberPickerHoras toast ↓
        val numPickerShared = getSharedPreferences("numPickerToastHour", MODE_PRIVATE)
        numberPickerHour.value = numPickerShared.getInt("SPPickerValue", 0)

        //numberpickerminutos toast↓
        val nPSPMin = getSharedPreferences("numPickerToastMin", MODE_PRIVATE)
        numberPickerminutes.value = nPSPMin.getInt("SPPTMin", 0)


        //numberPickerHorasWindowDOA toast ↓
        val npSPWinHour = getSharedPreferences("numPickerWinHour", MODE_PRIVATE)
        numberPickerHourW.value = npSPWinHour.getInt("SPNPWTHour", 0)

        //numberPickerMinWindowDOA toast ↓
        val npSPWinMin = getSharedPreferences("numPickerWinMin", MODE_PRIVATE)
        numberPickerMinutesW.value = npSPWinMin.getInt("SPNPWTMin", 0)

        //numberpicker Memorias
        val numberPickerSharedMem = getSharedPreferences("numPickerToastHourMem", MODE_PRIVATE)
        numberPickerHourMem.value = numberPickerSharedMem.getInt("SPPickerValueMem", 0)

        val npSPMinMem = getSharedPreferences("numPickerToastMinMem", MODE_PRIVATE)
        numberPickerMinutesMem.value = npSPMinMem.getInt("SPNPMinMem", 0)

        //

        //numberPickerisEnabledwhenpressbtnEstablecer
        val numPickIsEnable = getSharedPreferences("numPickEnabled", MODE_PRIVATE)
        numberPickerHour.isEnabled = numPickIsEnable.getBoolean("nPEnabled", numberPickerHour.isEnabled)
        numberPickerminutes.isEnabled = numPickIsEnable.getBoolean("nPEnabled", numberPickerHour.isEnabled)

        val numPickIsEnableW = getSharedPreferences("numPickEnabledW", MODE_PRIVATE)
        numberPickerHourW.isEnabled = numPickIsEnableW.getBoolean("nPEnabledW", numberPickerHour.isEnabled)
        numberPickerMinutesW.isEnabled = numPickIsEnableW.getBoolean("nPEnabledW", numberPickerHour.isEnabled)

        val numPickIsEnableMem = getSharedPreferences("numPickEnabledMem", MODE_PRIVATE)
        numberPickerHourMem.isEnabled = numPickIsEnableMem.getBoolean("nPEnabledMem", numberPickerHourMem.isEnabled)
        numberPickerMinutesMem.isEnabled = numPickIsEnableMem.getBoolean("nPEnabledMem", numberPickerMinutesMem.isEnabled)
        //↑SPNP


        //SharedPSwitch
        val switchBubble = getSharedPreferences("switchBuble", MODE_PRIVATE)
        switchBlb.isChecked = switchBubble.getBoolean("valSwitch", false)


        //↑






        //para toast

        binding.btnEstablecer.setOnClickListener {



            binding.btnEstablecer.isSelected = !binding.btnEstablecer.isSelected
            binding.numberPicker.isEnabled = !binding.numberPicker.isEnabled
            binding.numberPicker2.isEnabled = !binding.numberPicker2.isEnabled


            //foreground
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                startForegroundService(intent)
            }else{
                stopService(intent)
            }

            //↑
            //sharedPToast

            //numberPickerHoras toast ↓
            val nPTSharedHour = getSharedPreferences("numPickerToastHour", MODE_PRIVATE)!!.edit()
            nPTSharedHour.putInt("SPPickerValue", numberPickerHour.value).apply()

            //numberpickerminutos toast↓
            val nPTMin = getSharedPreferences("numPickerToastMin", MODE_PRIVATE)!!.edit()
            nPTMin.putInt("SPPTMin", numberPickerminutes.value).apply()

            //SharedPBtnEstablecer
            val btnEstablecerToast = getSharedPreferences("buttonEstToast", MODE_PRIVATE)!!.edit()
            btnEstablecerToast.putBoolean("btnEToastSP", binding.btnEstablecer.isSelected).apply()

            //SharedPBtnEstablecerText
            val btnTextToast = getSharedPreferences("buttonEstTextToast", MODE_PRIVATE)!!.edit()

            val numPEnabled = getSharedPreferences("numPickEnabled", MODE_PRIVATE)!!.edit()
            numPEnabled.putBoolean("nPEnabled",numberPickerHour.isEnabled).apply()
            numPEnabled.putBoolean("nPEnabled", numberPickerminutes.isEnabled).apply()


            //↑SPT



            if(binding.btnEstablecer.isSelected){


                binding.btnEstablecer.text = "Cancelar"
                btnTextToast.putString("btnETextToastSP", "Cancelar").apply()
//                val numHora = numberPickerHour.value * 60
//
//                val numMinute = numberPickerminutes.value
//
//                val minutosTotal =  numMinute + numHora
//                milisecundos = minutosTotal * 1000
//
//                mainHandler.removeCallbacks(runn)
//                mainHandler.postDelayed(runn, milisecundos.toLong())

//                Intent(this@SettingActivity, ToastService()::class.java).also {
//                    startService(it)
//                }

                Intent(this@SettingActivity, ToastService::class.java ).also {
                    it.putExtra("numberPickerHour", numberPickerHour.value)
                    it.putExtra("numberPickerMinutes", numberPickerminutes.value)


                    startService(it)
                }
                Log.d("datos", "funcionando")


            }else{

                binding.btnEstablecer.text = "Establecer"
                btnTextToast.putString("btnETextToastSP", "Establecer").apply()
                Intent(this@SettingActivity, ToastService::class.java).also {
                    stopService(it)
                }
//                mainHandler.removeCallbacks(runn)
                Log.d("datos", "dejo de funcionar")
            }


        }
//        //↑




        binding.btnEstablecerW.setOnClickListener {




            if(checkOverlayPermission()){

                binding.btnEstablecerW.isSelected = !binding.btnEstablecerW.isSelected
                binding.numberPicker3.isEnabled = !binding.numberPicker3.isEnabled
                binding.numberPicker4.isEnabled = !binding.numberPicker4.isEnabled
                //sharedPToast


                //foreground
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    startForegroundService(intent)
                }else{
                    stopService(intent)
                }

                //↑

                //numberPickerHorasWindowDOA  ↓
                val nPWHour = getSharedPreferences("numPickerWinHour", MODE_PRIVATE)!!.edit()
                nPWHour.putInt("SPNPWTHour", numberPickerHourW.value).apply()

                //numberPickerMinWindowDOA  ↓
                val nPWMin = getSharedPreferences("numPickerWinMin", MODE_PRIVATE)!!.edit()
                nPWMin.putInt("SPNPWTMin", numberPickerMinutesW.value).apply()

                //SharedPBtnEstablecerWin
                val btnEstablecerWin = getSharedPreferences("buttonEstWin", MODE_PRIVATE)!!.edit()
                btnEstablecerWin.putBoolean("btnEWinSP", binding.btnEstablecerW.isSelected).apply()

                //SharedPBtnEstablecerWinText
                val btnTextWin = getSharedPreferences("buttonEstTextWin", MODE_PRIVATE)!!.edit()

                //para SP NumberPicker issEnabled
                val numPEnabledW = getSharedPreferences("numPickEnabledW", MODE_PRIVATE)!!.edit()
                numPEnabledW.putBoolean("nPEnabledW",numberPickerHourW.isEnabled).apply()
                numPEnabledW.putBoolean("nPEnabledW", numberPickerMinutesW.isEnabled).apply()
                //↑SPT

                if(binding.btnEstablecerW.isSelected){
                    binding.btnEstablecerW.text = "Cancelar"
                    btnTextWin.putString("btnETextWinSP", "Cancelar").apply()

                    Intent(this@SettingActivity, FloatingWindow::class.java ).also {
                        it.putExtra("numberPickerHourW", numberPickerHourW.value)
                        it.putExtra("numberPickerMinutesW", numberPickerMinutesW.value)
                        startService(it)
                    }
                }else{
                    binding.btnEstablecerW.text = "Establecer"
                    btnTextWin.putString("btnETextWinSP", "Establecer").apply()

                    Intent(this@SettingActivity, FloatingWindow::class.java).also {
                        stopService(it)
                    }
                }

            }else{
                requestFlowatingPermission()

            }

        }
        binding.btnEstablecerWCard.setOnClickListener {




            if(checkOverlayPermission()){

                binding.btnEstablecerWCard.isSelected = !binding.btnEstablecerWCard.isSelected
                binding.numberPicker3Card.isEnabled = !binding.numberPicker3Card.isEnabled
                binding.numberPicker4Card.isEnabled = !binding.numberPicker4Card.isEnabled
                //sharedPToast


                //foreground
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                    startForegroundService(intent)
                }else{
//                    stopService(intent)
                }

                //↑

                //numberPickerHorasWindowDOA  ↓
                val nPWHourMem = getSharedPreferences("numPickerToastHourMem", MODE_PRIVATE)!!.edit()
                nPWHourMem.putInt("SPPickerValueMem", numberPickerHourMem.value).apply()

                //numberPickerMinWindowDOA  ↓
                val nPWMinMem = getSharedPreferences("numPickerToastMinMem", MODE_PRIVATE)!!.edit()
                nPWMinMem.putInt("SPNPMinMem", numberPickerMinutesMem.value).apply()

                //SharedPBtnEstablecerWin
                val btnEstablecerWinMem = getSharedPreferences("buttonEstMem", MODE_PRIVATE)!!.edit()
                btnEstablecerWinMem.putBoolean("btnEMem", binding.btnEstablecerWCard.isSelected).apply()

                //SharedPBtnEstablecerWinText
                val btnTextWinMem = getSharedPreferences("buttonEstMemoria", MODE_PRIVATE)!!.edit()

                //para SP NumberPicker issEnabled
                val numPEnabledW = getSharedPreferences("numPickEnabledMem", MODE_PRIVATE)!!.edit()
                numPEnabledW.putBoolean("nPEnabledMem",numberPickerHourMem.isEnabled).apply()
                numPEnabledW.putBoolean("nPEnabledMem", numberPickerMinutesMem.isEnabled).apply()
                //↑SPT

                if(binding.btnEstablecerWCard.isSelected){
                    binding.btnEstablecerWCard.text = "Cancelar"
                    btnTextWinMem.putString("btnETextMemo", "Cancelar").apply()

                    Intent(this@SettingActivity, memoriasService::class.java ).also {
                        it.putExtra("numberPickerHourWMem", numberPickerHourMem.value)
                        it.putExtra("numberPickerMinutesWMem", numberPickerMinutesMem.value)
                        startService(it)
                    }
                }else{
                    binding.btnEstablecerWCard.text = "Establecer"
                    btnTextWinMem.putString("btnETextMemo", "Establecer").apply()

                    Intent(this@SettingActivity, memoriasService::class.java).also {
                        stopService(it)
                    }
                }

            }else{
                requestFlowatingPermission()

            }

        }

        //wfloating↑
        //timeconfiguration↑

        clTitle1.setOnClickListener {
            val visibility = clHS.visibility
            val sharedPToast = getSharedPreferences("openCardToast", Context.MODE_PRIVATE)
            val editor = sharedPToast?.edit()

            if(visibility == View.VISIBLE ){

                //sharedP
                editor?.apply {
                    putBoolean("visCardToast", false)
                }?.apply()

                //↑SP
                clHS.visibility = View.GONE
                binding.down1.visibility = View.VISIBLE
                binding.up1.visibility = View.INVISIBLE



            }else{
                //sharedP
                editor?.apply {
                    putBoolean("visCardToast", true)
                }?.apply()
                //↑SP
                clHS.visibility = View.VISIBLE
                binding.down1.visibility = View.INVISIBLE
                binding.up1.visibility = View.VISIBLE
            }
        }

        clTitle2.setOnClickListener {
            var visibility2 = clHS2.visibility

            val sharedPWindows = getSharedPreferences("openCardWindows", MODE_PRIVATE)
            val editor = sharedPWindows?.edit()

            if(visibility2 == View.VISIBLE){
                //SharedP
                editor?.apply{
                    putBoolean("visCardWindows", false)
                }?.apply()
                //↑SP
                clHS2.visibility = View.GONE
                binding.down2.visibility = View.VISIBLE
                binding.up2.visibility = View.INVISIBLE
            }else{
                //SharedP
                editor?.apply{
                    putBoolean("visCardWindows", true)
                }?.apply()
                //↑SP
                clHS2.visibility = View.VISIBLE
                binding.down2.visibility = View.INVISIBLE
                binding.up2.visibility = View.VISIBLE
            }
        }
        clTitle3.setOnClickListener {
            var visibility2 = clHS3.visibility

            val sharedPWindows = getSharedPreferences("openCardMem", MODE_PRIVATE)
            val editor = sharedPWindows?.edit()

            if(visibility2 == View.VISIBLE){
                //SharedP
                editor?.apply{
                    putBoolean("visCardMem", false)
                }?.apply()
                //↑SP
                clHS3.visibility = View.GONE
                binding.down2Card.visibility = View.VISIBLE
                binding.up2Card.visibility = View.INVISIBLE
            }else{
                //SharedP
                editor?.apply{
                    putBoolean("visCardMem", true)
                }?.apply()
                //↑SP
                clHS3.visibility = View.VISIBLE
                binding.down2Card.visibility = View.INVISIBLE
                binding.up2Card.visibility = View.VISIBLE
            }
        }
        //section bubble
        binding.switchBurbj.setOnCheckedChangeListener { compoundButton, isChecked ->

            if(isChecked){

                if(checkOverlayPermission()){
                    val switchValBbl  = getSharedPreferences("switchBuble", MODE_PRIVATE)!!.edit()
                    switchValBbl.putBoolean("valSwitch", true).apply()
                    //foreground
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        try{
                            startForegroundService(intent)
                        }catch (_: Exception){

                        }

                    }else{
                        try{
                            stopService(intent)
                        }catch (_: Exception){

                        }

                    }

                    //↑
                    Intent(this@SettingActivity, BubbleService::class.java).also {
                            startService(it)
                    }

                }else{
                    switchBlb.isChecked = false
                    requestFlowatingPermission()
                }
            }else{
                val switchValBbl  = getSharedPreferences("switchBuble", MODE_PRIVATE)!!.edit()
                switchValBbl.putBoolean("valSwitch", false).apply()
               try {
                   Intent(this@SettingActivity, BubbleService::class.java).also {
                       stopService(it)
                   }
               }catch (_: Exception){

               }

            }
        }




        //↑
    }




    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestFlowatingPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Se necesita permiso de superposición de pantalla")
        builder.setMessage("Habilite 'Permitir mostrar sobre otras aplicaciones' desde la configuración")
        builder.setPositiveButton("Abrir configuraciones", DialogInterface.OnClickListener{ dialog, which ->

            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${packageName}")
            )
            startActivity(intent)
        })
        dialog = builder.create()
        dialog.show()
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkOverlayPermission():Boolean{
        return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            Settings.canDrawOverlays(this)
        }
        else return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }

//    override fun onPause() {
//        super.onPause()
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(br)
//    }




//    override fun onPause() {
//        super.onPause()
//        mainHandler.removeCallbacksAndMessages(null)
//    }


}