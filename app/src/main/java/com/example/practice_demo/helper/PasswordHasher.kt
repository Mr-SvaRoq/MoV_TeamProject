package com.example.practice_demo.helper

import com.himanshurawat.hasher.HashType
import com.himanshurawat.hasher.Hasher


class PasswordHasher {
    companion object {
        private val hashType = HashType.SHA_256

        fun hash(str: String): String =
            Hasher.Companion.hash(str, hashType)

        fun compare(plainStr: String, hashedStr: String): Boolean =
            this.hash(plainStr) == hashedStr
    }
}