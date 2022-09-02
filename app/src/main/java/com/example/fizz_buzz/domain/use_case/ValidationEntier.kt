package com.example.fizz_buzz.domain.use_case

class ValidationEntier {

    fun execute(entier: String?): ValidationResult {
        if (entier == null || entier.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Ce champ ne doit pas être vide!"
            )
        }
        return when (entier.toIntOrNull()) {
            null -> ValidationResult(
                successful = false,
                errorMessage = "Ce champ doit être un entier!"
            )
            else -> ValidationResult(
                successful = true,
                data = entier
            )
        }
    }
}