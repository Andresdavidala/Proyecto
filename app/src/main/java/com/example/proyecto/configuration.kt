package com.example.proyecto

import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.proyecto.databinding.FragmentConfigurationBinding
import java.io.InputStreamReader
import kotlin.properties.Delegates
import kotlin.random.Random


class configuration : Fragment() {
    private lateinit var dialog: AlertDialog
    private var _binding: FragmentConfigurationBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        return binding.root

    }
    @SuppressLint("UseSwitchCompatOrMaterialCode", "CommitPrefEdits")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clTitle1 = binding.clTitle1
        val clHS = binding.clShow

        val clTitle2 = binding.clTitle2
        val clHS2 = binding.clShow2


        //sharedP
        val sharedPreferencesToast = activity?.getSharedPreferences("openCardToast", MODE_PRIVATE)
        clHS.isVisible =sharedPreferencesToast!!.getBoolean("visCardToast", false)

        val sharedPreferencesWindow = activity?.getSharedPreferences("openCardWindows", MODE_PRIVATE)
        clHS2.isVisible = sharedPreferencesWindow!!.getBoolean("visCardWindows", false)
        //↑SP



        //para la lectura del .txt
        val fileInputStream = activity?.openFileInput("words.txt")
        val inputReader = InputStreamReader(fileInputStream)
        val output = inputReader.readText()
        var words = output.split(",")

        //↑


        //cambiar icono al presionar


        //time configuration

        var milisecundos by Delegates.notNull<Int>()
        var milisegundosDoa by Delegates.notNull<Int>()

            //handler y runnable for Toast
            var mapWords: MutableMap<String, String> =mutableMapOf()
            var numword = 0;
            for(i in words.indices){
                try {
                    mapWords[words[numword]] = words[numword + 1]
                    numword += 2
                }catch (e:IndexOutOfBoundsException){

                }
            }
            val mainHandler = Handler(Looper.getMainLooper())
            var runn = object : Runnable{
                override fun run() {
                    switchChecked(mapWords)
                    mainHandler.postDelayed(this, milisecundos.toLong())
                    Log.d("Tagmili", milisecundos.toString())
                }
            }

            //↑





        val numberPickerminutes = binding.numberPicker
        val numberPickerHour = binding.numberPicker2
        val numberPickerHourW = binding.numberPicker3
        val numberPickerMinutesW = binding.numberPicker4


        numberPickerHour.minValue = 0
        numberPickerHour.maxValue = 24
        numberPickerminutes.minValue = 0
        numberPickerminutes.maxValue = 60


        numberPickerMinutesW.minValue = 0
        numberPickerMinutesW.maxValue = 60
        numberPickerHourW.maxValue = 24
        numberPickerHourW.minValue = 0


        //SharedPNumberPicker--
        //numberPickerHoras toast ↓
        val numPickerShared = activity?.getSharedPreferences("numPickerToastHour", MODE_PRIVATE)
        numberPickerHour.value = numPickerShared!!.getInt("SPPickerValue", 0)

        //numberpickerminutos toast↓
        val nPSPMin= activity?.getSharedPreferences("numPickerToastMin", MODE_PRIVATE)
        numberPickerminutes.value = nPSPMin!!.getInt("SPPTMin", 0)


        //numberPickerHorasWindowDOA toast ↓
        val npSPWinHour = activity?.getSharedPreferences("numPickerWinHour", MODE_PRIVATE)
        numberPickerHourW.value = npSPWinHour!!.getInt("SPNPWTHour", 0)

        //numberPickerMinWindowDOA toast ↓
        val npSPWinMin = activity?.getSharedPreferences("numPickerWinMin", MODE_PRIVATE)
        numberPickerMinutesW.value = npSPWinMin!!.getInt("SPNPWTMin", 0)
        //↑SPNP


        //SharedPBtnEstablecer--

        //EstBtnToast
        val buttonEstablecerToast = activity?.getSharedPreferences("buttonEstToast", MODE_PRIVATE)
        binding.btnEstablecer.isSelected = buttonEstablecerToast!!.getBoolean("btnEToastSP", binding.btnEstablecer.isSelected)

        //EstBtnWin
        val buttonEstablecerWin = activity?.getSharedPreferences("buttonEstWin", MODE_PRIVATE)
        binding.btnEstablecerW.isSelected = buttonEstablecerWin!!.getBoolean("btnEWinSP", binding.btnEstablecer.isSelected)


        //SharedPBtnEstablecerText--

        //ToastBtnText
        val buttonTextToast = activity?.getSharedPreferences("buttonEstTextToast", MODE_PRIVATE)
        binding.btnEstablecer.text = buttonTextToast!!.getString("btnETextToastSP", "Establecer")


        //WinBtnText

        val buttonTextWin = activity?.getSharedPreferences("buttonEstTextWin", MODE_PRIVATE)
        binding.btnEstablecerW.text = buttonTextWin!!.getString("btnETextWinSP", "Establecer")

        //SPBE


        binding.btnEstablecer.setOnClickListener {
            binding.btnEstablecer.isSelected = !binding.btnEstablecer.isSelected
            //sharedPToast


            //numberPickerHoras toast ↓
            val nPTSharedHour = activity?.getSharedPreferences("numPickerToastHour", MODE_PRIVATE)!!.edit()
            nPTSharedHour.putInt("SPPickerValue", numberPickerHour.value).apply()

            //numberpickerminutos toast↓
            val nPTMin = activity?.getSharedPreferences("numPickerToastMin", MODE_PRIVATE)!!.edit()
            nPTMin.putInt("SPPTMin", numberPickerminutes.value).apply()

            //SharedPBtnEstablecer
            val btnEstablecerToast = activity?.getSharedPreferences("buttonEstToast", MODE_PRIVATE)!!.edit()
            btnEstablecerToast.putBoolean("btnEToastSP", binding.btnEstablecer.isSelected).apply()

            //SharedPBtnEstablecerText
            val btnTextToast = activity?.getSharedPreferences("buttonEstTextToast", MODE_PRIVATE)!!.edit()


            //↑SPT


            if(binding.btnEstablecer.isSelected == true){
                binding.btnEstablecer.text = "Cancelar"
                btnTextToast.putString("btnETextToastSP", "Cancelar").apply()
                val numHora = numberPickerHour.value * 60

                val numMinute = numberPickerminutes.value

                val minutosTotal =  numMinute + numHora
                milisecundos = minutosTotal * 1000

                mainHandler.removeCallbacks(runn)
                mainHandler.postDelayed(runn, milisecundos.toLong())

            }else{
                binding.btnEstablecer.text = "Establecer"
                btnTextToast.putString("btnETextToastSP", "Establecer").apply()
                mainHandler.removeCallbacks(runn)
            }

        }


        //For windowsFloating

        val mainHandlerWindowsDOA = Handler(Looper.getMainLooper())
        var intent = Intent(context, FloatingWindow :: class.java)
        var runnWindowDOA = object : Runnable{
            override fun run() {

                activity?.startService(intent)


                mainHandlerWindowsDOA.postDelayed(this,milisegundosDoa.toLong())
                Log.d("Tagmili", milisegundosDoa.toString())
            }
        }

        //↑

        binding.btnEstablecerW.setOnClickListener {
            binding.btnEstablecerW.isSelected = !binding.btnEstablecerW.isSelected
            //sharedPToast

            //numberPickerHorasWindowDOA  ↓
            val nPWHour = activity?.getSharedPreferences("numPickerWinHour", MODE_PRIVATE)!!.edit()
            nPWHour.putInt("SPNPWTHour", numberPickerHourW.value).apply()

            //numberPickerMinWindowDOA  ↓
            val nPWMin = activity?.getSharedPreferences("numPickerWinMin", MODE_PRIVATE)!!.edit()
            nPWMin.putInt("SPNPWTMin", numberPickerMinutesW.value).apply()

            //SharedPBtnEstablecerWin
            val btnEstablecerWin = activity?.getSharedPreferences("buttonEstWin", MODE_PRIVATE)!!.edit()
            btnEstablecerWin.putBoolean("btnEWinSP", binding.btnEstablecerW.isSelected).apply()

            //SharedPBtnEstablecerWinText
            val btnTextWin = activity?.getSharedPreferences("buttonEstTextWin", MODE_PRIVATE)!!.edit()
            //↑SPT

            if(binding.btnEstablecerW.isSelected == true){
                binding.btnEstablecerW.text = "Cancelar"
                btnTextWin.putString("btnETextWinSP", "Cancelar").apply()
                val numHora = numberPickerHourW.value * 60

                val numMinute = numberPickerMinutesW.value

                var minutosTotal =  numMinute + numHora
                milisegundosDoa = minutosTotal * 1000


                mainHandlerWindowsDOA.removeCallbacks(runnWindowDOA)


                mainHandlerWindowsDOA.postDelayed(runnWindowDOA, milisegundosDoa.toLong())
                Log.d("TAGERROR", "Imprimiento")

            }else{
                binding.btnEstablecerW.text = "Establecer"
                btnTextWin.putString("btnETextWinSP", "Establecer").apply()
                mainHandlerWindowsDOA.removeCallbacks(runnWindowDOA)

            }



            if(checkOverlayPermission()){



            }else{
                requestFlowatingPermission()

            }


        }


        //wfloating↑
        //timeconfiguration↑

        clTitle1.setOnClickListener {
            val visibility = clHS.visibility
            val sharedPToast = activity?.getSharedPreferences("openCardToast", Context.MODE_PRIVATE)
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

            val sharedPWindows = activity?.getSharedPreferences("openCardWindows", MODE_PRIVATE)
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
    }


    //metodo para obtener clave - valor e imprimirlo por un Toast
    private fun switchChecked(mapWords: Map<String,String>){
        var randoMap = mapWords.entries.elementAt(Random.nextInt(mapWords.size))
        Log.d("TAGSw", mapWords.toString())
        Toast.makeText(context, "${randoMap.key.uppercase()} → ${randoMap.value.uppercase()}", Toast.LENGTH_LONG).show()
    }


    //permisos drawoverApp ↓

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestFlowatingPermission() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setTitle("Se necesita permiso de superposición de pantalla")
        builder.setMessage("Habilite 'Permitir mostrar sobre otras aplicaciones' desde la configuración")
        builder.setPositiveButton("Abrir configuraciones", DialogInterface.OnClickListener{ dialog, which ->

            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${activity?.packageName}")
            )
            startActivity(intent)
        })
        dialog = builder.create()
        dialog.show()
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkOverlayPermission():Boolean{
        return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            Settings.canDrawOverlays(context)
        }
        else return true
    }

    fun sharedPreferences(){

    }
}