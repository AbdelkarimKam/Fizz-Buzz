package com.example.fizz_buzz.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fizz_buzz.data.Resultat
import com.example.fizz_buzz.data.ResultatRepository
import com.example.fizz_buzz.localservice.DonneesSaisieService
import com.example.fizz_buzz.model.DonneesSaisie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class ResultatViewModel : ViewModel() {
    private val repository = ResultatRepository(DonneesSaisieService())

    val pagingDataFlow: Flow<PagingData<Resultat>>

    /**
     * Processor of side effects from the UI which in turn feedback into [state]
     */
    val accept: (UserIntent) -> Unit

    init {
        val actionStateFlow: MutableStateFlow<UserIntent> by lazy {
            MutableStateFlow(UserIntent.AfficheResultats(DonneesSaisie(1,1,"","")))
        }
        val searches = actionStateFlow
            .filterIsInstance<UserIntent.AfficheResultats>()
            .distinctUntilChanged()

        pagingDataFlow = searches
            .flatMapLatest { repository.resultatPagingSource(query = it.donneesSaisie)  }
            .cachedIn(viewModelScope)

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }
    }
}
