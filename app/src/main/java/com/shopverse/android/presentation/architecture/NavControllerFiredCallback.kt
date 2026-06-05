package com.shopverse.android.presentation.architecture

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shopverse.android.presentation.screen.onboarding.OnboardingFragment
import com.shopverse.android.presentation.screen.splash.SplashFragment

abstract class NavControllerFiredCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentResumed(fm: FragmentManager, fragment: Fragment) {
        super.onFragmentResumed(fm, fragment)
        val isAppPreLoading = fragment is NavHostFragment ||
                fragment is SplashFragment ||
                fragment is OnboardingFragment
        if (fragment is NavHostFragment) onNavHostFragmentCreated(fragment)
        if (isAppPreLoading.not()) setAppState(true)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        super.onFragmentPaused(fm, f)
        if (f is BottomSheetDialogFragment) return
        setAppState(false)
    }

    abstract fun onNavHostFragmentCreated(navHostFragment: NavHostFragment)

    abstract fun setAppState(isFullyOpened: Boolean)
}