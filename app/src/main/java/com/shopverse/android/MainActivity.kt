package com.shopverse.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var rootView: MainView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootView = MainView(activity = this)
        setContentView(rootView)
    }
}
