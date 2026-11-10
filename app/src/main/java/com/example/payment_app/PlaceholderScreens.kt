package com.example.payment_app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
private fun PlaceholderScreen(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) = PlaceholderScreen("Home", modifier)

@Composable
fun GroupsScreen(modifier: Modifier = Modifier) = PlaceholderScreen("Groups", modifier)

@Composable
fun ActivityScreen(modifier: Modifier = Modifier) = PlaceholderScreen("Activity", modifier)

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) = PlaceholderScreen("Profile", modifier)
