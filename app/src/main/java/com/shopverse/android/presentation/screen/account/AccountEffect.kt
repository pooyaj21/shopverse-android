package com.shopverse.android.presentation.screen.account

sealed class AccountEffect {
    data object AccountDeleted : AccountEffect()
}