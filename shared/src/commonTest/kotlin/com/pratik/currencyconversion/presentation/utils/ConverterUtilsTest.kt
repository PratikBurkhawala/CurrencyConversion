package com.pratik.currencyconversion.presentation.utils

import com.pratik.currencyconversion.presentation.presenters.utils.convertToTargetCurrencyRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ConverterUtilsTest {

    private val sourceCurrencyRateAgainstDollar = 1.0
    private val targetCurrentRateAgainstDollar = 10.0

    private val job = Job()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(job + testDispatcher)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `verify value under thousand`() = testScope.runTest {
        val sourceCurrencyValue = 10.0
        val targetCurrentValue = convertToTargetCurrencyRate(
            sourceCurrencyRateAgainstDollar,
            sourceCurrencyValue,
            targetCurrentRateAgainstDollar
        )
        assertEquals(targetCurrentValue, "100.00")
    }

    @Test
    fun `verify value under million`() = testScope.runTest {
        val sourceCurrencyValue = 10000.0
        val targetCurrentValue = convertToTargetCurrencyRate(
            sourceCurrencyRateAgainstDollar,
            sourceCurrencyValue,
            targetCurrentRateAgainstDollar
        )
        assertEquals(targetCurrentValue, "100.00 K")
    }

    @Test
    fun `verify value under billion`() = testScope.runTest {
        val sourceCurrencyValue = 10000000.0
        val targetCurrentValue = convertToTargetCurrencyRate(
            sourceCurrencyRateAgainstDollar,
            sourceCurrencyValue,
            targetCurrentRateAgainstDollar
        )
        assertEquals(targetCurrentValue, "100.00 M")
    }

    @Test
    fun `verify value under trillion`() = testScope.runTest {
        val sourceCurrencyValue = 10000000000.0
        val targetCurrentValue = convertToTargetCurrencyRate(
            sourceCurrencyRateAgainstDollar,
            sourceCurrencyValue,
            targetCurrentRateAgainstDollar
        )
        assertEquals(targetCurrentValue, "100.00 B")
    }

    @Test
    fun `verify value above trillion`() = testScope.runTest {
        val sourceCurrencyValue = 10000000000000.0
        val targetCurrentValue = convertToTargetCurrencyRate(
            sourceCurrencyRateAgainstDollar,
            sourceCurrencyValue,
            targetCurrentRateAgainstDollar
        )
        assertEquals(targetCurrentValue, "100.00 T")
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

}