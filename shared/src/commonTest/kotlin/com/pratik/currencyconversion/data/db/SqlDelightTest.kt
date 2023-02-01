package com.pratik.currencyconversion.data.db

import com.goncalossilva.resources.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SqlDelightTest {

    private lateinit var database: Database

    private val job = Job()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(job + testDispatcher)

    private val mockSuccessResponse200 = Resource(
        "src/commonTest/resources/success_response_200.json"
    ).readText()

    private val emptyRatesResponse200 = Resource(
        "src/commonTest/resources/empty_rates_response_200.json"
    ).readText()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val driver = createTestDriver()
        database = Database(driver, Dispatchers.Default)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Check insert operation with rates in response result must be change in DB`() =
        testScope.runTest {
            val toObject = Json.parseToJsonElement(mockSuccessResponse200)
            val rates = toObject.jsonObject["rates"]?.jsonObject
            if (rates != null) {
                val actualSize = rates.size
                database.insertCurrencyDetails(rates)
                val resultSize = database.getAllRates().first().size
                assertEquals(actualSize, resultSize)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Check insert operation with no rates after success insertion from first operation in response result must be unchanged in DB`() =
        testScope.runTest {
            val toObject1 = Json.parseToJsonElement(mockSuccessResponse200)
            val rates1 = toObject1.jsonObject["rates"]?.jsonObject
            if (rates1 != null) {
                database.insertCurrencyDetails(rates1)
                val resultSize1 = database.getAllRates().first().size

                val toObject2 = Json.parseToJsonElement(emptyRatesResponse200)
                val rates2 = toObject2.jsonObject["rates"]?.jsonObject
                if (rates2 != null) {
                    database.insertCurrencyDetails(rates2)
                    val resultSize2 = database.getAllRates().first().size
                    assertEquals(resultSize1, resultSize2)
                }
            }
        }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }
}