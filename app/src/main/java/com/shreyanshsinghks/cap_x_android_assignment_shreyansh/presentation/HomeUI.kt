package com.shreyanshsinghks.cap_x_android_assignment_shreyansh.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shreyanshsinghks.cap_x_android_assignment_shreyansh.data.StockMatch
import com.shreyanshsinghks.cap_x_android_assignment_shreyansh.data.StockViewModel

@Composable
fun StockLookupApp(viewModel: StockViewModel = viewModel()) {
    var keywords by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = keywords,
            onValueChange = { keywords = it },
            label = { Text("Enter Stock Name or Symbol") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.searchStocks(keywords)  // Use actual user input
                Log.d("StockLookupApp", "Search button clicked with keywords: $keywords")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            viewModel.isLoading -> {
                CircularProgressIndicator()
            }
            viewModel.error != null -> {
                Text(
                    text = viewModel.error ?: "An error occurred",
                    color = MaterialTheme.colorScheme.error
                )
            }
            viewModel.searchResults.isNotEmpty() -> {
                LazyColumn {
                    items(viewModel.searchResults) { stock ->
                        StockInfoCard(stock)
                    }
                }
            }
        }
    }
}

@Composable
fun StockInfoCard(stock: StockMatch) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "${stock.name} (${stock.symbol})", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Type: ${stock.type}")
            Text(text = "Region: ${stock.region}")
            Text(text = "Currency: ${stock.currency}")
            Text(text = "Market Open: ${stock.marketOpen}")
            Text(text = "Market Close: ${stock.marketClose}")
            Text(text = "Timezone: ${stock.timezone}")
            Text(text = "Match Score: ${stock.matchScore}")
        }
    }
}
