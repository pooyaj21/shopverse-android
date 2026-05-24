package com.shopverse.android.core.extension

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.notImplementedYet() {
    Toast.makeText(requireContext(), "Soon will be implemented", Toast.LENGTH_SHORT).show()
}

fun View.notImplementedYet() {
    Toast.makeText(context, "Soon will be implemented", Toast.LENGTH_SHORT).show()
}