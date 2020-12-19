package com.richa.applicationproject.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.richa.applicationproject.R
import com.richa.applicationproject.fragments.HomeFragment
import com.richa.applicationproject.fragments.NotificationFragment
import com.richa.applicationproject.fragments.PostQueryFragment
import com.richa.applicationproject.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {


    private lateinit var nvBar: BottomNavigationView
    private val fm: FragmentManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        nvBar = findViewById(R.id.bottomNavigation)

        openHome()
        nvBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bttmHome -> {
                    openHome()
                }
                R.id.bttmProfile -> {
                    fm.beginTransaction()
                        .replace(R.id.llContent, ProfileFragment()).commit()

                }
                R.id.bttmNotification -> {
                    fm.beginTransaction()
                        .replace(R.id.llContent, NotificationFragment()).commit()

                }
                R.id.bttmPost -> {
                    fm.beginTransaction()
                        .replace(R.id.llContent, PostQueryFragment()).commit()

                }

            }
            true
        }
    }

    private fun openHome() {
        fm.beginTransaction()
            .replace(R.id.llContent, HomeFragment()).commit()
    }

}