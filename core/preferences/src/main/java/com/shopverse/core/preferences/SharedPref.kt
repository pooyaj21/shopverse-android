package com.shopverse.core.preferences

import android.content.SharedPreferences
import android.database.Observable

class SharedPref(private val sharedPreferences: SharedPreferences) :
    Observable<SharedPref.ValueObserver>() {

    private val onSharedPreferenceChangeListener by lazy {
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            mObservers.forEach { observer -> if (observer.key == key) observer.onValueChanged() }
        }
    }

    @Suppress("unused")
    fun read(key: String, defValue: String?): String? =
        sharedPreferences.getString(key, defValue)

    @Suppress("unused")
    fun write(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    @Suppress("unused")
    fun read(key: String, defValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defValue)

    @Suppress("unused")
    fun write(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    @Suppress("unused")
    fun read(key: String, defValue: Int): Int =
        sharedPreferences.getInt(key, defValue)

    @Suppress("unused")
    fun write(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    @Suppress("unused")
    fun read(key: String, defValue: Long): Long =
        sharedPreferences.getLong(key, defValue)

    @Suppress("unused")
    fun write(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    @Suppress("unused")
    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun registerObserver(observer: ValueObserver?) {
        if (mObservers.size == 0) {
            sharedPreferences.registerOnSharedPreferenceChangeListener(
                onSharedPreferenceChangeListener
            )
        }
        super.registerObserver(observer)
    }

    override fun unregisterObserver(observer: ValueObserver?) {
        super.unregisterObserver(observer)
        if (mObservers.size == 0) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(
                onSharedPreferenceChangeListener
            )
        }
    }

    abstract class ValueObserver(val key: String) {
        abstract fun onValueChanged()
    }
}
