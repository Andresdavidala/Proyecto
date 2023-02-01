package com.example.proyecto

import android.annotation.SuppressLint
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
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.Recycler.MemoriWords
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.FragmentSaveWordsBinding
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class SaveWords : Fragment() {
    private var _binding: FragmentSaveWordsBinding? = null
    private val binding get() = _binding!!

    private lateinit var outputWriter: OutputStreamWriter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentSaveWordsBinding.inflate(inflater, container, false)


        return binding.root


    }

 

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sharedCountAds

        val sharedPreferences = activity?.getSharedPreferences("preferences_name", Context.MODE_PRIVATE)


        //sharedP count Ads
        val countShared = activity?.getSharedPreferences("sharedCountEva", Context.MODE_PRIVATE)
        MainActivity.contAds = countShared!!.getInt("valueCountEva", MainActivity.contAds)

        //Loading data to memorias
        dataWordProvider.memorisWords.clear()
        activity?.openFileOutput("memorias.txt", Context.MODE_APPEND) //important
//
        val openFileMem = activity?.openFileInput("memorias.txt")
        val inputReaderMem = InputStreamReader(openFileMem)
        val dataMem = inputReaderMem.readText().trimEnd()
        val datatoListMem = dataMem.split("☼ ")
        var contWordMem = 0
//        initAds()
        if (dataMem.isNotEmpty()) {
            for (i in datatoListMem.indices) {

                dataWordProvider.memorisWords.add(
                    MemoriWords(
                        datatoListMem[contWordMem]
                    )
                )

                contWordMem += 1

            }
        }
        //↑

        dataWordProvider.dataWords.clear()
        var txtFile = activity?.openFileOutput("myfile.txt", Context.MODE_APPEND) //important
//
        val openFile = activity?.openFileInput("myfile.txt")
        val inputReader = InputStreamReader(openFile)
        val data = inputReader.readText().trimEnd()
        val datatoList = data.split("☼ ")

        var contWord = 0

        if (data.isNotEmpty()) {
            for (i in datatoList.indices step 2) {

                dataWordProvider.dataWords.add(
                    DataWordsBase(
                        datatoList[contWord],
                        datatoList[contWord + 1]
                    )
                )

                contWord += 2

            }
        }



        binding.btnSHelp.setOnClickListener {
            val customDialogView: View = LayoutInflater.from(context).inflate(R.layout.dialog_information, null)
            val customDialog = AlertDialog.Builder(context)
            customDialog.setView(customDialogView)
            val messagefind = customDialogView.findViewById<TextView>(R.id.tvInformation)
            val message = messagefind.setText(R.string.helpSave)

            customDialog.setMessage(message.toString().replace("kotlin.Unit", ""))
            val cancelBtn = customDialogView.findViewById<ImageView>(R.id.btnClose)

            val dialog = customDialog.create()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
        }

        fun Fragment.hideKeyboard() {
            view.let { activity?.hideKeyboard(it) }
        }
        fun saveWord(){
            val campoWordOrg = binding.wordOrg.text.toString().trim()
            val campoWordTrad = binding.wordTrad.text.toString().trim()
            try {

                if(binding.wordOrg.text?.isEmpty()==true || binding.wordTrad.text?.isEmpty()==true || TextUtils.isEmpty(campoWordOrg) || TextUtils.isEmpty(campoWordTrad)){
                    Toast.makeText(context, R.string.toastSave, Toast.LENGTH_SHORT).show()
                }else {
                    dataWordProvider.dataWords.add(DataWordsBase(campoWordOrg, campoWordTrad))
                    Toast.makeText(context, R.string.toastsave2, Toast.LENGTH_SHORT)
                        .show()
                    binding.wordOrg.setText("")
                    binding.wordTrad.setText("")

                    //Interstitial
                    MainActivity.contAds += 1
                    val editorCount = countShared.edit()

                    MainActivity.showInterst(requireContext(), requireActivity())
                    editorCount.putInt("valueCountEva", MainActivity.contAds).apply()

//                    if(count == 5){
//                        initListener()
//                        count = 0
//                        editorCount.putInt("valueCountEva", count).apply()
//
//                    }
                    Log.d("datos", MainActivity.contAds.toString())

                }
                //guardar en un textfile integrado dentro de la app↓

                txtFile = activity?.openFileOutput("myfile.txt", Context.MODE_PRIVATE)
                outputWriter = OutputStreamWriter(txtFile)

                //escritura de datos ↓

                for (i in dataWordProvider.dataWords.indices) {
                    outputWriter.write("${dataWordProvider.dataWords[i].wordOrg.trim()}☼ ")
                    outputWriter.write("${dataWordProvider.dataWords[i].wordTrad.trim()}☼ ")


                }

//                outputWriter.flush()
//                outputWriter.close()

                hideKeyboard()
                binding.wordOrg.clearFocus()





            } catch (e: java.lang.Exception) {
                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show()

            }finally {
                outputWriter.flush()
                outputWriter.close()
                txtFile?.close()
            }
        }
        binding.btnSaveWord.setOnClickListener {
            saveWord()


        }


        binding.wordTrad.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                saveWord()

            }
            false
        }


    }
//    override fun onDestroy() {
//        super.onDestroy()
//
//        // Cerrar el archivo y liberar el recurso
//        outputWriter.flush()
//        outputWriter.close()
//    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        binding.wordOrg.requestFocus()
    }

    //interstitial function
//    private fun initInterstitial(){
//        val adRequest = AdRequest.Builder().build()
//        InterstitialAd.load(requireActivity(), "ca-app-pub-3940256099942544/1033173712", adRequest, object: InterstitialAdLoadCallback(){
//            override fun onAdLoaded(interst: InterstitialAd) {
//                interstitial = interst
//            }
//
//            override fun onAdFailedToLoad(intert: LoadAdError) {
//                interstitial = null
//            }
//
//        })
//    }
//
//    private fun showAds(){
//        interstitial?.show(requireActivity())
//    }
//
//    private fun checkCount(){
//        showAds()
//        initInterstitial()
//        initListener()
//    }
//
//    private fun initListener(){
//        if(interstitial != null){
//            interstitial?.fullScreenContentCallback = object: FullScreenContentCallback(){
//                override fun onAdDismissedFullScreenContent() {
//                    interstitial = null
//                    initInterstitial()
//
//                }
//
//                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                    interstitial = null
//                }
//
//                override fun onAdShowedFullScreenContent() {
//                }
//            }
//        }
//
//        this@SaveWords.activity?.let { interstitial?.show(it) }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        interstitial = null
//    }

}