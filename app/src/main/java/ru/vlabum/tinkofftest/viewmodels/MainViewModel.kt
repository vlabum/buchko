package ru.vlabum.tinkofftest.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vlabum.tinkofftest.data.entity.DevelopersLife
import ru.vlabum.tinkofftest.data.repo.DevelopersLiveRepository
import ru.vlabum.tinkofftest.data.repo.ResultDevLife


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

        if (currentState.index + 1 < currentState.counter) {
            val nextIndex = currentState.index + 1
            updateState { it.copy(
                index = nextIndex,
                currentGifUrl = repository.devLifeList[nextIndex].gifURL,
                currentDescription = repository.devLifeList[nextIndex].description,
                isDisableBack = false,
                showError = false
                ) }
        }
        else {
            viewModelScope.launch {
                showLoading()

                val pageHot = currentState.pageHot + if (currentState.category == Category.HOT) 1 else 0
                val pageLatest = currentState.pageLatest + if (currentState.category == Category.LATEST) 1 else 0
                val pageTop = currentState.pageTop + if (currentState.category == Category.TOP) 1 else 0

                val result = when (currentState.category) {
                    Category.RANDOM -> repository.getNext()
                    Category.HOT -> repository.getHot(pageHot)
                    Category.LATEST -> repository.getLatest(pageLatest)
                    else -> repository.getTop(pageTop)
                }

                when (result) {
                    is ResultDevLife.Success<*> ->
                        updateState {
                            val nextIndex =
                                (if ((it.index + 1) >= repository.devLifeList.size) repository.devLifeList.size - 1 else (it.index + 1))
                            val isDisableBack = (nextIndex == 0)
                            it.copy(
                                counter = repository.devLifeList.size,
                                index = nextIndex,
                                currentGifUrl = repository.devLifeList[nextIndex].gifURL,
                                currentDescription = repository.devLifeList[nextIndex].description,
                                isDisableBack = isDisableBack,
                                showError = false,
                                pageHot = pageHot,
                                pageLatest = pageLatest,
                                pageTop = pageTop
                            )
                        }

                    is ResultDevLife.Error ->
                        updateState { it.copy(showError = true) }
                }
                hideLoading()
            }
        }
}

fun getPrev() {
    //При кешировании в SQLite, нужно будет тоже сделать корутины
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
}

    fun setCategory(category: Category) {
        updateState { it.copy(category = category) }
    }

}


data class MainState(
    val isDisableBack: Boolean = true,
    val counter: Int = 0,
    val index: Int = -1,
    val pageLatest: Int = -1,
    val pageHot: Int = -1,
    val pageTop: Int = -1,
    val category: Category = Category.RANDOM,
    val currentGifUrl: String = "",
    val currentDescription: String = "",
    val showError: Boolean = false
)

enum class Loading {
    SHOW_LOADING, HIDE_LOADING
}

enum class Category {
    RANDOM, LATEST, HOT, TOP
}