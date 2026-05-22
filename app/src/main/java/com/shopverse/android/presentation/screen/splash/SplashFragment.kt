package com.shopverse.android.presentation.screen.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shopverse.android.core.stage.AppStage
import com.shopverse.android.presentation.architecture.BaseFragmentVM
import com.shopverse.android.presentation.splash.SplashView
import com.shopverse.android.presentation.ui.Source
import com.shopverse.android.presentation.ui.navigateAndClearStack
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragmentVM<SplashView, SplashViewModel>() {

    override val currentSource: Source = Source.Splash
    override val viewModel: SplashViewModel by viewModel()

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): SplashView {
        return SplashView(
            context = requireContext(),
            onSplashEnd = { viewModel.splashEnded() }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullScreen()
        onEffect<AppStage> { appStage ->
            navigateAndClearStack(appStage, currentSource)
        }
    }
}
