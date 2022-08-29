package com.example.fizz_buzz.localservice

import com.example.fizz_buzz.data.Resultat
import com.example.fizz_buzz.model.DonneesSaisie

interface IDonneesSaisieService {
    fun getLotDeResltats(query: DonneesSaisie, position: Int, loadSize: Int): List<Resultat>
}