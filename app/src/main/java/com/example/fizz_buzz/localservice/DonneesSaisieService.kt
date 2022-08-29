package com.example.fizz_buzz.localservice

import com.example.fizz_buzz.data.Resultat
import com.example.fizz_buzz.model.DonneesSaisie

class DonneesSaisieService : IDonneesSaisieService {

    override fun getLotDeResltats(query: DonneesSaisie, position: Int, loadSize: Int): List<Resultat> {
        val list = ArrayList<Resultat>()
        for (index in position until loadSize) {
            with(query) {
                if (index estMultipleDe entier1 * entier2)
                    list.addResultat(index, "${mot1}${mot2}")
                else {
                    if (index estMultipleDe entier1)
                        list.addResultat(index, mot1)
                    else {
                        if (index estMultipleDe entier2)
                            list.addResultat(index, mot2)
                        else
                            list.addResultat(index, index.toString())
                    }
                }
            }
        }
        return list
    }

    private infix fun Int.estMultipleDe(numero: Int) = this % numero == 0

    private fun ArrayList<Resultat>.addResultat(id: Int, text: String) = this.add(
        Resultat(id, text)
    )
}