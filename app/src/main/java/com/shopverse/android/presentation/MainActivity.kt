package com.shopverse.android.presentation

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewModelScope
import com.shopverse.android.core.Screen
import com.shopverse.android.core.cart.CartManager
import com.shopverse.android.core.extension.lockPortrait
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()
    private val cartManager: CartManager by inject()
    private var rootView: MainView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lockPortrait()
        setBasic()
        rootView = MainView(activity = this)
        setContentView(rootView)
        startApp()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setBasic()
    }

    @Suppress("DEPRECATION")
    fun startApp() {
        viewModel.viewModelScope.launch {
            cartManager.init()
        }
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        WindowCompat.setDecorFitsSystemWindows(window, true)
        // https://stackoverflow.com/questions/40398528/android-webview-language-changes-abruptly-on-android-7-0-and-above
        // https://issuetracker.google.com/issues/37113860?pli=1
        // https://issuetracker.google.com/issues/109833940
        WebView(this).destroy()
        setBasic()
        rootView?.setNavigation()
    }

    private fun setBasic() {
        Screen.updateSize(this, resources.configuration)
    }
}
