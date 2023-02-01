package com.pratik.currencyconversion.presentation

import com.goncalossilva.resources.Resource
import com.pratik.currencyconversion.data.db.Database
import com.pratik.currencyconversion.data.db.createTestDriver
import com.pratik.currencyconversion.data.network.ApiService
import com.pratik.currencyconversion.data.network.MockServer
import com.pratik.currencyconversion.data.source.RepositoryImpl
import com.pratik.currencyconversion.domain.utils.Constants
import com.pratik.currencyconversion.presentation.presenters.CurrencyConverterStore
import com.pratik.currencyconversion.presentation.presenters.ScreenAction
import com.pratik.currencyconversion.presentation.presenters.ScreenSideEffect
import com.pratik.currencyconversion.presentation.presenters.utils.toPrecision
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyConverterStoreTest {

    private val job = Job()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(job + testDispatcher)

    private var hashMap: HashMap<String, String> = hashMapOf()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val mockSuccessResponse200 = Resource(
            "src/commonTest/resources/success_response_200.json"
        ).readText()
        Json.parseToJsonElement(mockSuccessResponse200).jsonObject["rates"]?.jsonObject?.entries?.forEach { element ->
            hashMap[element.key] = element.value.toString().toDouble().toPrecision()
        }
    }

    @Test
    fun `check initial state and side effect`() = testScope.runTest {
        val apiService = ApiService(MockServer.mockServer)
        val database = Database(createTestDriver(), Dispatchers.Default)
        val repository = RepositoryImpl(apiService, database)
        val store = CurrencyConverterStore(repository)

        val initialState = store.observeState().value

        assertEquals(initialState.convertedRates, emptyList())
        assertEquals(initialState.selectedCurrency, "")
        assertEquals(initialState.editFieldText, "")
        assertEquals(initialState.isLoading, false)
        assertEquals(initialState.availableOptions, emptyList())
    }

    @Test
    fun `On Init Action Perform With Success Response`() = testScope.runTest {
        MockServer.reset()
        MockServer.expectSuccess(true)
        MockServer.expectSuccessResponse(true)

        val apiService = ApiService(MockServer.mockServer)
        val database = Database(createTestDriver(), Dispatchers.Default)
        val repository = RepositoryImpl(apiService, database)
        val store = CurrencyConverterStore(repository)

        var count = 1

        launch {
            store.observeState().take(3).collect { state ->
                when (count) {
                    1 -> {
                        assertEquals(state.isLoading, false)
                        count++
                    }
                    2 -> {
                        assertEquals(state.isLoading, true)
                        count++
                    }
                    3 -> {
                        assertEquals(state.isLoading, false)
                        assertEquals(state.availableOptions.size, 3)
                    }
                }
            }
        }

        store.dispatch(ScreenAction.OnInit)
    }

    @Test
    fun `On Init Action Perform With Fail Response`() = testScope.runTest {
        MockServer.reset()
        MockServer.expectSuccess(false)

        val apiService = ApiService(MockServer.mockServer)
        val database = Database(createTestDriver(), Dispatchers.Default)
        val repository = RepositoryImpl(apiService, database)
        val store = CurrencyConverterStore(repository)

        var count = 1

        launch {
            store.observeState().take(3).collect { state ->
                when (count) {
                    1 -> {
                        assertEquals(state.isLoading, false)
                        count++
                    }
                    2 -> {
                        assertEquals(state.isLoading, true)
                        count++
                    }
                    3 -> {
                        assertEquals(state.isLoading, false)
                    }
                }
            }
        }

        launch {
            store.observeEffect().take(1).collect { effect ->
                assertTrue(effect is ScreenSideEffect.Error)
            }
        }

        store.dispatch(ScreenAction.OnInit)
    }

    @Test
    fun `On Init Action Perform With No Response`() = testScope.runTest {
        MockServer.reset()
        MockServer.expectSuccess(true)
        MockServer.expectNoRates(true)

        val apiService = ApiService(MockServer.mockServer)
        val database = Database(createTestDriver(), Dispatchers.Default)
        val repository = RepositoryImpl(apiService, database)
        val store = CurrencyConverterStore(repository)

        var count = 1

        launch {
            store.observeState().take(3).collect { state ->
                when (count) {
                    1 -> {
                        assertEquals(state.isLoading, false)
                        count++
                    }
                    2 -> {
                        assertEquals(state.isLoading, true)
                        count++
                    }
                    3 -> {
                        assertEquals(state.isLoading, false)
                        assertEquals(state.availableOptions.size, 0)
                    }
                }
            }
        }

        launch {
            store.observeEffect().take(1).collect { effect ->
                assertTrue(effect is ScreenSideEffect.Error)
                assertEquals(effect.errorMessage, Constants.EMPTY_LIST_MESSAGE)
            }
        }

        store.dispatch(ScreenAction.OnInit)
    }

    @Test
    fun `With Success response On Init action then Text Field Added then Selected Currency updated to verify state`() =
        testScope.runTest {
            MockServer.reset()
            MockServer.expectSuccess(true)
            MockServer.expectSuccessResponse(true)

            val apiService = ApiService(MockServer.mockServer)
            val database = Database(createTestDriver(), Dispatchers.Default)
            val repository = RepositoryImpl(apiService, database)
            val store = CurrencyConverterStore(repository)

            var count = 1

            launch {
                store.observeState().take(6).collect { state ->
                    when (count) {
                        1 -> {
                            assertEquals(state.isLoading, false)
                            count++
                        }
                        2 -> {
                            assertEquals(state.isLoading, true)
                            count++
                        }
                        3 -> {
                            assertEquals(state.isLoading, false)
                            assertEquals(state.availableOptions.size, 3)

                            store.dispatch(ScreenAction.OnEditFieldChange("1"))
                            count++
                        }
                        4 -> {
                            assertEquals(state.editFieldText, "1")

                            store.dispatch(ScreenAction.OnSelectedCurrencyChange("USD"))
                            count++
                        }
                        5 -> {
                            assertEquals(state.selectedCurrency, "USD")
                            count++
                        }
                        6 -> {
                            assertEquals(state.convertedRates.size, 3)

                            state.convertedRates.forEach { convertedCurrencyRate ->
                                assertEquals(
                                    convertedCurrencyRate.convertedRate,
                                    hashMap[convertedCurrencyRate.currencyCode]
                                )
                            }
                        }
                    }
                }
            }

            store.dispatch(ScreenAction.OnInit)
        }

    @Test
    fun `With Success response On Init action then Selected Currency then Text Field Added updated to verify state`() =
        testScope.runTest {
            MockServer.reset()
            MockServer.expectSuccess(true)
            MockServer.expectSuccessResponse(true)

            val apiService = ApiService(MockServer.mockServer)
            val database = Database(createTestDriver(), Dispatchers.Default)
            val repository = RepositoryImpl(apiService, database)
            val store = CurrencyConverterStore(repository)

            var count = 1

            launch {
                store.observeState().take(6).collect { state ->
                    when (count) {
                        1 -> {
                            assertEquals(state.isLoading, false)
                            count++
                        }
                        2 -> {
                            assertEquals(state.isLoading, true)
                            count++
                        }
                        3 -> {
                            assertEquals(state.isLoading, false)
                            assertEquals(state.availableOptions.size, 3)

                            store.dispatch(ScreenAction.OnSelectedCurrencyChange("USD"))

                            count++
                        }
                        4 -> {
                            assertEquals(state.selectedCurrency, "USD")

                            store.dispatch(ScreenAction.OnEditFieldChange("1"))
                            count++
                        }
                        5 -> {
                            assertEquals(state.editFieldText, "1")
                            count++
                        }
                        6 -> {
                            assertEquals(state.convertedRates.size, 3)

                            state.convertedRates.forEach { convertedCurrencyRate ->
                                assertEquals(
                                    convertedCurrencyRate.convertedRate,
                                    hashMap[convertedCurrencyRate.currencyCode]
                                )
                            }
                        }
                    }
                }
            }

            store.dispatch(ScreenAction.OnInit)
        }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

}