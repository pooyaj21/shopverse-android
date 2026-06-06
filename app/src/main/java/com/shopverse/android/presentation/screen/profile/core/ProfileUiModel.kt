package com.shopverse.android.presentation.screen.profile.core

import com.shopverse.android.core.extension.label
import com.shopverse.core.model.ThemeMode

data class ProfileUiModel(val items: List<Item>) {

    sealed class Item {

        data class Separator(val isLast: Boolean = false) : Item()

        data class Title(val title: String) : Item()

        sealed class Navigatable(val title: String) : Item() {
            data object Profile : Navigatable("Profile")
            data object Orders : Navigatable("Orders")
        }

        sealed class Simple(val title: String) : Item() {
            data object Login : Simple("Log in / Create account")
            data object Logout : Simple("Log out")
        }

        sealed class Editable(val title: String) : Item() {
            abstract val value: String

            data class Theme(val mode: ThemeMode) : Editable("Theme") {
                override val value: String = mode.label
            }
        }

        sealed class Info(val title: String, val value: String) : Item() {
            class AppVersion(value: String) : Info("App version", value)
            class AppBuildNumber(value: String) : Info("Build number", value)
        }
    }

    companion object {
        fun create(
            isLoggedIn: Boolean,
            themeMode: ThemeMode,
            appVersion: String,
            appBuildNumber: String,
        ): ProfileUiModel {
            val items = buildList {
                add(Item.Title("Account"))
                if (isLoggedIn) {
                    add(Item.Navigatable.Profile)
                    add(Item.Separator())
                    add(Item.Navigatable.Orders)
                } else {
                    add(Item.Simple.Login)
                }

                add(Item.Title("App"))
                add(Item.Editable.Theme(themeMode))
                add(Item.Separator())
                add(Item.Info.AppVersion(appVersion))
                add(Item.Separator())
                add(Item.Info.AppBuildNumber(appBuildNumber))

                if (isLoggedIn) {
                    add(Item.Title(""))
                    add(Item.Simple.Logout)
                    add(Item.Separator(isLast = true))
                } else {
                    add(Item.Separator(isLast = true))
                }
            }
            return ProfileUiModel(items)
        }
    }
}
