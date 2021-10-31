package com.wojciechkula.locals.extension

import androidx.lifecycle.LiveData

@Suppress("UnsafeCallOnNullableType")
fun <T> LiveData<T>.newBuilder(newValue: T.() -> T) = newValue(this.value!!)