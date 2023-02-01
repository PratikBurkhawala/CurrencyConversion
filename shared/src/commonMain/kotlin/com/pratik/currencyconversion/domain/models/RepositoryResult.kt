package com.pratik.currencyconversion.domain.models

sealed class RepositoryResult<T>(
    val data: T? = null,
    val errorMessage: String? = null
) {
    class Success<T>(data: T) : RepositoryResult<T>(data)
    class Error<T>(errorMessage: String?) :
        RepositoryResult<T>(errorMessage = errorMessage)
}