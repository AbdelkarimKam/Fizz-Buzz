package com.example.fizz_buzz.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.fizz_buzz.model.DonneesSaisie
import com.example.fizz_buzz.ui.activity.UserIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.max

// The initial key used for loading.
// This is the resultat id of the first resultat that will be loaded
private const val STARTING_KEY = 1

fun getList(index: Int, donneesSaisie: DonneesSaisie): kotlin.String {
    if (index estMultipleDe donneesSaisie.entier1 * donneesSaisie.entier2)
        return "${donneesSaisie.mot1}${donneesSaisie.mot2}"
    if (index estMultipleDe donneesSaisie.entier1)
        return donneesSaisie.mot1
    if (index estMultipleDe donneesSaisie.entier2)
        return donneesSaisie.mot2
    return index.toString()
}

private infix fun Int.estMultipleDe(numero: Int) = this % numero == 0
/**
 * A [PagingSource] that loads resultats for paging. The [Int] is the paging key or query that is used to fetch more
 * data, and the [String] specifies that the [PagingSource] fetches an [String] [List].
 */
class ResultatPagingSource : PagingSource<Int, Resultat>() {
    private var donneesSaisie =DonneesSaisie(1, 1, "", "")

     fun updateData( donneesSaisie: DonneesSaisie){
        this.donneesSaisie=donneesSaisie
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Resultat> {
        try {
            // If params.key is null, it is the first load, so we start loading with STARTING_KEY
            val startKey = params.key ?: STARTING_KEY

            // We fetch as many resultss as hinted to by params.loadSize
            val range = startKey.until(startKey + params.loadSize)
         //   val donneesSaisie = DonneesSaisie(3, 5, "fizz", "buzz")
         //   var donneesSaisie = DonneesSaisie(0, 0, "", "")

         //   val x = DonneesSaisie(3, 5, "fizz", "buzz")


            return LoadResult.Page(
                data = range.map { number ->
                    Resultat(
                        id = number,
                        text = getList(number, donneesSaisie)
                    )
                },
                prevKey = when (startKey) {
                    STARTING_KEY -> null
                    else -> when (val prevKey =
                        ensureValidKey(key = range.first - params.loadSize)) {
                        // We're at the start, there's nothing more to load
                        STARTING_KEY -> null
                        else -> prevKey
                    }
                },
                nextKey = range.last + 1
            )
        }catch (e:Exception){
            return LoadResult.Error(e)
        }
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
