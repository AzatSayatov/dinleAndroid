package tm.bent.dinle.domain.model

data class PagingData <out T>(
    val count : Long = 0,
    val rows : List<T>,
)
