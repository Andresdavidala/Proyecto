package com.example.proyecto

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.proyecto.databinding.ActivityMainBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var adRequest: AdRequest? = null

    companion object{
        var mInterstitialAd: InterstitialAd? = null
        var contAds = 0
        fun loadInterst(context: Context){
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(context, "ca-app-pub-3940256099942544/1033173712", adRequest,object : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    mInterstitialAd = interstitialAd
                }
            })
        }
        fun loadListener(context: Context){
            mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    mInterstitialAd = null
                    loadInterst(context)
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    // Called when ad fails to show.
                    mInterstitialAd = null
                }


            }
        }

        fun showInterst(context: Context, activity: Activity){
//            Interstitial↓
        if(contAds == 6){
//            loadInterst(this)
            mInterstitialAd?.show(activity)
            loadListener(context)
            contAds = 0
        }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //para que no se muevan los elementos al abrir el teclado
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        //↑

        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadAds()
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

        loadInterst(this)


    }






    private fun loadAds(){
        val adRequest = AdRequest.Builder().build()
        binding.adBanner.loadAd(adRequest)
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fmTextField.id, fragment)
            .commit()
    }

    override fun onBackPressed() {
        finishAffinity()
    }


}