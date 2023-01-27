package com.example.proyecto

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proyecto.Recycler.MemoriWords
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.FragmentCardMemorBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class cardMemor : Fragment() {
    private var _binding: FragmentCardMemorBinding?=null
    private val binding get() = _binding!!
    private var miInterstitialAd: InterstitialAd? = null

    //interstitial
    private var interstitial: InterstitialAd? = null
    private var count = 0
    //↑

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCardMemorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sharedCountAds

        val sharedPreferences = activity?.getSharedPreferences("preferences_name", Context.MODE_PRIVATE)

        //sharedP count Ads
        val countShared = activity?.getSharedPreferences("sharedCountMemo", Context.MODE_PRIVATE)
        count = countShared!!.getInt("valueCountMemo", count)

        dataWordProvider.memorisWords.clear()
        var txtFile = activity?.openFileOutput("memorias.txt", Context.MODE_APPEND) //important
//
        val openFile = activity?.openFileInput("memorias.txt")
        val inputReader = InputStreamReader(openFile)
        val data = inputReader.readText().trimEnd()
        val datatoList = data.split("☼ ")

        var contWord = 0


//
//        initAds()


        //↑

        binding.btnMHelp.setOnClickListener {
            //customDialog
            val customDialogView: View = LayoutInflater.from(context).inflate(R.layout.dialog_information, null)
            val customDialog = AlertDialog.Builder(context)
            customDialog.setView(customDialogView)
            val messagefind = customDialogView.findViewById<TextView>(R.id.tvInformation)
            val message = messagefind.setText(R.string.helpCard)

            customDialog.setMessage(message.toString().replace("kotlin.Unit", ""))
            val cancelBtn = customDialogView.findViewById<ImageView>(R.id.btnClose)

            val dialog = customDialog.create()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
        }

        if (data.isNotEmpty()) {
            for (i in datatoList.indices) {

                dataWordProvider.memorisWords.add(
                    MemoriWords(
                        datatoList[contWord]
                    )
                )

                contWord += 1

            }
        }

        fun Fragment.hideKeyboard() {
            view.let { activity?.hideKeyboard(it) }
        }

        fun saveWord(){
            val campoMem = binding.etmemoris.text.toString().trim()

            try {

                if(binding.etmemoris.text?.isEmpty()==true || TextUtils.isEmpty(campoMem) ){
                    Toast.makeText(context, R.string.toastCard, Toast.LENGTH_SHORT).show()
                }else {
                    dataWordProvider.memorisWords.add(MemoriWords(campoMem))
                    Toast.makeText(context, R.string.toastcard2, Toast.LENGTH_SHORT)
                        .show()
                    binding.etmemoris.setText("")

                    count += 1
                    val editorCount = countShared.edit()
                    editorCount.putInt("valueCountMemo", count).apply()
                    //Interstitial
                    initInterstitial()
                    if(count == 5){
                        checkCount()
                        count = 0
                        editorCount.putInt("valueCountMemo", count).apply()
                    }
                    Log.d("datos", count.toString())
                }
                //guardar en un textfile integrado dentro de la app↓

                txtFile = activity?.openFileOutput("memorias.txt", Context.MODE_PRIVATE)
                val outputWriter = OutputStreamWriter(txtFile)

                //escritura de datos ↓

                for (i in dataWordProvider.memorisWords.indices) {
                    outputWriter.write("${dataWordProvider.memorisWords[i].memorias.trim()}☼ ")
                }

                outputWriter.flush()
                outputWriter.close()
                txtFile?.close()

                hideKeyboard()
                binding.etmemoris.clearFocus()








            } catch (e: java.lang.Exception) {
                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show()

            }

        }
        binding.btnSaveMem.setOnClickListener {
            saveWord()
        }
        binding.etmemoris.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                saveWord()

            }
            false
        }
    }
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        binding.etmemoris.requestFocus()
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
        initListener()
    }
    private fun initListener(){
        interstitial?.fullScreenContentCallback = object: FullScreenContentCallback(){
            override fun onAdDismissedFullScreenContent() {
                interstitial = null

            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                interstitial = null
            }

            override fun onAdShowedFullScreenContent() {
                interstitial = null
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        interstitial = null
    }

}