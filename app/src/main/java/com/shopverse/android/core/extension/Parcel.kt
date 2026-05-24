package com.shopverse.android.core.extension

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import kotlin.reflect.KClass


fun <T : Parcelable> Parcel.readParcelableCompat(kClass: KClass<T>): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        readParcelable(kClass.java.classLoader, kClass.java)
    } else {
        @Suppress("DEPRECATION")
        readParcelable(kClass.java.classLoader)
    }
