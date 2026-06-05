package com.shopverse.android.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.shopverse.android.core.Screen
import com.shopverse.android.core.cart.CartManager
import com.shopverse.android.core.extension.lockPortrait
import com.shopverse.android.presentation.architecture.NavControllerFiredCallback
import com.shopverse.android.presentation.feature.deepLink.DeepLinkLauncher
import com.shopverse.android.presentation.screen.auth.AuthBottomSheetFragment
import com.shopverse.android.presentation.ui.Source
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()
    private val cartManager: CartManager by inject()
    private var rootView: MainView? = null
    private var loginDialog: AuthBottomSheetFragment? = null
    private var currentSource: Source = Source.AppLaunch

    val deepLinkLauncher: DeepLinkLauncher by inject()

    private val fragmentLifecycleCallbacks: NavControllerFiredCallback by lazy {
        object : NavControllerFiredCallback() {
            override fun onNavHostFragmentCreated(navHostFragment: NavHostFragment) {
                deepLinkLauncher.setNavHostFragment(navHostFragment)
            }

            override fun setAppState(isFullyOpened: Boolean) {
                deepLinkLauncher.setAppFullyOpened(isFullyOpened)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private val deeplinkDialogListener: (DeepLinkLauncher.DialogConfig) -> Unit = { dialogConfig ->
        when (dialogConfig) {
            is DeepLinkLauncher.DialogConfig.Login -> {
                if (loginDialog == null) {
                    loginDialog = AuthBottomSheetFragment.newInstance(
                        onLoginFailListener = {
                        },
                        onLoginSuccessListener = {
                            deepLinkLauncher.enqueue(
                                dialogConfig.uri,
                                Source.DeepLink(dialogConfig.uri.toString())
                            )
                        },
                    ).also {
                        it.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }

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

    @SuppressLint("MissingSuperCall")
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { uri -> deepLinkLauncher.enqueue(uri, Source.DeepLink(uri.toString())) }
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
        intent?.data?.let { uri ->
            deepLinkLauncher.enqueue(uri, Source.DeepLink(uri.toString()))
            currentSource = Source.DeepLink(uri.toString())
        }
        deepLinkLauncher.setOpenDialogListener(deeplinkDialogListener)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, true)
        setBasic()
        rootView?.setNavigation()
    }

    private fun setBasic() {
        Screen.updateSize(this, resources.configuration)
    }
}
