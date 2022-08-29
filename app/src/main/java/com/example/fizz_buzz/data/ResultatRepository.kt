package com.example.fizz_buzz.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.fizz_buzz.localservice.IDonneesSaisieService
import com.example.fizz_buzz.model.DonneesSaisie
import kotlinx.coroutines.flow.Flow


class ResultatRepository(private val service: IDonneesSaisieService) {
    fun resultatPagingSource(query: DonneesSaisie): Flow<PagingData<Resultat>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ResultatPagingSource(service, query) }
        ).flow
    }

    companion object {
        const val ITEMS_PER_PAGE = 100
    }
}