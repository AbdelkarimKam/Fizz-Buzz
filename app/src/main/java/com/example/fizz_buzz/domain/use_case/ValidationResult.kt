package com.example.fizz_buzz.domain.use_case

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null,
    val data: String? = null
)