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
import java.io.InputStreamReader


class EvaWord : Fragment() {
    private var _binding: FragmentEvaWordBinding?=null
    private val binding get() = _binding!!




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




        //sharedPref para el customDialog
        val sharedPrefCustom = activity?.getSharedPreferences("my_prefCustomEva", Context.MODE_PRIVATE)
        val dialogShown = sharedPrefCustom?.getBoolean("dialog_shownEva", false)



        if (!dialogShown!!) {
            //customDialog
            val customDialogView: View = LayoutInflater.from(context).inflate(R.layout.dialog_information, null)
            val customDialog = AlertDialog.Builder(context)
            customDialog.setView(customDialogView)
            val messagefind = customDialogView.findViewById<TextView>(R.id.tvInformation)
            val message = messagefind.setText("La sección evaluación, te permite practicar tus preguntas, es decir deberas ingresar la respuesta a la pregunta" +
                    "que tu mismo guardaste y si correcto podras pasar a tu siguiente pregunta")

            customDialog.setMessage(message.toString().replace("kotlin.Unit", ""))
            val cancelBtn = customDialogView.findViewById<ImageView>(R.id.btnClose)

            val dialog = customDialog.create()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
            sharedPrefCustom.edit().putBoolean("dialog_shownEva", true).apply()
        }

        //↑

//se crea un List de todas las palabras
        try{
            binding.wordTrad.hint = "Ingresa tu respuesta"
            val openFile = activity?.openFileInput("myfile.txt")
            val inputReader = InputStreamReader(openFile)
            val data =inputReader.readText().trimEnd()
            val datatoList = data.split("☼○ ")


            Log.d("datos", datatoList.toString())
            var mapWords: MutableMap<String, String> = mutableMapOf()
            var numWord = 0
            for(i in datatoList.indices){
                try{
                    mapWords[datatoList[numWord]] = datatoList[numWord+1]
                    numWord += 2
                }catch (e: java.lang.IndexOutOfBoundsException){

                }

            }

            Log.d("mapword", mapWords.toString())

            var wordTrad =mapWords[valorRam(dataWordProvider.dataWords, binding.evaWO.editText)]!!
            Log.d("MAP",wordTrad.toString())

            fun evaWord(){
                if(wordTrad.replace("☼○", "").equals(binding.evaWT.editText?.text.toString().trim(), true)){
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


            binding.wordTrad.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
                if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_SEND) {
                    evaWord()

                }
                true
            })



        }catch (_: Exception){
//            Toast.makeText(context, "No existen palabras registradas", Toast.LENGTH_SHORT ).show()
            binding.wordTrad.isFocusable = false
            binding.wordTrad.hint = " No existen preguntas"
        }

    }

    private fun valorRam(valList:MutableList<DataWordsBase>, editEvaluar: EditText?): String {
        var list = valList.shuffled().take(1)[0]
        var wordReturn = list.wordOrg
        editEvaluar?.setText(wordReturn)

        Log.d("datosMap", wordReturn)
        return wordReturn
    }




//    private fun valorRandom(mapValor: Map<String, String>, editWO: EditText?):String{
//        var randomMap = mapValor.entries.elementAt(Random.nextInt(mapValor.size))
//        editWO?.setText(randomMap.key)
//        return randomMap.key
//    }
}