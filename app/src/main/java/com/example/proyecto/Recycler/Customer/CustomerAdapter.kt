package com.example.proyecto.Recycler.Customer

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.MainActivity
import com.example.proyecto.R
import com.example.proyecto.Recycler.DataWordsBase
import com.example.proyecto.Recycler.dataWordProvider
import com.example.proyecto.databinding.WordslistrecyclerviewBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
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

    inner class vhDataList(view: View): RecyclerView.ViewHolder(view) {

        val binding = WordslistrecyclerviewBinding.bind(view)
        var miInterstitialAd: InterstitialAd? = null
        //vamos al shared


        @SuppressLint("ClickableViewAccessibility")
        fun renderData(
            dataListW: DataWordsBase,
            onClickDelete: (Int) -> Unit,
            fileContext: Context
        ) {

            //ads

            initAds()
            //↑
//sharedCountAds
            val sharedPreferences = fileContext.getSharedPreferences("preferencesRC", Context.MODE_PRIVATE)
            // Guarda la variable en SharedPreferences
            MainActivity.contardorRecyclers = sharedPreferences!!.getInt("recyclersCont", 0)
            //↑
//                initAds()

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

                    MainActivity.contardorRecyclers+=1
                    if(MainActivity.contardorRecyclers == 7){
                        callAd()
                        MainActivity.contardorRecyclers = 0
                    }

                    onClickDelete(adapterPosition)
                    dialog.dismiss()
                    sharedPreferences.edit().putInt("recyclersCont",
                        MainActivity.contardorRecyclers
                    ).apply()
                    Log.d("datosReycler", MainActivity.contardorRecyclers.toString())
                }

            }

            binding.btnEdit.setOnClickListener {
                binding.btnEdit.isSelected = !binding.btnEdit.isSelected
            }

            binding.carViewWO.setOnClickListener {
                Log.d("datosWT", dataListW.wordOrg.replace("☼○", ""))
            }
            binding.cardViewWT.setOnClickListener {
                Log.d("datosWT", dataListW.wordTrad.replace("☼○", ""))
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
                        outputWriter.write("${dataWordProvider.dataWords[i].wordOrg.trim()}☼○ ")
                        outputWriter.write("${dataWordProvider.dataWords[i].wordTrad.trim()}☼○ ")
                    }
                    Log.d("datosE", "FUNC")
                    Log.d("datosRV", dataWordProvider.dataWords.toString())

                    outputWriter.flush()
                    outputWriter.close()

                    notifyItemChanged(adapterPosition)

                    //ads↓
                    MainActivity.contardorRecyclers+=1
                    if(MainActivity.contardorRecyclers == 7){
                        callAd()
                        MainActivity.contardorRecyclers = 0

                    }
                    sharedPreferences.edit().putInt("recyclersCont",
                        MainActivity.contardorRecyclers
                    ).apply()
                    Log.d("datosReycler", MainActivity.contardorRecyclers.toString())

                } catch (_: java.lang.Exception) {
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



        private fun initAds(){
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(fileContext,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    miInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    miInterstitialAd = interstitialAd
                }
            })
        }
        private fun callAd(){
            showAds()
            MainActivity.nombreVariable = 0
            initAds()
        }

        private fun showAds(){
            miInterstitialAd?.show(Activity().parent)
        }

    }
}