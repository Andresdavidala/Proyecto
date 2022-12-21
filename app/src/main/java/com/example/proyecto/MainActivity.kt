package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.proyecto.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //para que no se muevan los elementos al abrir el teclado
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //↑

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        loadAds()
        replaceFragment(SaveWords())

        binding.btnNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.newWord -> replaceFragment(SaveWords())
                R.id.memorias -> replaceFragment(cardMemor())
                R.id.evaWord -> replaceFragment(EvaWord())
                R.id.listWord -> replaceFragment(listWords())
                R.id.setting -> startActivity(Intent(this, SettingActivity::class.java))

                else->{

                }

            }

            true
        }
    }

    private fun loadAds(){
        val adRequest = AdRequest.Builder().build()
        binding.adBanner.loadAd(adRequest)
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fmTextField.id, fragment)
            .commit()
    }

    override fun onBackPressed() {
        finishAffinity()
    }

}