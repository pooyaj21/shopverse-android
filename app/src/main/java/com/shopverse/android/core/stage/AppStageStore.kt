package com.shopverse.android.core.stage

import com.shopverse.core.preferences.SharedPref

interface AppStageStore {
    fun getCurrentStage(): AppStage
    fun stageCompleted(appStage: AppStage): AppStage
    fun moveToStage(appStage: AppStage)
    fun registerObserver(observer: Observer)
    fun unregisterObserver(observer: Observer)

    fun interface Observer {
        fun onAppStageChanged(newAppStage: AppStage)
    }
}

private const val KEY_APP_STAGE = "app_stage"

class AppStageStoreImpl(private val sharedPref: SharedPref) : AppStageStore {

    private val listeners = mutableSetOf<AppStageStore.Observer>()

    override fun getCurrentStage(): AppStage {
        val saved = sharedPref.read(key = KEY_APP_STAGE, defValue = null)
        return AppStage.toStage(saved) ?: AppStage.NEW.nextStage
    }

    override fun stageCompleted(appStage: AppStage): AppStage {
        sharedPref.write(KEY_APP_STAGE, appStage.nextStage.toString())
        listeners.forEach { it.onAppStageChanged(appStage.nextStage) }
        return appStage.nextStage
    }

    override fun moveToStage(appStage: AppStage) {
        listeners.forEach { it.onAppStageChanged(appStage) }
        sharedPref.write(KEY_APP_STAGE, appStage.toString())
    }

    override fun registerObserver(observer: AppStageStore.Observer) {
        listeners.add(observer)
    }

    override fun unregisterObserver(observer: AppStageStore.Observer) {
        listeners.remove(observer)
    }
}
