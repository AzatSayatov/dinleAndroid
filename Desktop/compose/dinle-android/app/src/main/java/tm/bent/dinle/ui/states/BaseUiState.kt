package tm.bent.dinle.ui.states

import tm.bent.dinle.domain.model.Artist
import tm.bent.dinle.domain.model.ArtistDetail
import tm.bent.dinle.domain.model.Home
import tm.bent.dinle.domain.model.PagingData


data class BaseUIState(
    val success: Boolean = false,
    val loading: Boolean = false,
    val failure: Boolean = false,
    val errorMessage: String = "",
) {
    fun updateToLoading(): BaseUIState {
        return copy(loading = true)
    }

    fun updateToLoaded(errorMessage: String = ""): BaseUIState {
        return copy(success = true, loading = false, failure = false, errorMessage = errorMessage)
    }

    fun updateToFailure(errorMessage: String = ""): BaseUIState {
        return copy(loading = false, failure = true, errorMessage = errorMessage)
    }

    fun updateToDefault(): BaseUIState {
        return copy(
            success = false,
            loading = false,
            failure = false,
        )
    }
}

data class UIState<T>(
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isFailure: Boolean = false,
    val data: T? = null,
    val errorMessage: String = "",
) {
    fun updateToLoading(): UIState<T> {
        return copy(isLoading = true)
    }

    fun updateToLoaded(data: T): UIState<T> {
        return copy(isSuccess = true, isLoading = false, data = data, isFailure = false)
    }

    fun updateToFailure(errorMessage: String): UIState<T> {
        return copy(isLoading = false, isFailure = true, errorMessage = errorMessage)
    }

    fun updateToDefault():  UIState<T> {
        return copy(
            isSuccess = false,
            isLoading = false,
            isFailure = false,
            data = null
        )
    }
}