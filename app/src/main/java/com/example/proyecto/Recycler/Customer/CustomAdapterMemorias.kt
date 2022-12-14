package com.example.proyecto.Recycler.Customer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.Recycler.MemoriWords
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.WordmemorirecyclerBinding
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class CustomAdapterMemorias(var wordsDataList:List<MemoriWords>, private val onClickDelete: (Int) -> Unit, private val fileContext: Context):
    RecyclerView.Adapter<CustomAdapterMemorias.vhDataList>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapterMemorias.vhDataList {
        val dataLayout =LayoutInflater.from(parent.context)
        return vhDataList(dataLayout.inflate(R.layout.wordmemorirecycler, parent, false))
    }

    override fun onBindViewHolder(holder: CustomAdapterMemorias.vhDataList, position: Int) {
        val dataListPosition = wordsDataList[position]
        holder.renderData(dataListPosition, onClickDelete, fileContext)
    }

    override fun getItemCount(): Int {
        return wordsDataList.size
    }

    inner class vhDataList(view: View): RecyclerView.ViewHolder(view){

        val binding = WordmemorirecyclerBinding.bind(view)

        @SuppressLint("ClickableViewAccessibility")
        fun renderData(dataListW: MemoriWords, onClickDelete: (Int) -> Unit, fileContext: Context) {

            binding.tvWordOrg.text = dataListW.memorias.replace("☼○", "")

            binding.tvWordOrg.setOnTouchListener(View.OnTouchListener { v, event -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            })
//            binding.tvWordTrad.setOnTouchListener(View.OnTouchListener { v, event -> // Disallow the touch request for parent scroll on touch of child view
//                v.parent.requestDisallowInterceptTouchEvent(true)
//                false
//            })

            binding.etWordOrg.setOnTouchListener(View.OnTouchListener { v, event -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            })

//            binding.etWordTrad.setOnTouchListener(View.OnTouchListener { v, event -> // Disallow the touch request for parent scroll on touch of child view
//                v.parent.requestDisallowInterceptTouchEvent(true)
//                false
//            })

//dentro del btn esta la validacion del boton borrar con alertDialog
            binding.btnDelete2.setOnClickListener {
                val customDialogView: View = LayoutInflater.from(fileContext).inflate(R.layout.custom_dialog, null)
                val customDialog = AlertDialog.Builder(fileContext)
                customDialog.setView(customDialogView)
                val cancelBtn = customDialogView.findViewById<Button>(R.id.btnNegative)
                val okBtn = customDialogView.findViewById<Button>(R.id.btnPositive)

                val dialog = customDialog.create()

                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

                cancelBtn.setOnClickListener {
                    dialog.dismiss()
                }

                okBtn.setOnClickListener {
                    onClickDelete(adapterPosition)
                    dialog.dismiss()
                }

            }

            binding.btnEdit2.setOnClickListener {
                binding.btnEdit2.isSelected = !binding.btnEdit2.isSelected
            }


            binding.carViewWO.setOnClickListener {
                Log.d("datosWT", dataListW.memorias.replace("☼○", ""))
            }

            binding.btnEdit2.setOnClickListener {

                binding.btnCheck2.visibility = View.VISIBLE
                binding.btnEdit2.visibility = View.GONE
                binding.tvWordOrg.visibility = View.GONE
                binding.tvWordOrg.visibility = View.GONE

                binding.etWordOrg.visibility = View.VISIBLE
                binding.etWordOrg.visibility = View.VISIBLE
                binding.etWordOrg.setText(dataListW.memorias.replace("☼○", ""))
                binding.btnCancel2.visibility = View.VISIBLE

            }

            binding.btnCheck2.setOnClickListener {
                binding.btnCheck2.visibility = View.GONE
                binding.btnEdit2.visibility = View.VISIBLE
                binding.tvWordOrg.visibility = View.VISIBLE

                binding.etWordOrg.visibility = View.GONE
                binding.btnCancel2.visibility = View.GONE

                dataWordProvider.memorisWords[adapterPosition] = MemoriWords(binding.etWordOrg.text.toString().trim())
                val txtfile: FileOutputStream? = fileContext.openFileOutput("memorias.txt",Context.MODE_PRIVATE) // se borra porque automaticamente el modoprivate lo borra desde aqui piensa
                val outputWriter = OutputStreamWriter(txtfile)
                try {

                    for( i in dataWordProvider.memorisWords.indices){
                        outputWriter.write("${dataWordProvider.memorisWords[i].memorias.trim()}☼○ ")

                    }
                    Log.d("datosE", "FUNC")
                    Log.d("datosRV", dataWordProvider.memorisWords.toString())

                    outputWriter.flush()
                    outputWriter.close()

                    notifyItemChanged(adapterPosition)
                }catch (_: java.lang.Exception){
                    Log.d("datosE", "ERRROR")
                }

            }

            binding.btnCancel2.setOnClickListener {
                binding.btnEdit2.visibility = View.VISIBLE
                binding.btnCheck2.visibility = View.GONE
                binding.btnDelete2.visibility = View.VISIBLE
                binding.btnCancel2.visibility = View.GONE
                binding.etWordOrg.visibility = View.GONE
                binding.tvWordOrg.visibility = View.VISIBLE

                binding.etWordOrg.setText(dataListW.memorias.replace("☼○", ""))


            }
        }
    }


}