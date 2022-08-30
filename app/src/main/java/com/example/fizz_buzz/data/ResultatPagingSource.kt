package com.example.fizz_buzz.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.fizz_buzz.localservice.IDonneesSaisieService
import com.example.fizz_buzz.model.DonneesSaisie
import kotlin.math.max

// The initial key used for loading.
// This is the resultat id of the first resultat that will be loaded
private const val STARTING_KEY = 1

/**
 * A [PagingSource] that loads resultats for paging. The [Int] is the paging key or query that is used to fetch more
 * data, and the [String] specifies that the [PagingSource] fetches an [String] [List].
 */
class ResultatPagingSource(
    private val donneSaisieService: IDonneesSaisieService,
    private val query: DonneesSaisie
) : PagingSource<Int, Resultat>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Resultat> {
        // If params.key is null, it is the first load, so we start loading with STARTING_KEY
        val position = params.key ?: STARTING_KEY
        val range = position.until(position + params.loadSize)

        return LoadResult.Page(
            data = range.map { number ->
                Resultat(
                    id = number,
                    text = donneSaisieService.getResultText(number, query)
                )
            },
            prevKey = if (position == STARTING_KEY) null else position - 1,
            nextKey = range.last + 1
        )
    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Resultat>): Int? {
        // In our case we grab the item closest to the anchor position
        // then return its id - (state.config.pageSize / 2) as a buffer
        val anchorPosition = state.anchorPosition ?: return null
        val resultat = state.closestItemToPosition(anchorPosition) ?: return null
        return ensureValidKey(key = resultat.id - (state.config.pageSize / 2))
    }

    /**
     * Makes sure the paging key is never less than [STARTING_KEY]
     */
    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
}
