package com.pratik.currencyconversion.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonObject

@kotlinx.serialization.Serializable
data class SuccessResponse(
    @SerialName("license")
    val license: String? = null,

    @SerialName("rates")
    val rates: JsonObject? = null,

    @SerialName("disclaimer")
    val disclaimer: String? = null,

    @SerialName("timestamp")
    val timestamp: Int? = null,

    @SerialName("base")
    val base: String? = null
)