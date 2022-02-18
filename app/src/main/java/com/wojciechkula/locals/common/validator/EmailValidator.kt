package com.wojciechkula.locals.common.validator

import android.util.Patterns
import javax.inject.Inject

class EmailValidator @Inject constructor() {

    fun validate(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}