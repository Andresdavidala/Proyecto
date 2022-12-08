package com.example.proyecto.Recycler.Customer

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.WordslistrecyclerviewBinding
import java.io.FileOutputStream
import java.io.OutputStreamWriter


class CustomerAdapter(var wordsDataList:List<DataWordsBase>, private val onClickDelete: (Int) -> Unit, private val fileContext: Context):RecyclerView.Adapter<CustomerAdapter.vhDataList>() {

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

//
    fun updateWord(words: List<DataWordsBase>){
        this.wordsDataList = words
        notifyDataSetChanged()
    }
    inner class vhDataList(view: View): RecyclerView.ViewHolder(view){

        val binding = WordslistrecyclerviewBinding.bind(view)

        @SuppressLint("ClickableViewAccessibility")
        fun renderData(dataListW: DataWordsBase, onClickDelete: (Int) -> Unit, fileContext: Context) {

            binding.tvWordOrg.text = dataListW.wordOrg
            binding.tvWordTrad.text = dataListW.wordTrad.replace("☼○", "")

            binding.tvWordOrg.setOnTouchListener(OnTouchListener { v, event -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            })
            binding.tvWordTrad.setOnTouchListener(OnTouchListener { v, event -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            })

            binding.etWordOrg.setOnTouchListener(OnTouchListener { v, event -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            })

            binding.etWordTrad.setOnTouchListener(OnTouchListener { v, event -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            })

//dentro del btn esta la validacion del boton borrar con alertDialog
            binding.btnDelete.setOnClickListener {
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
//                val sureDelete = AlertDialog.Builder(fileContext).setTitle("¿Quieres eliminar la palabra?").setPositiveButton("OK", null)
//                    .setNegativeButton("Cancel", null).show()
//                val okButton : Button = sureDelete.getButton(AlertDialog.BUTTON_POSITIVE)
//
//                okButton.setOnClickListener {
//                    onClickDelete(adapterPosition)
//                    sureDelete.dismiss()
//                }
//
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
                binding.etWordTrad.setText(dataListW.wordTrad.replace("☼○", ""))
                binding.btnCancel.visibility = View.VISIBLE
            }

            binding.btnCheck.setOnClickListener {
                binding.btnCheck.visibility = View.GONE
                binding.btnEdit.visibility = View.VISIBLE
                binding.tvWordOrg.visibility = View.VISIBLE
                binding.tvWordTrad.visibility = View.VISIBLE

                binding.etWordOrg.visibility = View.GONE
                binding.etWordTrad.visibility = View.GONE
                binding.btnCancel.visibility = View.GONE

                dataWordProvider.dataWords[adapterPosition] = DataWordsBase(binding.etWordOrg.text.toString().trim(), binding.etWordTrad.text.toString().trim())
                val txtfile: FileOutputStream? = fileContext.openFileOutput("myfile.txt",Context.MODE_PRIVATE) // se borra porque automaticamente el modoprivate lo borra desde aqui piensa
                val outputWriter = OutputStreamWriter(txtfile)
                try {

                    for( i in dataWordProvider.dataWords.indices){
                        outputWriter.write("${dataWordProvider.dataWords[i].wordOrg.trim()}☼○ ")
                        outputWriter.write("${dataWordProvider.dataWords[i].wordTrad.trim()}☼○ ")
                    }
                    Log.d("datosE", "FUNC")
                    Log.d("datosRV", dataWordProvider.dataWords.toString())

                    outputWriter.flush()
                    outputWriter.close()

                    notifyItemChanged(adapterPosition)
                }catch (_: java.lang.Exception){
                    Log.d("datosE", "ERRROR")
                }

            }

            binding.btnCancel.setOnClickListener {
                binding.btnEdit.visibility = View.VISIBLE
                binding.btnCheck.visibility = View.GONE
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnCancel.visibility = View.GONE
                binding.etWordOrg.visibility = View.GONE
                binding.etWordTrad.visibility = View.GONE
                binding.tvWordOrg.visibility = View.VISIBLE
                binding.tvWordTrad.visibility = View.VISIBLE

                binding.etWordOrg.setText(dataListW.wordOrg)
                binding.etWordTrad.setText(dataListW.wordTrad.replace("☼○", ""))


            }
        }
    }
}