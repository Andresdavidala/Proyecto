package app.example.proyecto.Recycler.Customer

import android.annotation.SuppressLint
import android.app.Activity
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
import app.example.proyecto.MainActivity
import com.example.proyecto.R
import app.example.proyecto.Recycler.DataWordsBase
import app.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.WordslistrecyclerviewBinding
import java.io.FileOutputStream
import java.io.OutputStreamWriter


class CustomerAdapter(private var wordsDataList:List<DataWordsBase>, private val onClickDelete: (Int) -> Unit, private val fileContext: Context):RecyclerView.Adapter<CustomerAdapter.vhDataList>() {
    var isSuppressed = false

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

    inner class vhDataList(view: View): RecyclerView.ViewHolder(view) {

        val binding = WordslistrecyclerviewBinding.bind(view)

        //vamos al shared


        @SuppressLint("ClickableViewAccessibility")
        fun renderData(
            dataListW: DataWordsBase,
            onClickDelete: (Int) -> Unit,
            fileContext: Context
        ) {

            binding.tvWordOrg.text = dataListW.wordOrg
            binding.tvWordTrad.text = dataListW.wordTrad.replace("☼", "")

            binding.tvWordOrg.setOnTouchListener { v, _ -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }
            binding.tvWordTrad.setOnTouchListener { v, _ -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }

            binding.etWordOrg.setOnTouchListener { v, _ -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }

            binding.etWordTrad.setOnTouchListener { v, _ -> // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }

//dentro del btn esta la validacion del boton borrar con alertDialog
            binding.btnDelete.setOnClickListener {
                val customDialogView: View =
                    LayoutInflater.from(fileContext).inflate(R.layout.custom_dialog, null)
                val customDialog = AlertDialog.Builder(fileContext)
                customDialog.setView(customDialogView)
                val cancelBtn = customDialogView.findViewById<Button>(R.id.btnNegative)
                val okBtn = customDialogView.findViewById<Button>(R.id.btnPositive)

                val dialog = customDialog.create()
//                initAds()



                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

                cancelBtn.setOnClickListener {
                    dialog.dismiss()
                }

                okBtn.setOnClickListener {

                    onClickDelete(adapterPosition)
                    dialog.dismiss()


                    Log.d("datos", MainActivity.contAds.toString())
                }

                it.isEnabled = false
                it.postDelayed({
                    it.isEnabled = true
                }, 500)

            }

            binding.btnEdit.setOnClickListener {
                binding.btnEdit.isSelected = !binding.btnEdit.isSelected
            }

            binding.btnEdit.setOnClickListener {
                if(!isSuppressed){
                    binding.btnCheck.visibility = View.VISIBLE
                    binding.btnEdit.visibility = View.GONE
                    binding.tvWordOrg.visibility = View.GONE
                    binding.tvWordTrad.visibility = View.GONE
                    binding.btnDelete.visibility = View.GONE

                    binding.etWordOrg.visibility = View.VISIBLE
                    binding.etWordTrad.visibility = View.VISIBLE
                    binding.etWordOrg.setText(dataListW.wordOrg)
                    binding.etWordTrad.setText(dataListW.wordTrad.replace("☼", ""))
                    binding.btnCancel.visibility = View.VISIBLE

                    var v: View = it

                    while (v.parent != null) {
                        v = v.parent as View
                        if (v is RecyclerView) {
                            v.suppressLayout(true)
                            break
                        }
                    }
                }

                isSuppressed = true

                it.isEnabled = false
                it.postDelayed({
                    it.isEnabled = true
                }, 1500)

            }

            binding.btnCheck.setOnClickListener {
                var v: View = it
                while (v.parent != null) {
                    v = v.parent as View
                    if (v is RecyclerView) {
                        (v as RecyclerView).suppressLayout(false)
                        break
                    }
                }
                isSuppressed = false

                binding.btnCheck.visibility = View.GONE
                binding.btnEdit.visibility = View.VISIBLE
                binding.tvWordOrg.visibility = View.VISIBLE
                binding.tvWordTrad.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE

                binding.etWordOrg.visibility = View.GONE
                binding.etWordTrad.visibility = View.GONE
                binding.btnCancel.visibility = View.GONE

                dataWordProvider.dataWords[adapterPosition] = DataWordsBase(
                    binding.etWordOrg.text.toString().trim(),
                    binding.etWordTrad.text.toString().trim()
                )
                val txtfile: FileOutputStream? = fileContext.openFileOutput(
                    "myfile.txt",
                    Context.MODE_PRIVATE
                ) // se borra porque automaticamente el modoprivate lo borra desde aqui piensa
                val outputWriter = OutputStreamWriter(txtfile)
                try {

                    for (i in dataWordProvider.dataWords.indices) {
                        outputWriter.write("${dataWordProvider.dataWords[i].wordOrg.trim()}☼ ")
                        outputWriter.write("${dataWordProvider.dataWords[i].wordTrad.trim()}☼ ")
                    }

                    outputWriter.flush()
                    outputWriter.close()

                    notifyItemChanged(adapterPosition)



                } catch (_: java.lang.Exception) {
                }


            }

            binding.btnCancel.setOnClickListener {
                var v: View = it
                while (v.parent != null) {
                    v = v.parent as View
                    if (v is RecyclerView) {
                        (v as RecyclerView).suppressLayout(false)
                        break
                    }
                }
                isSuppressed = false

                binding.btnEdit.visibility = View.VISIBLE
                binding.btnCheck.visibility = View.GONE
                binding.btnDelete.visibility = View.VISIBLE
                binding.btnCancel.visibility = View.GONE
                binding.etWordOrg.visibility = View.GONE
                binding.etWordTrad.visibility = View.GONE
                binding.tvWordOrg.visibility = View.VISIBLE
                binding.tvWordTrad.visibility = View.VISIBLE

                binding.etWordOrg.setText(dataListW.wordOrg)
                binding.etWordTrad.setText(dataListW.wordTrad.replace("☼", ""))


            }



        }



    }



}