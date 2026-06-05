package com.shopverse.android.presentation.feature.deepLink

import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.shopverse.android.R
import com.shopverse.android.core.extension.TAG
import com.shopverse.android.core.stage.AppStage
import com.shopverse.android.core.stage.AppStageStore
import com.shopverse.android.presentation.screen.navigator.NavigatorFragment
import com.shopverse.android.presentation.screen.navigator.NavigatorScreenArgs
import com.shopverse.android.presentation.screen.navigator.NavigatorView
import com.shopverse.android.presentation.screen.orderDetail.OrderDetailScreenArgs
import com.shopverse.android.presentation.screen.productDetail.ProductDetailScreenArgs
import com.shopverse.android.presentation.ui.NoRequirementArgs
import com.shopverse.android.presentation.ui.ScreenArgument
import com.shopverse.android.presentation.ui.Source
import com.shopverse.android.presentation.ui.toBundle
import com.shopverse.core.domain.auth.GetSavedProfileUseCase
import kotlinx.parcelize.Parcelize
import java.lang.ref.WeakReference
import java.util.LinkedList
import java.util.Queue

class DeepLinkLauncher(
    private val appStageStore: AppStageStore,
    private val getSavedProfileUseCase: GetSavedProfileUseCase,
) {

    private var queue: Queue<Item> = LinkedList()
    private var navHostFragment: WeakReference<NavHostFragment>? = null
    private var openDialogListener: WeakReference<(DialogConfig) -> Unit>? = null
    private var isAppFullyOpened = false

    init {
        appStageStore.registerObserver { stage ->
            if (stage == AppStage.ESTABLISHED) {
                dequeueIfPossible()
            }
        }
    }

    fun setNavHostFragment(navHost: NavHostFragment?) {
        this.navHostFragment = if (navHost == null) null else WeakReference(navHost)
        dequeueIfPossible()
    }

    fun setAppFullyOpened(isFullyOpened: Boolean) {
        isAppFullyOpened = isFullyOpened
        if (isFullyOpened) dequeueIfPossible()
    }

    fun setOpenDialogListener(openDialogListener: (DialogConfig) -> Unit) {
        this.openDialogListener = WeakReference(openDialogListener)
        dequeueIfPossible()
    }

    fun enqueue(uri: Uri, source: Source) {
        Log.d(TAG, "Deeplink enqueued : $uri")
        queue.add(Item(uri, source))
        dequeueIfPossible()
    }

    private fun dequeueIfPossible() {
        val nc = navHostFragment?.get()?.navController ?: return
        if (appStageStore.getCurrentStage() != AppStage.ESTABLISHED) return
        if (isAppFullyOpened.not()) return
        queue.poll()?.let { deeplink ->
            open(nc, deeplink.uri, deeplink.source)
            if (queue.isNotEmpty()) dequeueIfPossible()
        }
    }

    private fun open(navController: NavController, uri: Uri, source: Source) {
        Log.d(TAG, "Deeplink open : $uri")
        val action = uri.toAction()
        val navArg: NavArg? = when (action) {
            Action.Home -> navigateToTab(source, NavigatorView.TAB_HOME)
            Action.Cart -> navigateToTab(source, NavigatorView.TAB_CART)
            Action.Profile -> navigateToTab(source, NavigatorView.TAB_PROFILE)
            is Action.ProductDetail -> {
                NavArg(
                    R.id.productDetailFragment,
                    ProductDetailScreenArgs(source, ProductDetailScreenArgs.Requirements(action.id))
                )
            }

            Action.Account -> {
                NavArg(R.id.accountFragment, NoRequirementArgs(source))
            }

            Action.Orders -> {
                NavArg(R.id.ordersFragment, NoRequirementArgs(source))
            }

            is Action.OrderDetail -> {
                NavArg(
                    R.id.orderDetailFragment,
                    OrderDetailScreenArgs(source, OrderDetailScreenArgs.Requirements(action.id))
                )
            }

            null -> null
        }
        if (navArg == null) {
            Log.e(TAG, "DeepLink not defined : $uri")
            return
        }
        if (action?.isLoginNeeded == true && getSavedProfileUseCase() == null) {
            openDialogListener?.get()?.invoke(DialogConfig.Login(uri))
            return
        }
        navController.navigate(resId = navArg.fragmentId, args = navArg.screenArgument.toBundle())
    }

    private fun navigateToTab(
        source: Source,
        tab: String,
    ): NavArg? {

        fun buildNavArg() = NavArg(
            fragmentId = R.id.navigatorFragment,
            screenArgument = NavigatorScreenArgs(
                source = source,
                requirements = NavigatorScreenArgs.Requirements(
                    selectTabTag = tab,
                )
            )
        )

        return try {
            val navigatorFragment = navHostFragment?.get()
                ?.childFragmentManager
                ?.fragments
                ?.firstOrNull() as? NavigatorFragment

            if (navigatorFragment != null && navigatorFragment.isVisible) {
                navigatorFragment.getRootView()?.tabBarView?.select(tab)
                null
            } else {
                buildNavArg()
            }
        } catch (_: Throwable) {
            buildNavArg()
        }
    }

    private sealed class Action(val isLoginNeeded: Boolean = false) {
        object Home : Action()
        object Cart : Action()
        object Profile : Action()
        data class ProductDetail(val id: String) : Action()
        object Account : Action(true)
        object Orders : Action(true)
        data class OrderDetail(val id: String) : Action(true)
    }


    private fun Uri.toAction(): Action? {
        val path = pathSegments.joinToString("/")
        return when {
            host == "home" -> {
                Action.Home
            }

            host == "cart" -> {
                Action.Cart
            }

            host == "profile" -> {
                Action.Profile
            }

            host == "product" -> {
                this.pathSegments.getOrNull(0)?.let { id ->
                    Action.ProductDetail(id)
                }
            }

            host == "account" -> {
                Action.Account
            }

            host == "order" -> {
                val id = this.pathSegments.getOrNull(0)
                if (id == null) Action.Orders
                else Action.OrderDetail(id)
            }

            else -> null
        }
    }

    sealed class DialogConfig : Parcelable {
        @Parcelize
        data class Login(val uri: Uri) : DialogConfig()
    }

    private data class NavArg(
        @param:IdRes val fragmentId: Int, val screenArgument: ScreenArgument<*>
    )

    data class Item(val uri: Uri, val source: Source)
}