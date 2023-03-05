package com.example.authorization.constants

object SecurityConstants {
    const val JWT_KEY = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4"
    const val JWT_HEADER = "Authorization"
    val JWT_EXPIRE_TIME = System.currentTimeMillis() + 10800000
}
