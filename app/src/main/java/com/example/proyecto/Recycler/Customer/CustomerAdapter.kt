package com.example.proyecto.Recycler.Customer

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.WordslistrecyclerviewBinding
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class CustomerAdapter(val wordsDataList:List<DataWordsBase>, private val onClickDelete: (Int) -> Unit, private val fileContext: Context):RecyclerView.Adapter<CustomerAdapter.vhDataList>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vhDataList {
        val dataLayout =LayoutInflater.from(parent.context)
        return vhDataList(dataLayout.inflate(R.layout.wordslistrecyclerview, parent, false))
    }

    override fun onBindViewHolder(holder: vhDataList, position: Int) {
        val dataListPosition = wordsDataList[position]
        holder.renderData(dataListPosition, onClickDelete, fileContext)
    }

    override fun getItemCount(): Int {
        return wordsDataList.size
    }

    inner class vhDataList(view: View): RecyclerView.ViewHolder(view){


        val binding = WordslistrecyclerviewBinding.bind(view)

        fun renderData(dataListW: DataWordsBase, onClickDelete: (Int) -> Unit, fileContext: Context) {




            binding.tvWordOrg.text = dataListW.wordOrg
            binding.tvWordTrad.text = dataListW.wordTrad.replace(",", "")


            binding.btnDelete.setOnClickListener {
                onClickDelete(adapterPosition)
            }

            binding.btnEdit.setOnClickListener {
                binding.btnEdit.isSelected = !binding.btnEdit.isSelected
            }

            binding.carViewWO.setOnClickListener {
                Log.d("datosWT", dataListW.wordOrg)
            }
            binding.cardViewWT.setOnClickListener {
                Log.d("datosWT", dataListW.wordTrad)
            }





            binding.btnEdit.setOnClickListener {

                binding.btnCheck.visibility = View.VISIBLE
                binding.btnEdit.visibility = View.GONE
                binding.tvWordOrg.visibility = View.GONE
                binding.tvWordTrad.visibility = View.GONE

                binding.etWordOrg.visibility = View.VISIBLE
                binding.etWordTrad.visibility = View.VISIBLE
                binding.etWordOrg.setText(dataListW.wordOrg)
                binding.etWordTrad.setText(dataListW.wordTrad.replace(",", ""))

            }

            binding.btnCheck.setOnClickListener {
                binding.btnCheck.visibility = View.GONE
                binding.btnEdit.visibility = View.VISIBLE
                binding.tvWordOrg.visibility = View.VISIBLE
                binding.tvWordTrad.visibility = View.VISIBLE

                binding.etWordOrg.visibility = View.GONE
                binding.etWordTrad.visibility = View.GONE


//                val os: FileOutputStream = openFileOutput("myfile.txt", Context.MODE_PRIVATE)
                dataWordProvider.dataWords[adapterPosition] = DataWordsBase(binding.etWordOrg.text.toString().trim(), binding.etWordTrad.text.toString().trim())
                val txtfile: FileOutputStream? = fileContext.openFileOutput("myfile.txt",Context.MODE_PRIVATE) // se borra porque automaticamente el modoprivate lo borra desde aqui piensa
                val outputWriter = OutputStreamWriter(txtfile)
                try {

                    for( i in dataWordProvider.dataWords.indices){
                        outputWriter.write("${dataWordProvider.dataWords[i].wordOrg.trim()}, ")
                        outputWriter.write("${dataWordProvider.dataWords[i].wordTrad.trim()}, ")
                    }
                    Log.d("datosE", "FUNC")
                    Log.d("datosRV", dataWordProvider.dataWords.toString())

                    outputWriter.flush()
                    outputWriter.close()
                }catch (_: java.lang.Exception){
                    Log.d("datosE", "ERRROR")
                }

//                val txtFile = activity?.openFileOutput("myfile.txt", Context.MODE_PRIVATE)
//            var outputWriter = OutputStreamWriter(txtFile)

//            var outputWriter = OutputStreamWriter(txtFile)

            }



        }
    }
}