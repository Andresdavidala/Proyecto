package com.example.proyecto

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto.Recycler.Customer.CustomAdapterMemorias
import com.example.proyecto.Recycler.Customer.CustomerAdapter
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.FragmentListWordsBinding
import java.io.OutputStreamWriter


class listWords : Fragment() {
    private var _binding: FragmentListWordsBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: CustomerAdapter
    private lateinit var adapterMemoris: CustomAdapterMemorias
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

        //sharedP
        val switchRV = activity?.getSharedPreferences("switchRv", AppCompatActivity.MODE_PRIVATE)
        binding.switchRV.isChecked = switchRV!!.getBoolean("valSwitchRV", false)

        val rvVisible = activity?.getSharedPreferences("rvVisible", AppCompatActivity.MODE_PRIVATE)
        binding.rvDataList.isVisible = rvVisible!!.getBoolean("valRVVisible", binding.rvDataList.isVisible)

        val rvVisibleMem = activity?.getSharedPreferences("rvVisibleMem", AppCompatActivity.MODE_PRIVATE)
        binding.rvMemorias.isVisible = rvVisibleMem!!.getBoolean("valRVVisibleMem", !binding.rvMemorias.isVisible)

        val tvPalabras = activity?.getSharedPreferences("tvWords", AppCompatActivity.MODE_PRIVATE)
        binding.tvPreg.isVisible = tvPalabras!!.getBoolean("tvWordsVal", binding.tvPreg.isVisible)

        val tvMem = activity?.getSharedPreferences("tvWordsMem", AppCompatActivity.MODE_PRIVATE)
        binding.tvMem.isVisible = tvMem!!.getBoolean("tvWordsVal", !binding.tvMem.isVisible)
        //↑



        binding.switchRV.setOnCheckedChangeListener { compoundButton, b ->

            if(b){
                //sharedP
                val switchValBbl  = activity?.getSharedPreferences("switchRv", AppCompatActivity.MODE_PRIVATE)!!.edit()
                switchValBbl.putBoolean("valSwitchRV", true).apply()

                val rvVisibleVal = activity?.getSharedPreferences("rvVisible", AppCompatActivity.MODE_PRIVATE)!!.edit()
                rvVisibleVal.putBoolean("valRVVisible", false).apply()

                val rvVisibleMemVal = activity?.getSharedPreferences("rvVisibleMem", AppCompatActivity.MODE_PRIVATE)!!.edit()
                rvVisibleMemVal.putBoolean("valRVVisibleMem", true).apply()

                val tvPalabrasVal = activity?.getSharedPreferences("tvWords", AppCompatActivity.MODE_PRIVATE)!!.edit()
                tvPalabrasVal.putBoolean("tvWordsVal", false).apply()

                val tvMemVal = activity?.getSharedPreferences("tvWordsMem", AppCompatActivity.MODE_PRIVATE)!!.edit()
                tvMemVal.putBoolean("tvWordsVal", true).apply()
                //↑


                binding.rvDataList.isVisible = false
                binding.rvMemorias.isVisible = true
                binding.tvMem.isVisible = true
                binding.tvPreg.isVisible = false

            }else{
                //sharedP
                val switchValBbl  = activity?.getSharedPreferences("switchRv", AppCompatActivity.MODE_PRIVATE)!!.edit()
                switchValBbl.putBoolean("valSwitchRV", false).apply()

                val rvVisibleVal = activity?.getSharedPreferences("rvVisible", AppCompatActivity.MODE_PRIVATE)!!.edit()
                rvVisibleVal.putBoolean("valRVVisible", true).apply()

                val rvVisibleMemVal = activity?.getSharedPreferences("rvVisibleMem", AppCompatActivity.MODE_PRIVATE)!!.edit()
                rvVisibleMemVal.putBoolean("valRVVisibleMem", false).apply()

                val tvPalabrasVal = activity?.getSharedPreferences("tvWords", AppCompatActivity.MODE_PRIVATE)!!.edit()
                tvPalabrasVal.putBoolean("tvWordsVal", true).apply()

                val tvMemVal = activity?.getSharedPreferences("tvWordsMem", AppCompatActivity.MODE_PRIVATE)!!.edit()
                tvMemVal.putBoolean("tvWordsVal", false).apply()
                //↑

                binding.rvDataList.isVisible = true
                binding.rvMemorias.isVisible = false
                binding.tvMem.isVisible = false
                binding.tvPreg.isVisible = true

            }
        }


        iniRecyclerView()
        initRecyclerMemorisView()

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

    fun initRecyclerMemorisView(){
        adapterMemoris = CustomAdapterMemorias(dataWordProvider.memorisWords,{position -> onDeletMemoris(position)}, requireContext())
        val recyclerView = binding.rvMemorias
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapterMemoris
    }
    fun iniRecyclerView(){

        adapter = CustomerAdapter(dataWordProvider.dataWords,{position -> onDeleteWord(position)}, requireContext())
        val recyclerView= binding.rvDataList



        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = adapter


    }

    fun onDeletMemoris(position: Int){
        //proceso para eliminar palabras en el recyclerView
        val txtFile = activity?.openFileOutput("memorias.txt", Context.MODE_PRIVATE)
        val outputWriter = OutputStreamWriter(txtFile)
        dataWordProvider.memorisWords.removeAt(position)

        Log.d("datosRV", dataWordProvider.memorisWords.toString())

        try{
            for( i in dataWordProvider.memorisWords.indices){
                outputWriter.write("${dataWordProvider.memorisWords[i].memorias.trim()}☼○ ")
            }
        }catch (_: java.lang.Exception){

        }
        outputWriter.flush()
        outputWriter.close()

        //se notifica al recycler que se elimino un elemento
        adapterMemoris.notifyItemRemoved(position)
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