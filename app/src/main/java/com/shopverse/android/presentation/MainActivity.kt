package com.shopverse.android.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shopverse.android.core.Screen
import com.shopverse.android.core.extension.lockPortrait

class MainActivity : AppCompatActivity() {

    private var rootView: MainView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lockPortrait()
        setBasic()
        rootView = MainView(activity = this)
        setContentView(rootView)
        rootView?.setNavigation()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setBasic()
    }

    private fun setBasic() {
        Screen.updateSize(this, resources.configuration)
    }
}
