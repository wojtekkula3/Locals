package com.wojciechkula.locals.extension

import android.annotation.SuppressLint
import android.view.View
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

@SuppressLint("WrongConstant")
fun ViewBinding.showSnackbar(
    message: String,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_INDEFINITE
): Snackbar {
    return Snackbar.make(root, message, duration).apply {
        setAction("Ok") { dismiss() }
        show()
    }
}