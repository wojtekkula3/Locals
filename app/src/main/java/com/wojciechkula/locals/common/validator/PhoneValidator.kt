package com.wojciechkula.locals.common.validator

import javax.inject.Inject

class PhoneValidator @Inject constructor() {

    fun validate(value: String): Boolean {
        return if (value.isBlank()) {
            true
        } else PHONE_REGEX.toRegex().matches(value)
    }

    companion object {
        private const val PHONE_REGEX = "^[0-9]{9,13}\$"
    }
}