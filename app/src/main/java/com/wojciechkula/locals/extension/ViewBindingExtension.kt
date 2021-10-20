package com.wojciechkula.locals.extension

import android.annotation.SuppressLint
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

@SuppressLint("WrongConstant")
fun ViewBinding.showSnackbar(
    message: String,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_SHORT
): Snackbar {
    return Snackbar.make(root, message, duration).apply {
        show()
    }
}