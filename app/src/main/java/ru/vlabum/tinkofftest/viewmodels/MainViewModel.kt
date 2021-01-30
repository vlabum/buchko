package ru.vlabum.tinkofftest.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vlabum.tinkofftest.data.repo.DevelopersLiveRepository


class MainViewModel(
    initState: MainState = MainState()
) : ViewModel() {

    private val repository = DevelopersLiveRepository

    private val loading = MutableLiveData<Loading>(Loading.HIDE_LOADING)

    val state: MediatorLiveData<MainState> = MediatorLiveData<MainState>().apply {
        value = initState
    }

    val currentState
        get() = state.value!!

    inline fun updateState(update: (currentState: MainState) -> MainState) {
        val updatedState: MainState = update(currentState)
        state.value = updatedState
    }

    fun observeState(owner: LifecycleOwner, onChanged: (newState: MainState) -> Unit) {
        state.observe(owner, Observer { onChanged(it!!) })
    }

    fun observeLoading(owner: LifecycleOwner, onChanged: (newState: Loading) -> Unit) {
        loading.observe(owner, Observer { onChanged(it!!) })
    }

    protected fun showLoading(loadingType: Loading = Loading.SHOW_LOADING) {
        loading.value = loadingType
    }

    protected fun hideLoading() {
        loading.value = Loading.HIDE_LOADING
    }


    fun getCurrent() {
        if (repository.devLifeList.isEmpty())
            getNext()
        else
            viewModelScope.launch {
                updateState { it.copy() }
            }
    }

    fun getNext() {
        showLoading()
        viewModelScope.launch {
            repository.getNext()
            updateState {
                val nextIndex =
                    (if ((it.index + 1) >= repository.devLifeList.size) repository.devLifeList.size - 1 else (it.index + 1))
                val isDisableBack = (nextIndex == 0)
                it.copy(
                    counter = repository.devLifeList.size,
                    index = nextIndex,
                    currentGifUrl = repository.devLifeList[nextIndex].gifURL,
                    currentDescription = repository.devLifeList[nextIndex].description,
                    isDisableBack = isDisableBack
                )
            }
        }
        hideLoading()
    }

    fun getPrev() {
        viewModelScope.launch {
            showLoading()
            val prevIndex = if (currentState.index - 1 <= 0) 0 else currentState.index - 1
            val isDisableBack = (prevIndex == 0)
            updateState {
                it.copy(
                    index = prevIndex,
                    currentGifUrl = repository.devLifeList[prevIndex].gifURL,
                    currentDescription = repository.devLifeList[prevIndex].description,
                    isDisableBack = isDisableBack
                )
            }
            hideLoading()
        }
    }

}


data class MainState(
    val isDisableBack: Boolean = true,
    val counter: Int = 0,
    val index: Int = -1,
    val category: Int = 0,
    val currentGifUrl: String = "",
    val currentDescription: String = ""
)

enum class Loading {
    SHOW_LOADING, HIDE_LOADING
}