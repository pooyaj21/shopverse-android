package com.shopverse.android.presentation.screen.auth

sealed class AuthBottomSheetEffect {
    data object AuthCompleted : AuthBottomSheetEffect()
}
