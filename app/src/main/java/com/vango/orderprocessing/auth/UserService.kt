package com.vango.orderprocessing.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor() {
    var userId: String? = null
    var accountType: String? = null
}