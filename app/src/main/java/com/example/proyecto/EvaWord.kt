package com.example.proyecto

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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

        //desactivar el campo de palabra Orig

        //↑

//se crea un List de todas las palabras
        try{
            binding.wordTrad.hint = "Ingresa la traducción"
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

            binding.btnEvaWord.setOnClickListener {
                if(wordTrad.replace("☼○", "") == binding.evaWT.editText?.text.toString().trim()){
                    wordTrad =mapWords[valorRam(dataWordProvider.dataWords, binding.evaWO.editText)]!!

                }else{

                }
                binding.evaWT.editText?.setText("")
            }

        }catch (_: Exception){
            Toast.makeText(context, "No existen palabras registradas", Toast.LENGTH_SHORT ).show()
            binding.wordTrad.isFocusable = false
            binding.wordTrad.hint = " "
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