package com.example.payment_app.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payment_app.ui.theme.Payment_AppTheme

@Composable
fun PaymentMethodsScreen(
    cards: List<PaymentCard>,
    onAddCard: () -> Unit,
    onManageCard: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Header (Figma screen 25)
        PaymentTopBar(
            title = "Payment methods",
            onBack = onBack,
            backLabel = "Back"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Intro section
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Cards",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = FigmaTextPrimary
                    )
                )
                Text(
                    text = "Choose a saved card or add another payment method.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        color = FigmaTextMuted,
                        lineHeight = 18.sp
                    )
                )
            }

            // Add card button
            PrimaryPill(
                text = "Add card",
                onClick = onAddCard,
                leadingIcon = Icons.Filled.Add,
                height = 44
            )

            // Saved cards section
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Saved cards",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FigmaTextMuted
                        )
                    )
                    Text(
                        text = "${cards.size} ${if (cards.size == 1) "card" else "cards"}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 13.sp,
                            color = FigmaTextMuted
                        )
                    )
                }

                cards.forEach { card ->
                    PaymentCardItem(
                        card = card,
                        onActionClick = { onManageCard(card.id) }
                    )
                }
            }

            // Security note
            PaymentSecurityNote()
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun PaymentMethodsScreenPreview() {
    Payment_AppTheme {
        Surface(color = FigmaBackground) {
            PaymentMethodsScreen(
                cards = defaultSavedCards,
                onAddCard = {},
                onManageCard = {},
                onBack = {}
            )
        }
    }
}
