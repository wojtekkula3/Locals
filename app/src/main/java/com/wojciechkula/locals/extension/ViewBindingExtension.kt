package com.wojciechkula.locals.extension

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.wojciechkula.locals.R

@SuppressLint("WrongConstant")
fun ViewBinding.showSnackbarOk(
    message: String,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_INDEFINITE
): Snackbar {
    return Snackbar.make(root, message, duration).apply {
        setAction("Ok") { dismiss() }
        show()
    }
}

@SuppressLint("WrongConstant")
fun ViewBinding.showSnackbarInfo(
    message: String,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_LONG
): Snackbar {
    return Snackbar.make(root, message, duration).apply {
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setBackgroundTint(ContextCompat.getColor(context, R.color.gray_300))
        show()
    }
}

fun ViewBinding.showSnackbarError(
    message: String,
    @BaseTransientBottomBar.Duration duration: Int = Snackbar.LENGTH_INDEFINITE
): Snackbar {
    return Snackbar.make(root, message, duration).apply {
        setTextColor(ContextCompat.getColor(context, R.color.white))
        setBackgroundTint(ContextCompat.getColor(context, R.color.red_400))
        setAction("Ok") { dismiss() }
        show()
    }
}