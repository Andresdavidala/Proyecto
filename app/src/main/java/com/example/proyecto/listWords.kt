package com.example.proyecto

import android.content.Context
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.Recycler.Customer.CustomerAdapter
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.FragmentEvaWordBinding
import com.example.proyecto.databinding.FragmentListWordsBinding
import java.io.*


class listWords : Fragment() {
    private var _binding: FragmentListWordsBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: CustomerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentListWordsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iniRecyclerView()



    }

    fun listWrite(context: Context){

    }
    fun iniRecyclerView(){

        adapter = CustomerAdapter(dataWordProvider.dataWords,{position -> onDeleteWord(position)}, requireContext())
        val recyclerView= binding.rvDataList


        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter


    }


    fun onDeleteWord(position: Int){

        //proceso para eliminar palabras en el recyclerView
        val txtFile = activity?.openFileOutput("myfile.txt", Context.MODE_PRIVATE)
        val outputWriter = OutputStreamWriter(txtFile)
        dataWordProvider.dataWords.removeAt(position)

        Log.d("datosRV", dataWordProvider.dataWords.toString())

        try{
            for( i in dataWordProvider.dataWords.indices){
                outputWriter.write("${dataWordProvider.dataWords[i].wordOrg.trim()}☼○ ")
                outputWriter.write("${dataWordProvider.dataWords[i].wordTrad.trim()}☼○ ")
            }
        }catch (_: java.lang.Exception){

        }
        outputWriter.flush()
        outputWriter.close()

        //se notifica al recycler que se elimino un elemento
        adapter.notifyItemRemoved(position)

    }

}