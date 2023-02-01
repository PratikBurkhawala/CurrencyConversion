package com.pratik.currencyconversion.domain.mappers

import com.pratik.currencyconversion.domain.models.CurrencyRate
import compratikcurrencyconversion.data.db.Rates

fun Rates.rateDbToCurrencyRate(): CurrencyRate {
    return CurrencyRate(
        this.code,
        this.rates
    )
}