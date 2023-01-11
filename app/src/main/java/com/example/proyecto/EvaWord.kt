package com.example.proyecto

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.google.android.gms.ads.interstitial.InterstitialAd
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class EvaWord : Fragment() {
    private var _binding: FragmentEvaWordBinding?=null
    private val binding get() = _binding!!
    private lateinit var openFile: FileInputStream
    private lateinit var inputReader:  InputStreamReader


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

}