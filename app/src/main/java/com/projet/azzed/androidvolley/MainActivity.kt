package com.projet.azzed.androidvolley

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var nav_view: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nav_view = findViewById(R.id.my_nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        nav_view.setupWithNavController(navController)
    }
}
