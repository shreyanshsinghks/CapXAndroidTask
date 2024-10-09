package com.shreyanshsinghks.cap_x_android_assignment_shreyansh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.shreyanshsinghks.cap_x_android_assignment_shreyansh.presentation.StockLookupApp
import com.shreyanshsinghks.cap_x_android_assignment_shreyansh.ui.theme.Cap_X_Android_Assignment_ShreyanshTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Cap_X_Android_Assignment_ShreyanshTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        StockLookupApp()
                    }
                }
            }
        }
    }
}