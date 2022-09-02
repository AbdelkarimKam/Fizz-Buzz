package com.example.fizz_buzz.localservice

import com.example.fizz_buzz.domain.model.DonneesSaisie

interface IDonneesSaisieService {
    fun getResultText(index: Int, donneesSaisie: DonneesSaisie): String
}