package com.example.fizz_buzz.ui.viewmodel

import com.example.fizz_buzz.domain.model.DonneesSaisie

sealed class UserIntent {
    data class AfficheResultats(val donneesSaisie: DonneesSaisie): UserIntent()
    data class DonneesInput(val entier1: String?, val entier2: String?, val mot1: String?, val mot2: String?): UserIntent()
}
