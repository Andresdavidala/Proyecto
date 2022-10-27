package com.example.proyecto

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
import com.example.proyecto.databinding.FragmentSaveWordsBinding
import java.io.InputStreamReader
import java.io.OutputStreamWriter


/**
 * A simple [Fragment] subclass.
 * Use the [SaveWords.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaveWords : Fragment() {
    private var _binding: FragmentSaveWordsBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentSaveWordsBinding.inflate(inflater, container, false)


        return binding.root

        //return inflater.inflate(R.layout.fragment_save_words, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSaveWord.setOnClickListener {
            try {
                //guardar en un textfile integrado dentro de la app↓
                val txtFile = activity?.openFileOutput("words.txt", Context.MODE_APPEND)
                var outputWriter = OutputStreamWriter(txtFile)

                //escritura de datos ↓
                if(binding.wordOrg.text?.isEmpty()==true || binding.wordTrad.text?.isEmpty()==true){
                    Toast.makeText(context, "Error debe llenar los campos", Toast.LENGTH_SHORT).show()
                }else{
                    outputWriter.write(binding.wordOrg.text.toString().trim()+",")
                    outputWriter.write(binding.wordTrad.text.toString().trim()+",")
                    Toast.makeText(context, "Palabra guardada correctamente!", Toast.LENGTH_SHORT).show()

                }






                outputWriter.flush()
                outputWriter.close()


                binding.wordOrg.setText("")
                binding.wordTrad.setText("")






            }catch (e: java.lang.Exception){
                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show()

            }


        }
    }

}