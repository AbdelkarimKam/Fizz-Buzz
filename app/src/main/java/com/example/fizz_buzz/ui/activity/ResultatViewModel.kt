package com.example.fizz_buzz.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fizz_buzz.data.ResultatRepository
import com.example.fizz_buzz.data.Resultat
import com.example.fizz_buzz.model.DonneesSaisie
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

private const val ITEMS_PER_PAGE = 50

class ResultatViewModel : ViewModel() {

    val intentChannel = Channel<UserIntent>(Channel.UNLIMITED)

    private val _donneesSaisie : MutableLiveData<DonneesSaisie> by lazy {
        MutableLiveData()
    }

    val donneesSaisie : LiveData<DonneesSaisie>
        get() = _donneesSaisie


    init {
        processIntent()
    }

    val repository = ResultatRepository()
    /**
     * Stream of immutable states representative of the UI.
     */
    val items: Flow<PagingData<Resultat>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { repository.resultatPagingSource() }
    )
        .flow
        // cachedIn allows paging to remain active in the viewModel scope, so even if the UI
        // showing the paged data goes through lifecycle changes, pagination remains cached and
        // the UI does not have to start paging from the beginning when it resumes.
        .cachedIn(viewModelScope)





    //processes
    private fun processIntent() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                when (it) {
                    is UserIntent.AfficheResultats -> updateDonneesSaisie(it.donneesSaisie)
                }
            }
        }
    }

    //reduce Result
    // save data in storage to consume by
    private fun updateDonneesSaisie(donneesSaisie: DonneesSaisie) {
        _donneesSaisie.value = donneesSaisie

        repository.resultatPagingSource().updateData(donneesSaisie)
    }
}
