package com.example.proyecto

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterVP2 (fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 4


    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {SaveWords()}
            1 -> {EvaWord()}
            2 ->{listWords()}
            3 ->{configuration()}

            else ->{throw Resources.NotFoundException("No encontrado")}
        }
    }
}