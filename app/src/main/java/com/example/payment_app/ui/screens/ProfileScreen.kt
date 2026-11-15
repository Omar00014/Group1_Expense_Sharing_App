package com.example.payment_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.payment_app.ui.screens.profile.Currency
import com.example.payment_app.ui.screens.profile.RowSubtitle
import com.example.payment_app.ui.screens.profile.RowTitle
import com.example.payment_app.ui.screens.profile.ScreenTitle
import com.example.payment_app.ui.screens.profile.SettingsCard
import com.example.payment_app.ui.screens.profile.UserProfile
import com.example.payment_app.ui.theme.Payment_AppTheme

// Placeholder until balances come from real data
private const val NET_OWED_PLACEHOLDER = "47.50"

@Composable
fun ProfileScreen(
    profile: UserProfile,
    onCurrencyChange: (Currency) -> Unit,
    onEditProfile: () -> Unit,
    onNotificationSettings: () -> Unit,
    modifier: Modifier = Modifier,
    onPaymentMethods: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header: label + avatar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFE0E0E0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "PFP",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF171A22)
                )
            }
        }

        // Identity
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            ScreenTitle(profile.fullName)
            Text(
                profile.username,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                profile.email,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Balance card
        SettingsCard {
            Text(
                "Net owed before payment",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "${profile.currency.symbol}$NET_OWED_PLACEHOLDER",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(6.dp))
            Text(
                profile.currency.code,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Primary currency with dropdown
        var currencyMenuOpen by remember { mutableStateOf(false) }
        SettingsCard(onClick = { currencyMenuOpen = true }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    RowTitle("Primary currency")
                    RowSubtitle(profile.currency.code)
                }
                Box {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            Currency.entries.joinToString(" / ") { it.code },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = "Choose currency",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    DropdownMenu(
                        expanded = currencyMenuOpen,
                        onDismissRequest = { currencyMenuOpen = false }
                    ) {
                        Currency.entries.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text(currency.code) },
                                onClick = {
                                    onCurrencyChange(currency)
                                    currencyMenuOpen = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Account section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Account",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Manage",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        SettingsCard(onClick = onEditProfile) {
            RowTitle("Edit profile")
            RowSubtitle("Name, email and username")
        }
        SettingsCard(onClick = onNotificationSettings) {
            RowTitle("Notification settings")
            RowSubtitle("Payments, reminders and updates")
        }
        SettingsCard(onClick = onPaymentMethods) {
            RowTitle("Payment methods")
            RowSubtitle("Saved cards and default card")
        }
        SettingsCard(onClick = onLogout) {
            RowTitle("Logout")
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 760)
@Composable
private fun ProfileScreenPreview() {
    Payment_AppTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ProfileScreen(
                profile = UserProfile("John Doe", "@john", "john@example.com", Currency.USD),
                onCurrencyChange = {},
                onEditProfile = {},
                onNotificationSettings = {}
            )
        }
    }
}