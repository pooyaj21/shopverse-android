package com.shopverse.android.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.shopverse.android.R
import com.shopverse.android.core.stage.AppStage
import com.shopverse.android.presentation.screen.navigator.NavigatorScreenArgs
import com.shopverse.android.presentation.screen.productDetail.ProductDetailScreenArgs
import com.shopverse.core.model.Product


private fun Fragment.navigate(
    @IdRes fragmentId: Int, args: ScreenArgument<*>, clearBackStack: Boolean = false
) {
    val navOptions = navOptions {
        if (clearBackStack) popUpTo(R.id.navigation_main) {}
    }
    findNavController().navigate(fragmentId, args.toBundle(), navOptions)
}

fun Fragment.navigateUp() = findNavController().navigateUp()

fun View.navigateUp() = findNavController().navigateUp()

fun View.navigate(@IdRes fragmentId: Int, args: Bundle) =
    findNavController().navigate(fragmentId, args)

fun Fragment.navigateUpTo(@IdRes fragmentId: Int, inclusive: Boolean = false) =
    findNavController().popBackStack(fragmentId, inclusive)

fun Fragment.navigateAndClearStack(appStage: AppStage, source: Source) {
    val destination: Pair<Int, Bundle> = when (appStage) {
        AppStage.NEW -> R.id.splashFragment to NoRequirementArgs(source).toBundle()
        AppStage.ON_BOARDING -> R.id.onboardingFragment to NoRequirementArgs(source).toBundle()
        AppStage.ESTABLISHED -> R.id.navigatorFragment to NavigatorScreenArgs(
            source,
            NavigatorScreenArgs.Requirements()
        ).toBundle()
    }
    val navOptions = navOptions { popUpTo(R.id.navigation_main) {} }
    findNavController().navigate(destination.first, destination.second, navOptions)
}

//Screens

fun Fragment.navigateToOnboarding(source: Source) {
    navigate(R.id.onboardingFragment, NoRequirementArgs(source))
}

fun Fragment.navigateToNavigator(args: NavigatorScreenArgs) {
    navigate(R.id.navigatorFragment, args)
}

fun Fragment.navigateToOrders(source: Source) {
    navigate(R.id.ordersFragment, NoRequirementArgs(source))
}

fun Fragment.navigateToAccount(source: Source) {
    navigate(R.id.accountFragment, NoRequirementArgs(source))
}

fun Fragment.navigateToProductDetail(source: Source, product: Product) {
    navigate(
        R.id.productDetailFragment,
        ProductDetailScreenArgs(
            source = source,
            requirements = ProductDetailScreenArgs.Requirements(product = product),
        )
    )
}