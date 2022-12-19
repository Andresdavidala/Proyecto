package com.example.proyecto

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.Recycler.MemoriWords
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.FragmentCardMemorBinding
import com.example.proyecto.databinding.FragmentEvaWordBinding
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class cardMemor : Fragment() {
    private var _binding: FragmentCardMemorBinding?=null
    private val binding get() = _binding!!
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

        dataWordProvider.memorisWords.clear()
        Log.d("datosEmpty", "vacio")
        var txtFile = activity?.openFileOutput("memorias.txt", Context.MODE_APPEND) //important
//
        val openFile = activity?.openFileInput("memorias.txt")
        val inputReader = InputStreamReader(openFile)
        val data = inputReader.readText().trimEnd()
        val datatoList = data.split("☼○ ")

        var contWord = 0
        Log.d("datos8", data)
        Log.d("datos6", (datatoList.indices.toString()))
        Log.d("datos9", datatoList.toString())

        //sharedPref para el customDialog
        val sharedPrefCustom = activity?.getSharedPreferences("my_prefCustomMemo", Context.MODE_PRIVATE)
        val dialogShown = sharedPrefCustom?.getBoolean("dialog_shownMemo", false)



        if (!dialogShown!!) {
            //customDialog
            val customDialogView: View = LayoutInflater.from(context).inflate(R.layout.dialog_information, null)
            val customDialog = AlertDialog.Builder(context)
            customDialog.setView(customDialogView)
            val messagefind = customDialogView.findViewById<TextView>(R.id.tvInformation)
            val message = messagefind.setText("La sección memoria, te permite guardar palabras, frases, etc. que no tengan una respuesta. Puedes establecer el intervalo de " +
                    "tiempo que quieres que se te recuerde en la sección de configuración.")

            customDialog.setMessage(message.toString())
            val cancelBtn = customDialogView.findViewById<ImageView>(R.id.btnClose)

            val dialog = customDialog.create()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
            sharedPrefCustom.edit().putBoolean("dialog_shownMemo", true).apply()
        }

        //↑
        if (data.isNotEmpty()) {
            for (i in datatoList.indices) {

                dataWordProvider.memorisWords.add(
                    MemoriWords(
                        datatoList[contWord]
                    )
                )

                contWord += 1

                Log.d("datos9", datatoList.toString())
            }
            Log.d("datos2", "no vacio")
        } else {
            Log.d("datos2", "vacio")
        }


        Log.d("datos2", dataWordProvider.memorisWords.toString())
        fun Fragment.hideKeyboard() {
            view.let { activity?.hideKeyboard(it) }
        }

        fun saveWord(){
            val campoMem = binding.etmemoris.text.toString().trim()

            try {

                if(binding.etmemoris.text?.isEmpty()==true || TextUtils.isEmpty(campoMem) ){
                    Toast.makeText(context, "Debe llenar el campo de texto", Toast.LENGTH_SHORT).show()
                }else {
                    dataWordProvider.memorisWords.add(MemoriWords(campoMem))
                    Toast.makeText(context, "Palabra guardada correctamente!", Toast.LENGTH_SHORT)
                        .show()
                    binding.etmemoris.setText("")

                }
                //guardar en un textfile integrado dentro de la app↓

                txtFile = activity?.openFileOutput("memorias.txt", Context.MODE_PRIVATE)
                val outputWriter = OutputStreamWriter(txtFile)

                //escritura de datos ↓

                for (i in dataWordProvider.memorisWords.indices) {
                    outputWriter.write("${dataWordProvider.memorisWords[i].memorias.trim()}☼○ ")


                }

                outputWriter.flush()
                outputWriter.close()

                hideKeyboard()
                binding.etmemoris.clearFocus()





            } catch (e: java.lang.Exception) {
                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show()

            }

        }
        binding.btnSaveMem.setOnClickListener {
            saveWord()
        }
        binding.etmemoris.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                saveWord()

            }
            false
        })
    }
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        binding.etmemoris.requestFocus()
    }

}