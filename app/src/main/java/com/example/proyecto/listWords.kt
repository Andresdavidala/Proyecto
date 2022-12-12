package com.example.proyecto

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto.Recycler.Customer.CustomerAdapter
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.FragmentListWordsBinding
import java.io.OutputStreamWriter


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
        binding.filterET.addTextChangedListener {
            val listData = binding.filterET.text.toString().trim()


            try{
                val findWord = dataWordProvider.dataWords.indexOfFirst {
                    it.wordOrg.replace("☼○", "").lowercase() == listData.lowercase() || it.wordTrad.replace("☼○", "").lowercase() == listData.lowercase()
                }


                if(findWord == -1){
                    binding.rvDataList.scrollToPosition(0)
                }
                Log.i("datos", findWord.toString())
                binding.rvDataList.smoothScrollToPosition(findWord+2)

            }catch (_: Exception){

            }

        }


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