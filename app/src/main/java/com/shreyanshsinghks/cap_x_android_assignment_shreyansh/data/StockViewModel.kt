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
    var searchResults by mutableStateOf<List<StockMatch>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    private val json = Json { ignoreUnknownKeys = true }

    private val apiService = Retrofit.Builder()
        .baseUrl("https://www.alphavantage.co/")
        .addConverterFactory(ScalarsConverterFactory.create()) // Only Scalars needed for string response
        .build()
        .create(AlphaVantageApiService::class.java)

    fun searchStocks(keywords: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            searchResults = emptyList()
            try {
                Log.d("StockViewModel", "Searching for stocks with keywords: $keywords")
                val responseString = apiService.searchSymbols(keywords)
                Log.d("StockViewModel", "API Response: $responseString")

                val stockMatchResponse = json.decodeFromString<StockMatchResponse>(responseString)

                // Check if bestMatches is null before proceeding
                if (!stockMatchResponse.bestMatches.isNullOrEmpty()) {
                    searchResults = stockMatchResponse.bestMatches
                } else {
                    error = "No results found for '$keywords'"
                }
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
data class StockMatchResponse(
    val bestMatches: List<StockMatch>? // Make this nullable
)

@Serializable
data class StockMatch(
    @kotlinx.serialization.SerialName("1. symbol")
    val symbol: String,
    @kotlinx.serialization.SerialName("2. name")
    val name: String,
    @kotlinx.serialization.SerialName("3. type")
    val type: String,
    @kotlinx.serialization.SerialName("4. region")
    val region: String,
    @kotlinx.serialization.SerialName("5. marketOpen")
    val marketOpen: String,
    @kotlinx.serialization.SerialName("6. marketClose")
    val marketClose: String,
    @kotlinx.serialization.SerialName("7. timezone")
    val timezone: String,
    @kotlinx.serialization.SerialName("8. currency")
    val currency: String,
    @kotlinx.serialization.SerialName("9. matchScore")
    val matchScore: String
)

interface AlphaVantageApiService {
    @GET("query?function=SYMBOL_SEARCH")
    suspend fun searchSymbols(
        @Query("keywords") keywords: String,
        @Query("apikey") apiKey: String = API_KEY
    ): String // Change return type to String to match ScalarsConverterFactory
}
