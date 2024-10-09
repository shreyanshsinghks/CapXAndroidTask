package com.shreyanshsinghks.cap_x_android_assignment_shreyansh.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class StockViewModel : ViewModel() {
    var stockQuote by mutableStateOf<GlobalQuote?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    private val json = Json { ignoreUnknownKeys = true }

    private val apiService = Retrofit.Builder()
        .baseUrl("https://www.alphavantage.co/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(AlphaVantageApiService::class.java)

    fun searchStockQuote(symbol: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            stockQuote = null
            try {
                Log.d("StockViewModel", "Fetching quote for symbol: $symbol")
                val responseString = apiService.getGlobalQuote(symbol)
                Log.d("StockViewModel", "API Response: $responseString")

                val globalQuoteResponse = json.decodeFromString<GlobalQuoteResponse>(responseString)
                stockQuote = globalQuoteResponse.globalQuote
            } catch (e: Exception) {
                error = "Failed to fetch stock data: ${e.message}"
                Log.e("StockViewModel", "Error fetching stock data", e)
            } finally {
                isLoading = false
            }
        }
    }
}


@Serializable
data class GlobalQuoteResponse(
    @kotlinx.serialization.SerialName("Global Quote")
    val globalQuote: GlobalQuote
)

@Serializable
data class GlobalQuote(
    @kotlinx.serialization.SerialName("01. symbol")
    val symbol: String,
    @kotlinx.serialization.SerialName("02. open")
    val open: String,
    @kotlinx.serialization.SerialName("03. high")
    val high: String,
    @kotlinx.serialization.SerialName("04. low")
    val low: String,
    @kotlinx.serialization.SerialName("05. price")
    val price: String,
    @kotlinx.serialization.SerialName("06. volume")
    val volume: String,
    @kotlinx.serialization.SerialName("07. latest trading day")
    val latestTradingDay: String,
    @kotlinx.serialization.SerialName("08. previous close")
    val previousClose: String,
    @kotlinx.serialization.SerialName("09. change")
    val change: String,
    @kotlinx.serialization.SerialName("10. change percent")
    val changePercent: String
)



interface AlphaVantageApiService {
    @GET("query?function=GLOBAL_QUOTE")
    suspend fun getGlobalQuote(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String = API_KEY
    ): String
}

