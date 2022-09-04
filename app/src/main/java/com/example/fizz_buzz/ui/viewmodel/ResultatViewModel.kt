package com.example.fizz_buzz.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fizz_buzz.data.Resultat
import com.example.fizz_buzz.data.ResultatRepository
import com.example.fizz_buzz.domain.model.DonneesSaisie
import com.example.fizz_buzz.domain.use_case.ValidationEntier
import com.example.fizz_buzz.domain.use_case.ValidationMot
import com.example.fizz_buzz.localservice.DonneesSaisieService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*


@OptIn(ExperimentalCoroutinesApi::class)
class ResultatViewModel : ViewModel() {
    private val repository = ResultatRepository(DonneesSaisieService())
    private val validationEntier: ValidationEntier = ValidationEntier()
    private val validationMot: ValidationMot = ValidationMot()

    private val _entier1IsError = MutableLiveData<Boolean>()
    private val _entier1ErrorText = MutableLiveData<String?>()

    private val _entier2IsError = MutableLiveData<Boolean>()
    private val _entier2ErrorText = MutableLiveData<String?>()

    private val _mot1IsError = MutableLiveData<Boolean>()
    private val _mot1ErrorText = MutableLiveData<String?>()

    private val _mot2IsError = MutableLiveData<Boolean>()
    private val _mot2ErrorText = MutableLiveData<String?>()

    val entier1IsError: LiveData<Boolean>
        get() = _entier1IsError
    val entier1ErrorText: LiveData<String?>
        get() = _entier1ErrorText

    val entier2IsError: LiveData<Boolean>
        get() = _entier2IsError
    val entier2ErrorText: LiveData<String?>
        get() = _entier2ErrorText

    val mot1IsError: LiveData<Boolean>
        get() = _mot1IsError
    val mot1ErrorText: LiveData<String?>
        get() = _mot1ErrorText

    val mot2IsError: LiveData<Boolean>
        get() = _mot2IsError
    val mot2ErrorText: LiveData<String?>
        get() = _mot2ErrorText

    val pagingDataFlow: Flow<PagingData<Resultat>>

    val inputDataFlow: Flow<Boolean>

    /**
     * Processor of side effects from the UI which in turn feedback into [state]
     */
    val actionStateFlow: MutableStateFlow<UserIntent> by lazy {
        MutableStateFlow(UserIntent.DonneesInput(null, null, null, null))
    }

    init {

        val searches = actionStateFlow
            .filterIsInstance<UserIntent.AfficheResultats>()
            .distinctUntilChanged()

        pagingDataFlow = searches
            .flatMapLatest { repository.resultatPagingSource(query = it.donneesSaisie) }
            .cachedIn(viewModelScope)

        val actions = actionStateFlow
            .filterIsInstance<UserIntent.DonneesInput>()
            .distinctUntilChanged()

        inputDataFlow = actions
            .flatMapLatest {
               flow<Boolean> {
                    emit(false)
                    val entier1Result = validationEntier.execute(it.entier1)
                    val entier2Result = validationEntier.execute(it.entier2)
                    val mot1Result = validationMot.execute(it.mot1)
                    val mot2Result = validationMot.execute(it.mot2)

                    _entier1IsError.value = entier1Result.successful
                    _entier2IsError.value = entier2Result.successful
                    _mot1IsError.value = mot1Result.successful
                    _mot2IsError.value = mot2Result.successful

                    _entier1ErrorText.value = entier1Result.errorMessage
                    _entier2ErrorText.value = entier2Result.errorMessage
                    _mot1ErrorText.value = mot1Result.errorMessage
                    _mot2ErrorText.value = mot2Result.errorMessage

                    val hasError = listOf(
                        entier1Result.successful,
                        entier2Result.successful,
                        mot1Result.successful,
                        mot2Result.successful
                    ).any { successful -> !successful }

                    if (!hasError) {
                        actionStateFlow.emit(
                            UserIntent.AfficheResultats(
                                DonneesSaisie(
                                    entier1Result.data!!.toInt(),
                                    entier2Result.data!!.toInt(),
                                    mot1Result.data!!,
                                    mot2Result.data!!
                                )
                            )
                        )
                        emit(true)
                    }
                }
            }
    }
}
