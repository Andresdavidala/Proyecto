package com.example.proyecto

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

        binding.btnLHelp.setOnClickListener {
            //customDialog
            val customDialogView: View = LayoutInflater.from(context).inflate(R.layout.dialog_information, null)
            val customDialog = AlertDialog.Builder(context)
            customDialog.setView(customDialogView)
            val messagefind = customDialogView.findViewById<TextView>(R.id.tvInformation)
            val message = messagefind.setText("La sección lista te permite visualizar, editar y eliminar las preguntas y memorias que hayas guardado. Estan divididas en " +
                    "dos secciones, que puedes ver presionando el switch que se encuentra en la parte superior.")

            customDialog.setMessage(message.toString().replace("kotlin.Unit", ""))
            val cancelBtn = customDialogView.findViewById<ImageView>(R.id.btnClose)

            val dialog = customDialog.create()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }
        }

        //sharedP
        val switchRV = activity?.getSharedPreferences("switchRv", AppCompatActivity.MODE_PRIVATE)
        binding.switchRV.isChecked = switchRV!!.getBoolean("valSwitchRV", false)

        val rvVisible = activity?.getSharedPreferences("rvVisible", AppCompatActivity.MODE_PRIVATE)
        binding.rvDataList.isVisible = rvVisible!!.getBoolean("valRVVisible", binding.rvDataList.isVisible)

        val rvVisibleMem = activity?.getSharedPreferences("rvVisibleMem", AppCompatActivity.MODE_PRIVATE)
        binding.rvMemorias.isVisible = rvVisibleMem!!.getBoolean("valRVVisibleMem", false)

        val tvPalabras = activity?.getSharedPreferences("tvWords", AppCompatActivity.MODE_PRIVATE)
        binding.tvPreg.isVisible = tvPalabras!!.getBoolean("tvWordsVal", binding.tvPreg.isVisible)

        val tvMem = activity?.getSharedPreferences("tvWordsMem", AppCompatActivity.MODE_PRIVATE)
        binding.tvMem.isVisible = tvMem!!.getBoolean("tvWordsVal", binding.tvMem.isVisible)
        //↑



        binding.switchRV.setOnCheckedChangeListener { compoundButton, b ->

            if(b){
                binding.filterET.visibility = View.GONE
                binding.filterETMem.visibility = View.VISIBLE

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

                binding.filterET.setText("")

                binding.rvDataList.isVisible = false
                binding.rvMemorias.isVisible = true
                binding.tvMem.isVisible = true
                binding.tvPreg.isVisible = false

            }else{

                binding.filterET.visibility = View.VISIBLE
                binding.filterETMem.visibility = View.GONE

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

        //creo los et filter para filtrar las palabras, ahora bien tuve que crear dos debido a que con uno generaba un error

        binding.filterETMem.addTextChangedListener { filMem ->
            try{
                val findWordMem = dataWordProvider.memorisWords.indexOfFirst {it.memorias.lowercase().contains(filMem.toString().lowercase())}//si la lista memorias contiene los valores que le ingresamos en el etFilter
                binding.rvMemorias.smoothScrollToPosition(findWordMem)
            }catch (_: Exception){

            }
        }
        binding.filterET.addTextChangedListener {filerWord->
//            val listData = binding.filterET.text.toString().trim()


            try{
//                val superheroesFiltered = superHeroMutableList.filter { superhero -> superhero.superhero.contains (userFilter.toString()) }
                val findWor = dataWordProvider.dataWords.filter { words -> dataWordProvider.dataWords[0].wordOrg.contains(filerWord.toString()) }


                val findWord = dataWordProvider.dataWords.indexOfFirst {
                    it.wordOrg.lowercase().contains(filerWord.toString().lowercase()) || it.wordTrad.lowercase().contains(filerWord.toString().lowercase())
                }

//                val findWord = dataWordProvider.dataWords.indexOfFirst {
//                    it.wordOrg.replace("☼○", "").lowercase() == listData.lowercase() || it.wordTrad.replace("☼○", "").lowercase() == listData.lowercase()
//                }





//                val findWordMem = dataWordProvider.memorisWords.indexOfFirst {
//                    it.memorias.replace("☼○", "").lowercase() == listData.lowercase() || it.memorias.replace("☼○", "").lowercase() == listData.lowercase()
//                }

//                if(findWord == -1 || findWordMem == -1){
//                    binding.rvDataList.scrollToPosition(0)
//                    binding.rvMemorias.scrollToPosition(0)
//                }
                binding.rvDataList.smoothScrollToPosition(findWord)

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