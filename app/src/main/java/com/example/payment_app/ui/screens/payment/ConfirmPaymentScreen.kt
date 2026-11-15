package com.example.payment_app.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payment_app.ui.theme.Payment_AppTheme

@Composable
fun ConfirmPaymentScreen(
    context: PaymentShareContext,
    card: PaymentCard,
    senderName: String = "Alex Taylor",
    onConfirm: () -> Unit,
    onBack: () -> Unit,
    onEditDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Header: Back (left), Title "Confirm payment", Edit (right)
            PaymentTopBar(
                title = "Confirm payment",
                onBack = onBack,
                endIcon = Icons.Filled.Edit,
                onEndClick = onEditDetails
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Summary block
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Review before sending",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            color = FigmaTextMuted
                        )
                    )
                    Text(
                        text = context.formattedAmount,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = FigmaTextPrimary
                        )
                    )
                    Text(
                        text = "You will pay ${context.recipientName} for ${context.groupName}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            color = FigmaTextMuted
                        )
                    )
                }

                // Recipient card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = FigmaSurface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Avatar M
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD9D9D9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = context.recipientName.firstOrNull()?.uppercase() ?: "M",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FigmaTextDark
                                )
                            )
                        }

                        // Recipient names
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = context.recipientName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = FigmaTextPrimary
                                )
                            )
                            Text(
                                text = context.recipientHandle,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    color = FigmaTextMuted
                                )
                            )
                        }

                        // Group chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(FigmaSurfaceHigh)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = context.groupName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = FigmaTextMuted
                                )
                            )
                        }
                    }
                }

                // Payment Details breakdown
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = FigmaSurface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Payment details",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = FigmaTextMuted
                            )
                        )

                        PaymentDetailRow(label = "Payment amount", value = context.formattedAmount)
                        PaymentDetailRow(label = "Fees", value = "$0.00")
                        PaymentDetailRow(label = "Total", value = context.formattedAmount, isBold = true)

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 2.dp),
                            color = FigmaSurfaceHigh,
                            thickness = 1.dp
                        )

                        PaymentDetailRow(label = "From", value = senderName)
                        PaymentDetailRow(label = "Card", value = card.displayCompact)
                        PaymentDetailRow(label = "Expiry", value = card.expiryDate)
                    }
                }

                // Note / Before you send
                PaymentSecurityNote(
                    title = "Before you send",
                    description = "Review the details, then confirm to pay your share."
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Footer Actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryPill(
                text = "Confirm payment",
                onClick = onConfirm,
                height = 48
            )

            Text(
                text = "Back to edit details",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = FigmaTextMuted,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(onClick = onEditDetails)
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun PaymentDetailRow(
    label: String,
    value: String,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                color = FigmaTextMuted
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold,
                color = FigmaTextPrimary
            )
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun ConfirmPaymentScreenPreview() {
    Payment_AppTheme {
        Surface(color = FigmaBackground) {
            ConfirmPaymentScreen(
                context = PaymentShareContext(),
                card = defaultSavedCards.first(),
                onConfirm = {},
                onBack = {},
                onEditDetails = {}
            )
        }
    }
}
