package com.shopverse.android.presentation.screen.navigator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import com.shopverse.android.presentation.architecture.BaseFragmentVM
import com.shopverse.android.presentation.ui.Source
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavigatorFragment : BaseFragmentVM<NavigatorView, NavigatorViewModel>() {

    override val currentSource: Source get() = Source.Navigator

    override val viewModel: NavigatorViewModel by viewModel()

    private lateinit var onBackPressCallback: OnBackPressedCallback

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): NavigatorView {
        return NavigatorView(
            fragment = this,
            tabBarBackStack = viewModel.tabBarBackStack,
            onTabSelectedListener = { isDefaultTab, _ ->
                onBackPressCallback.isEnabled = isDefaultTab.not()
            }
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                rootView?.tabBarView?.back()
            }
        }
    }


    override fun onResume() {
        super.onResume()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW)
    }

    override fun onPause() {
        super.onPause()
        viewModel.tabBarBackStack = rootView?.getBackstack()
    }

    override fun onDestroyRootView() {
        rootView?.onDestroyView()
    }
}