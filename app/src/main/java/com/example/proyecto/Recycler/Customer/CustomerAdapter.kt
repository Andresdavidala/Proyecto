package com.example.proyecto.Recycler.Customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.databinding.FragmentListWordsBinding
import com.example.proyecto.databinding.WordslistrecyclerviewBinding

class CustomerAdapter(val wordsDataList:List<DataWordsBase>):RecyclerView.Adapter<CustomerAdapter.vhDataList>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vhDataList {
        val dataLayout =LayoutInflater.from(parent.context)
        return vhDataList(dataLayout.inflate(R.layout.wordslistrecyclerview, parent, false))
    }

    override fun onBindViewHolder(holder: vhDataList, position: Int) {
        val dataListPosition = wordsDataList[position]
        holder.renderData(dataListPosition)
    }

    override fun getItemCount(): Int {
        return wordsDataList.size
    }

    inner class vhDataList(view: View): RecyclerView.ViewHolder(view){
        val binding = WordslistrecyclerviewBinding.bind(view)
        fun renderData(dataListW: DataWordsBase){
            binding.tvWordOrg.text = dataListW.wordOrg
            binding.tvWordTrad.text = dataListW.wordTrad.replace(",", "")
        }
    }
}