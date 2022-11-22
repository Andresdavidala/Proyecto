package com.example.proyecto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.proyecto.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        //para que no se muevan los elementos al abrir el teclado
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //↑

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.vpSlide.adapter = AdapterVP2(this)

        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.vpSlide,
        TabLayoutMediator.TabConfigurationStrategy{
            tab, position ->
                when(position){
                    0 -> {
                        tab.setIcon(R.drawable.save)
                    }
                    1 -> {
                        tab.setIcon(R.drawable.quiz)
                    }
                    2 ->{
                        tab.setIcon(R.drawable.ic_baseline_format_list_bulleted_24)
                    }
                    3 ->{
                        tab.setIcon(R.drawable.ic_baseline_settings_24)
                    }
                }
        })
        tabLayoutMediator.attach()

    }

}