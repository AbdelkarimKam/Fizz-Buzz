package com.example.fizz_buzz.localservice

import com.example.fizz_buzz.model.DonneesSaisie

interface IDonneesSaisieService {
    fun getResultText(index: Int, donneesSaisie: DonneesSaisie): String
}