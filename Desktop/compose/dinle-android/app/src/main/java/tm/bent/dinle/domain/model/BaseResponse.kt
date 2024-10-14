package tm.bent.dinle.domain.model


data class PagingResponse< out T>(
    val statusCode : Int,
    val success : Boolean,
    val data : PagingData<T> ,
)

data class BaseResponse< out T>(
    val statusCode : Int,
    val success : Boolean,
    val data : T
)