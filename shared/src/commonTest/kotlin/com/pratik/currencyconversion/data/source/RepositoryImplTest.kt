package com.pratik.currencyconversion.data.source

import com.pratik.currencyconversion.data.db.Database
import com.pratik.currencyconversion.data.db.createTestDriver
import com.pratik.currencyconversion.data.network.ApiService
import com.pratik.currencyconversion.data.network.MockServer
import com.pratik.currencyconversion.domain.models.RepositoryResult
import com.pratik.currencyconversion.domain.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryImplTest {

    private lateinit var repositoryImpl: RepositoryImpl

    private val job = Job()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(job + testDispatcher)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val apiService = ApiService(MockServer.mockServer)
        val database = Database(createTestDriver(), Dispatchers.Default)
        repositoryImpl = RepositoryImpl(apiService, database)
    }

    @Test
    fun `get rates with api call success response with rates data`() = testScope.runTest {
        MockServer.reset()
        MockServer.expectSuccess(true)
        MockServer.expectSuccessResponse(true)

        val result = repositoryImpl.getAllRates().first()
        assertTrue(result is RepositoryResult.Success, message = "Error")
        assertEquals(result.data?.size, 3, result.data.toString())
    }

    @Test
    fun `get rates with api call success but no rates in response`() = testScope.runTest {
        MockServer.reset()
        MockServer.expectSuccess(true)
        MockServer.expectNoRates(true)

        val result = repositoryImpl.getAllRates().first()
        assertTrue(result is RepositoryResult.Error)
        assertEquals(result.errorMessage, Constants.EMPTY_LIST_MESSAGE)
    }

    @Test
    fun `get rates with api call success but empty rates in response`() = testScope.runTest {
        MockServer.reset()
        MockServer.expectSuccess(true)
        MockServer.expectEmptyRates(true)

        val result = repositoryImpl.getAllRates().first()
        assertTrue(result is RepositoryResult.Error)
        assertEquals(result.errorMessage, Constants.EMPTY_LIST_MESSAGE)
    }

    @Test
    fun `get rates with api call success but empty response`() = testScope.runTest {
        MockServer.reset()
        MockServer.expectSuccess(true)
        MockServer.expectEmptyResponse(true)

        val result = repositoryImpl.getAllRates().first()
        assertTrue(result is RepositoryResult.Error)
        assertEquals(result.errorMessage, Constants.UNKNOWN_ERROR)
    }

    @Test
    fun `get rates with api call fail`() = testScope.runTest {
        MockServer.reset()
        MockServer.expectSuccess(false)

        val result = repositoryImpl.getAllRates().first()
        assertTrue(result is RepositoryResult.Error)
        assertEquals(result.errorMessage, Constants.EMPTY_LIST_MESSAGE)
    }

    @Test
    fun `get rates with unavailable request`() = testScope.runTest {
        MockServer.reset()
        MockServer.expectChangeRequestUrl(true)

        val result = repositoryImpl.getAllRates().first()
        assertTrue(result is RepositoryResult.Error)
        assertEquals(result.errorMessage, Constants.UNKNOWN_ERROR)
    }

    @Test
    fun `get rates with api call first the again call the method to check response`() =
        testScope.runTest {
            MockServer.reset()
            MockServer.expectSuccess(true)
            MockServer.expectSuccessResponse(true)

            repositoryImpl.getAllRates().first()
            val result = repositoryImpl.getAllRates().first()
            assertTrue(result is RepositoryResult.Success)
            assertEquals(result.data?.size, 3)
        }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

}