package com.example.fizz_buzz.domain.use_case

class ValidationMot {

    fun execute(mot: String?): ValidationResult {
        if (mot == null || mot.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Ce champ ne doit pas être vide!"
            )
        }
        return if (mot.filter { it in 'A'..'Z' || it in 'a'..'z' }.length != mot.length)
            ValidationResult(
                successful = false,
                errorMessage = "Ce champ doit être un mot!"
            )
        else ValidationResult(
            successful = true,
            data = mot
        )
    }
}