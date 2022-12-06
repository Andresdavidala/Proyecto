package com.example.proyecto

import android.annotation.SuppressLint
import android.app.RemoteInput
import android.content.Context
import android.media.MediaParser.InputReader
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import android.widget.Toast
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.FragmentSaveWordsBinding
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class SaveWords : Fragment() {
    private var _binding: FragmentSaveWordsBinding? = null
    private val binding get() = _binding!!

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


        Log.d("datos2", dataWordProvider.dataWords.toString())

        binding.btnSaveWord.setOnClickListener {
            val campoWordOrg = binding.wordOrg.text.toString().trim()
            val campoWordTrad = binding.wordTrad.text.toString().trim()
            try {

                if(binding.wordOrg.text?.isEmpty()==true || binding.wordTrad.text?.isEmpty()==true){
                    Toast.makeText(context, "Debe llenar los campos", Toast.LENGTH_SHORT).show()
                }else {
                    dataWordProvider.dataWords.add(DataWordsBase(campoWordOrg, campoWordTrad))
                    Toast.makeText(context, "Palabra guardada correctamente!", Toast.LENGTH_SHORT)
                        .show()
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


                binding.wordOrg.setText("")
                binding.wordTrad.setText("")


            } catch (e: java.lang.Exception) {
                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show()

            }

        }


    }

}