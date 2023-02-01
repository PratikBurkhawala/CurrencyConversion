package com.pratik.currencyconversion.data.network.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ErrorResponse(
    @SerialName("description")
    val description: String? = null,

    @SerialName("error")
    val error: Boolean? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName("status")
    val status: Int? = null
)

