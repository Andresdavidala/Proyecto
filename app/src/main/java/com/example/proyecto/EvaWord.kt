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
import java.io.FileInputStream
import java.io.InputStreamReader


class EvaWord : Fragment() {
    private var _binding: FragmentEvaWordBinding?=null
    private val binding get() = _binding!!
    private lateinit var openFile: FileInputStream
    private lateinit var inputReader:  InputStreamReader
    //interstitial
    private var interstitial: InterstitialAd? = null
    private var count = 0
    //↑

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentEvaWordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sharedCountAds

        val sharedPreferences = activity?.getSharedPreferences("preferences_name", Context.MODE_PRIVATE)

        //sharedP count Ads
        val countShared = activity?.getSharedPreferences("sharedCountEva", Context.MODE_PRIVATE)
        count = countShared!!.getInt("valueCountEva", count)

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
            openFile = activity?.openFileInput("myfile.txt")!!
            inputReader = InputStreamReader(openFile)
            val data =inputReader.readText().trimEnd()
            val datatoList = data.split("☼ ")

            val mapWords: MutableMap<String, String> = mutableMapOf()
            var numWord = 0
            for(i in datatoList.indices){
                try{
                    mapWords[datatoList[numWord]] = datatoList[numWord+1]
                    numWord += 2
                }catch (_: java.lang.IndexOutOfBoundsException){

                }

            }


            var wordTrad =mapWords[valorRam(dataWordProvider.dataWords, binding.evaWO.editText)]!!

            fun evaWord(){
                if(wordTrad.replace("☼", "").equals(binding.evaWT.editText?.text.toString().trim(), true)){
                    wordTrad =mapWords[valorRam(dataWordProvider.dataWords, binding.evaWO.editText)]!!
                    binding.wordTrad.requestFocus()


                    //Interstitial
                    count += 1
                    val editorCount = countShared.edit()
                    editorCount.putInt("valueCountEva", count).apply()
                    initInterstitial()
                    if(count == 5){
                        checkCount()
                        count = 0
                        editorCount.putInt("valueCountEva", count).apply()

                    }
                    Log.d("datos", count.toString())
                }else{
                    binding.evaWT.editText?.setText("")
                }
                binding.evaWT.editText?.setText("")
            }
            binding.btnEvaWord.setOnClickListener {
                evaWord()
            }


            binding.wordTrad.setOnEditorActionListener { v, actionId, event ->
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_SEND) {
                    evaWord()

                }
                true
            }


        }catch (_: Exception){
            binding.wordTrad.isFocusable = false
            binding.wordTrad.hint = getString(R.string.textCampTradEva)
        }finally {
            openFile.close()
            inputReader.close()
        }

    }

    private fun valorRam(valList:MutableList<DataWordsBase>, editEvaluar: EditText?): String {
        val list = valList.shuffled().take(1)[0]
        val wordReturn = list.wordOrg
        editEvaluar?.setText(wordReturn)
        return wordReturn
    }

    //interstitial function
    private fun initInterstitial(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireActivity(), "ca-app-pub-3940256099942544/1033173712", adRequest, object: InterstitialAdLoadCallback(){
            override fun onAdLoaded(interst: InterstitialAd) {
                interstitial = interst
            }

            override fun onAdFailedToLoad(intert: LoadAdError) {
                interstitial = null
            }

        })
    }

    private fun showAds(){
        interstitial?.show(requireActivity())
    }

    private fun checkCount(){
        showAds()
        initInterstitial()
    }

}