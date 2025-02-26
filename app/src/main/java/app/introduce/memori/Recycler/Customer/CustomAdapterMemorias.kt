package app.introduce.memori.Recycler.Customer

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
import app.introduce.memori.Recycler.MemoriWords
import app.introduce.memori.Recycler.dataWordProvider
import com.example.proyecto.databinding.WordmemorirecyclerBinding
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class CustomAdapterMemorias(private var wordsDataList:List<MemoriWords>, private val onClickDelete: (Int) -> Unit, private val fileContext: Context):
    RecyclerView.Adapter<CustomAdapterMemorias.vhDataList>() {
    var isSuppressed = false
    //interstitial


    //↑
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): vhDataList {
        val dataLayout =LayoutInflater.from(parent.context)
        return vhDataList(dataLayout.inflate(R.layout.wordmemorirecycler, parent, false))
    }

    override fun onBindViewHolder(holder: vhDataList, position: Int) {
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


            binding.tvWordOrg.text = dataListW.memorias.replace("☼", "")

            binding.tvWordOrg.setOnTouchListener { v, _ -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }


            binding.etWordOrg.setOnTouchListener { v, _ -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }

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
                    //↑

                }
                it.isEnabled = false
                it.postDelayed({
                    it.isEnabled = true
                }, 500)

            }

            binding.btnEdit2.setOnClickListener {
                binding.btnEdit2.isSelected = !binding.btnEdit2.isSelected
            }


            binding.btnEdit2.setOnClickListener {

                Log.d("datos", "edit")
               if(!isSuppressed){
                   binding.btnCheck2.visibility = View.VISIBLE
                   binding.btnEdit2.visibility = View.GONE
                   binding.tvWordOrg.visibility = View.GONE
                   binding.tvWordOrg.visibility = View.GONE
                   binding.btnDelete2.visibility = View.GONE

                   binding.etWordOrg.visibility = View.VISIBLE
                   binding.etWordOrg.visibility = View.VISIBLE
                   binding.etWordOrg.setText(dataListW.memorias.replace("☼", ""))
                   binding.btnCancel2.visibility = View.VISIBLE

                   var v: View = it

                   while (v.parent != null) {
                       v = v.parent as View
                       if (v is RecyclerView) {
                           v.suppressLayout(true)
                           break
                       }
                   }
                   Log.d("datos", "inside")
               }
                isSuppressed = true

                it.isEnabled = false
                it.postDelayed({
                    it.isEnabled = true
                }, 1500)

            }

            binding.btnCheck2.setOnClickListener {

                Log.d("datoscheck", "check")
                var v: View = it
                while (v.parent != null) {
                    v = v.parent as View
                    if (v is RecyclerView) {
                        (v as RecyclerView).suppressLayout(false)
                        break
                    }
                }
                isSuppressed = false
                binding.btnCheck2.visibility = View.GONE
                binding.btnEdit2.visibility = View.VISIBLE
                binding.tvWordOrg.visibility = View.VISIBLE
                binding.btnDelete2.visibility = View.VISIBLE

                binding.etWordOrg.visibility = View.GONE
                binding.btnCancel2.visibility = View.GONE

                dataWordProvider.memorisWords[adapterPosition] = MemoriWords(binding.etWordOrg.text.toString().trim())
                val txtfile: FileOutputStream? = fileContext.openFileOutput("memorias.txt",Context.MODE_PRIVATE) // se borra porque automaticamente el modoprivate lo borra desde aqui piensa
                val outputWriter = OutputStreamWriter(txtfile)
                try {

                    for( i in dataWordProvider.memorisWords.indices){
                        outputWriter.write("${dataWordProvider.memorisWords[i].memorias.trim()}☼ ")

                    }

                    outputWriter.flush()
                    outputWriter.close()

                    notifyItemChanged(adapterPosition)


                }catch (_: java.lang.Exception){
                }


            }

            binding.btnCancel2.setOnClickListener {
                var v: View = it
                while (v.parent != null) {
                    v = v.parent as View
                    if (v is RecyclerView) {
                        (v as RecyclerView).suppressLayout(false)
                        break
                    }
                }
                isSuppressed = false
                binding.btnEdit2.visibility = View.VISIBLE
                binding.btnCheck2.visibility = View.GONE
                binding.btnDelete2.visibility = View.VISIBLE
                binding.btnCancel2.visibility = View.GONE
                binding.etWordOrg.visibility = View.GONE
                binding.tvWordOrg.visibility = View.VISIBLE

                binding.etWordOrg.setText(dataListW.memorias.replace("☼", ""))


            }
        }


    }

}