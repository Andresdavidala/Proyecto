package com.example.proyecto

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.FragmentEvaWordBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.io.InputStreamReader


class EvaWord : Fragment() {
    private var _binding: FragmentEvaWordBinding?=null
    private val binding get() = _binding!!
    private var miInterstitialAd: InterstitialAd? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentEvaWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sharedCountAds

        val sharedPreferences = activity?.getSharedPreferences("preferences_name", Context.MODE_PRIVATE)

// Guarda la variable en SharedPreferences
        MainActivity.nombreVariable = sharedPreferences!!.getInt("nombreVariable_key", 0)
        //↑


      binding.btnEHelp.setOnClickListener {
          val customDialogView: View = LayoutInflater.from(context).inflate(R.layout.dialog_information, null)
          val customDialog = AlertDialog.Builder(context)
          customDialog.setView(customDialogView)
          val messagefind = customDialogView.findViewById<TextView>(R.id.tvInformation)
          val message = messagefind.setText(R.string.helpEva)

          customDialog.setMessage(message.toString().replace("kotlin.Unit", ""))
          val cancelBtn = customDialogView.findViewById<ImageView>(R.id.btnClose)

          val dialog = customDialog.create()

          dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          dialog.show()

          cancelBtn.setOnClickListener {
              dialog.dismiss()
          }
      }

//se crea un List de todas las palabras
        try{
            binding.wordTrad.hint = "Ingresa tu respuesta"
            val openFile = activity?.openFileInput("myfile.txt")
            val inputReader = InputStreamReader(openFile)
            val data =inputReader.readText().trimEnd()
            val datatoList = data.split("☼○ ")

            var mapWords: MutableMap<String, String> = mutableMapOf()
            var numWord = 0
            for(i in datatoList.indices){
                try{
                    mapWords[datatoList[numWord]] = datatoList[numWord+1]
                    numWord += 2
                }catch (e: java.lang.IndexOutOfBoundsException){

                }

            }


            var wordTrad =mapWords[valorRam(dataWordProvider.dataWords, binding.evaWO.editText)]!!

            fun evaWord(){
                if(wordTrad.replace("☼○", "").equals(binding.evaWT.editText?.text.toString().trim(), true)){
                    wordTrad =mapWords[valorRam(dataWordProvider.dataWords, binding.evaWO.editText)]!!
                    binding.wordTrad.requestFocus()
                    MainActivity.nombreVariable+=1
                    if(MainActivity.nombreVariable == 4){
                        callAd()
                    }
                    sharedPreferences.edit().putInt("nombreVariable_key",
                        MainActivity.nombreVariable
                    ).apply()
                }else{
                    binding.evaWT.editText?.setText("")
                }
                binding.evaWT.editText?.setText("")
            }
            binding.btnEvaWord.setOnClickListener {
                evaWord()
            }


            binding.wordTrad.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_SEND) {
                    evaWord()

                }
                true
            })



        }catch (_: Exception){
            binding.wordTrad.isFocusable = false
            binding.wordTrad.hint = getString(R.string.textCampTradEva)
        }

    }

    private fun valorRam(valList:MutableList<DataWordsBase>, editEvaluar: EditText?): String {
        var list = valList.shuffled().take(1)[0]
        var wordReturn = list.wordOrg
        editEvaluar?.setText(wordReturn)
        return wordReturn
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

}