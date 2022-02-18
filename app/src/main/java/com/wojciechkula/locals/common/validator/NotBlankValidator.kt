package com.wojciechkula.locals.common.validator

import javax.inject.Inject

class NotBlankValidator @Inject constructor() {

    fun validate(value: String): Boolean {
        return value.isNotBlank()
    }
}