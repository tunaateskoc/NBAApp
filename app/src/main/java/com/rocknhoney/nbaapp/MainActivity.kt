package com.rocknhoney.nbaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rocknhoney.nbaapp.ui.NbaFragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: NbaFragmentFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.fragmentFactory = fragmentFactory
    }
}