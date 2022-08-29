package com.example.fizz_buzz.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fizz_buzz.data.Resultat
import com.example.fizz_buzz.data.ResultatRepository
import com.example.fizz_buzz.localservice.DonneesSaisieService
import com.example.fizz_buzz.model.DonneesSaisie
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ResultatViewModel : ViewModel() {
    private val repository = ResultatRepository(DonneesSaisieService())
    val intentChannel = Channel<UserIntent>(Channel.UNLIMITED)

    val pagingDataFlow: Flow<PagingData<Resultat>>

    /**
     * Processor of side effects from the UI which in turn feedback into [state]
     */
    //val accept: (UserIntent) -> Unit
    private val actionStateFlow = MutableSharedFlow<UserIntent>()

    init {
        processIntent()
        pagingDataFlow = actionStateFlow
            .filterIsInstance<UserIntent.AfficheResultats>()
            .onStart {
                emit(
                    UserIntent.AfficheResultats(
                        donneesSaisie = DonneesSaisie(
                            1,
                            1,
                            "",
                            ""
                        )
                    )
                )
            }
            .flatMapLatest { searchRepo(queryDonneesSaisie = it.donneesSaisie) }
            // cachedIn allows paging to remain active in the viewModel scope, so even if the UI
            // showing the paged data goes through lifecycle changes, pagination remains cached and
            // the UI does not have to start paging from the beginning when it resumes.
            .cachedIn(viewModelScope)
    }

    private fun searchRepo(queryDonneesSaisie: DonneesSaisie): Flow<PagingData<Resultat>> =
        repository.resultatPagingSource(queryDonneesSaisie)

    //processes
    private fun processIntent() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                when (it) {
                    is UserIntent.AfficheResultats -> {
                        actionStateFlow.emit(it)
                    }
                }
            }
        }
    }

}
