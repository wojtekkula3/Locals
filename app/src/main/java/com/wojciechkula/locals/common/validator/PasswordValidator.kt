package com.wojciechkula.locals.common.validator

import javax.inject.Inject

class PasswordValidator @Inject constructor() {

    fun validate(value: String): Boolean {
        return PASSWORD_REGEX.toRegex().matches(value)
    }

    companion object {
        private const val PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*[.\$@\$!%*?&#])(?=.*[0-9])(?!.*[<>])[A-Za-z\\\\\$@\$!%*?&#].{8,}\$"
    }
}