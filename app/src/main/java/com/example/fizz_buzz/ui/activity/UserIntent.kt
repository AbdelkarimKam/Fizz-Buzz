package com.example.fizz_buzz.ui.activity

import com.example.fizz_buzz.model.DonneesSaisie

sealed class UserIntent {
    data class AfficheResultats(val donneesSaisie: DonneesSaisie):UserIntent()
}
