package com.example.proyecto

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.proyecto.databinding.FragmentEvaWordBinding
import java.io.InputStreamReader
import kotlin.random.Random


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

//se crea un List de todas las palabras insertadas
        val openFile = activity?.openFileInput("words.txt")
        val inputReader = InputStreamReader(openFile)
        val data =inputReader.readText()
        val datatoList = data.split(",")

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

        var wordTrad =mapWords[valorRandom(mapWords, binding.evaWO.editText!!)]
        Log.d("MAP",wordTrad.toString())

        binding.btnEvaWord.setOnClickListener {
            if(wordTrad == binding.evaWT.editText?.text.toString()){
                wordTrad = mapWords[valorRandom(mapWords, binding.evaWO.editText!!)]

            }else{

            }
            binding.evaWT.editText?.setText("")
        }
    }

    private fun valorRandom(mapValor: Map<String, String>, editWO: EditText):String{
        var randomMap = mapValor.entries.elementAt(Random.nextInt(mapValor.size))
        editWO.setText(randomMap.key)
        return randomMap.key
    }
}