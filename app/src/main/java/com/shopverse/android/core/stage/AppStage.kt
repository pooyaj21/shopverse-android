package com.shopverse.android.core.stage

enum class AppStage(private val stage: String) {
    NEW("new"),
    ON_BOARDING("on_boarding"),
    ESTABLISHED("established");

    override fun toString(): String = stage

    companion object {
        fun toStage(stage: String?): AppStage? =
            entries.firstOrNull { it.stage == stage }
    }
}

val AppStage.nextStage: AppStage
    get() = when (this) {
        AppStage.NEW -> AppStage.ON_BOARDING
        AppStage.ON_BOARDING -> AppStage.ESTABLISHED
        AppStage.ESTABLISHED -> AppStage.ESTABLISHED
    }
