package com.shopverse.android.presentation.screen.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shopverse.android.core.stage.AppStage
import com.shopverse.android.presentation.architecture.BaseFragmentVM
import com.shopverse.android.presentation.ui.Source
import com.shopverse.android.presentation.ui.navigateAndClearStack
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment : BaseFragmentVM<OnboardingView, OnboardingViewModel>() {

    override val currentSource: Source = Source.Onboarding

    override val viewModel: OnboardingViewModel by viewModel()

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): OnboardingView {
        return OnboardingView(
            context = requireContext(),
            onContinue = { viewModel.goNextStage() }
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
