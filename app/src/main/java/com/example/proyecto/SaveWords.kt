package com.example.proyecto

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyecto.MainActivity.Companion.nombreVariable
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.Recycler.MemoriWords
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.FragmentSaveWordsBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class SaveWords : Fragment() {
    private var _binding: FragmentSaveWordsBinding? = null
    private val binding get() = _binding!!
    private var miInterstitialAd: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentSaveWordsBinding.inflate(inflater, container, false)


        return binding.root


    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sharedCountAds

        val sharedPreferences = activity?.getSharedPreferences("preferences_name", Context.MODE_PRIVATE)

// Guarda la variable en SharedPreferences
        MainActivity.nombreVariable = sharedPreferences!!.getInt("nombreVariable_key", 0)
        //↑

        //Loading data to memorias
        dataWordProvider.memorisWords.clear()
        Log.d("datosEmpty", "vacio")
        var txtFileMem = activity?.openFileOutput("memorias.txt", Context.MODE_APPEND) //important
//
        val openFileMem = activity?.openFileInput("memorias.txt")
        val inputReaderMem = InputStreamReader(openFileMem)
        val dataMem = inputReaderMem.readText().trimEnd()
        val datatoListMem = dataMem.split("☼○ ")
        var contWordMem = 0
        initAds()
        if (dataMem.isNotEmpty()) {
            for (i in datatoListMem.indices) {

                dataWordProvider.memorisWords.add(
                    MemoriWords(
                        datatoListMem[contWordMem]
                    )
                )

                contWordMem += 1

                Log.d("datos9", datatoListMem.toString())
            }
            Log.d("datos2", "no vacio")
        } else {
            Log.d("datos2", "vacio")
        }
        //↑

        dataWordProvider.dataWords.clear()
        Log.d("datosEmpty", "vacio")
        var txtFile = activity?.openFileOutput("myfile.txt", Context.MODE_APPEND) //important
//
        val openFile = activity?.openFileInput("myfile.txt")
        val inputReader = InputStreamReader(openFile)
        val data = inputReader.readText().trimEnd()
        val datatoList = data.split("☼○ ")

        var contWord = 0
        Log.d("datos8", data)
        Log.d("datos6", (datatoList.indices.toString()))
        Log.d("datos9", datatoList.toString())

        if (data.isNotEmpty()) {
            for (i in datatoList.indices step 2) {

                dataWordProvider.dataWords.add(
                    DataWordsBase(
                        datatoList[contWord],
                        datatoList[contWord + 1]
                    )
                )

                contWord += 2

                Log.d("datos9", datatoList.toString())
            }
            Log.d("datos2", "no vacio")
        } else {
            Log.d("datos2", "vacio")
        }


        //sharedPref para el customDialog
        val sharedPrefCustom = activity?.getSharedPreferences("my_prefCustom", MODE_PRIVATE)
        val dialogShown = sharedPrefCustom?.getBoolean("dialog_shown", false)






        if (!dialogShown!!) {
            //customDialog
            val customDialogView: View = LayoutInflater.from(context).inflate(R.layout.dialog_information, null)
            val customDialog = AlertDialog.Builder(context)
            customDialog.setView(customDialogView)
            val messagefind = customDialogView.findViewById<TextView>(R.id.tvInformation)
            val message = messagefind.setText("La sección preguntas te permite ingresar una pregunta y una respuesta hechas por ti, " +
                    "estas se mostraran en un tiempo determinado y configurado por ti mismo en la sección de configuraciones.")

            customDialog.setMessage(message.toString().replace("kotlin.Unit", ""))
            val cancelBtn = customDialogView.findViewById<ImageView>(R.id.btnClose)

            val dialog = customDialog.create()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
            sharedPrefCustom.edit().putBoolean("dialog_shown", true).apply()
        }

        //↑

        Log.d("datos2", dataWordProvider.dataWords.toString())
        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }
        fun saveWord(){
            val campoWordOrg = binding.wordOrg.text.toString().trim()
            val campoWordTrad = binding.wordTrad.text.toString().trim()
            try {

                if(binding.wordOrg.text?.isEmpty()==true || binding.wordTrad.text?.isEmpty()==true || TextUtils.isEmpty(campoWordOrg) || TextUtils.isEmpty(campoWordTrad)){
                    Toast.makeText(context, "Debe llenar los campos", Toast.LENGTH_SHORT).show()
                }else {
                    dataWordProvider.dataWords.add(DataWordsBase(campoWordOrg, campoWordTrad))
                    Toast.makeText(context, "Palabra guardada correctamente!", Toast.LENGTH_SHORT)
                        .show()
                    binding.wordOrg.setText("")
                    binding.wordTrad.setText("")

                    MainActivity.nombreVariable += 1

                    if(MainActivity.nombreVariable == 4){
                        Log.d("datosCoun+", "entra")
                        callAd()
                    }
                    sharedPreferences.edit().putInt("nombreVariable_key", nombreVariable).apply()
                    Log.d("datosCount", MainActivity.nombreVariable.toString())

                }
                //guardar en un textfile integrado dentro de la app↓

                txtFile = activity?.openFileOutput("myfile.txt", Context.MODE_PRIVATE)
                var outputWriter = OutputStreamWriter(txtFile)

                //escritura de datos ↓

                for (i in dataWordProvider.dataWords.indices) {
                    outputWriter.write("${dataWordProvider.dataWords[i].wordOrg.trim()}☼○ ")
                    outputWriter.write("${dataWordProvider.dataWords[i].wordTrad.trim()}☼○ ")


                }

                outputWriter.flush()
                outputWriter.close()

                hideKeyboard()
                binding.wordOrg.clearFocus()





            } catch (e: java.lang.Exception) {
                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show()

            }
        }
        binding.btnSaveWord.setOnClickListener {
            saveWord()

        }


        binding.wordTrad.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                saveWord()

            }
            false
        })


    }

    fun initAds(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {

                miInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {

                miInterstitialAd = interstitialAd
            }
        })
    }
    fun callAd(){
        showAds()
        MainActivity.nombreVariable = 0
        initAds()
    }

    fun showAds(){
        activity?.let { miInterstitialAd?.show(it) }
    }


    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        binding.wordOrg.requestFocus()
    }

}