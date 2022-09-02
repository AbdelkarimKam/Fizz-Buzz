package com.example.fizz_buzz.localservice

import com.example.fizz_buzz.domain.model.DonneesSaisie

class DonneesSaisieService : IDonneesSaisieService {

    override fun getResultText(index: Int, donneesSaisie: DonneesSaisie): String {
        if (index estMultipleDe donneesSaisie.entier1 * donneesSaisie.entier2)
            return "${donneesSaisie.mot1}${donneesSaisie.mot2}"
        if (index estMultipleDe donneesSaisie.entier1)
            return donneesSaisie.mot1
        if (index estMultipleDe donneesSaisie.entier2)
            return donneesSaisie.mot2
        return index.toString()
    }

    private infix fun Int.estMultipleDe(numero: Int) = this % numero == 0
}
