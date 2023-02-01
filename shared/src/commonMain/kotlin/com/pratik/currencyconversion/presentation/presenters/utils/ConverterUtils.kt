package com.pratik.currencyconversion.presentation.presenters.utils

import com.pratik.currencyconversion.domain.utils.Constants.NUMBER_PRECISION
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.roundToInt

const val thousand = 1000.0
const val million = 1000000.0
const val billion = 1000000000.0
const val trillion = 1000000000000.0

fun convertToTargetCurrencyRate(
    sourceCurrencyRate: Double,
    sourceCurrencyValue: Double,
    targetCurrencyRate: Double
): String {
    val targetValue =
        (targetCurrencyRate * sourceCurrencyValue) / sourceCurrencyRate
    val targetValueUpdated = when {
        targetValue >= 0.0 && targetValue < thousand -> {
            targetValue.toPrecision()
        }
        targetValue >= thousand && targetValue < million -> {
            val withFraction = calculateFraction(targetValue, thousand).toPrecision()
            "$withFraction K"
        }
        targetValue >= million && targetValue < billion -> {
            val withFraction = calculateFraction(targetValue, million).toPrecision()
            "$withFraction M"
        }
        targetValue >= billion && targetValue < trillion -> {
            val withFraction = calculateFraction(targetValue, billion).toPrecision()
            "$withFraction B"
        }

        targetValue >= trillion -> {
            val withFraction = calculateFraction(targetValue, trillion).toPrecision()
            "$withFraction T"
        }
        else -> {
            ""
        }
    }
    return targetValueUpdated
}

fun calculateFraction(number: Double, divisor: Double): Double {
    return number / divisor
}

fun Double.toPrecision(precision: Int = NUMBER_PRECISION) =
    if (precision < 1) {
        "${this.roundToInt()}"
    } else {
        val p = 10.0.pow(precision)
        val v = (abs(this) * p).roundToInt()
        val i = floor(v / p)
        var f = "${floor(v - (i * p)).toInt()}"
        while (f.length < precision) f = "0$f"
        val s = if (this < 0) "-" else ""
        "$s${i.toInt()}.$f"
    }
