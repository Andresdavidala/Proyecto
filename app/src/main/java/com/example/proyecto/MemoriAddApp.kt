package com.example.proyecto

import android.app.Application
import com.google.android.gms.ads.MobileAds

class MemoriAddApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}