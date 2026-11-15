package com.example.payment_app.ui.screens.payment

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payment_app.ui.theme.Payment_AppTheme

@Composable
fun PaymentResultScreen(
    receipt: PaymentReceipt,
    onDone: () -> Unit,
    onViewDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp)
    ) {
        // Header: Title (left) & subtle decorative check icon (right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Payment result",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FigmaTextMuted
                )
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(FigmaSurfaceHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = FigmaPrimaryPill,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hero section
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Payment completed",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = FigmaTextMuted,
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = receipt.formattedAmount,
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = FigmaTextPrimary,
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = "Paid to ${receipt.toUser} for ${receipt.groupName}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = FigmaTextMuted,
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = receipt.cardMethod,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = FigmaTextMuted,
                        textAlign = TextAlign.Center
                    )
                )
            }

            // Receipt card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = FigmaSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Receipt / reference",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FigmaTextMuted
                        )
                    )
                    Text(
                        text = "Example receipt — for confirmation only",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = FigmaTextMuted
                        )
                    )
                    Text(
                        text = receipt.referenceId,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FigmaTextPrimary
                        ),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    Text(
                        text = receipt.formattedDate,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = FigmaTextMuted
                        )
                    )
                    Text(
                        text = "From ${receipt.fromUser}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = FigmaTextMuted
                        )
                    )
                    Text(
                        text = "To ${receipt.toUser}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = FigmaTextMuted
                        )
                    )
                    Text(
                        text = "Group ${receipt.groupName}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = FigmaTextMuted
                        )
                    )
                    Text(
                        text = "Status: ${receipt.status}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = FigmaTextMuted
                        )
                    )
                    Text(
                        text = "Method: ${receipt.cardMethod}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = FigmaTextMuted
                        )
                    )
                }
            }

            // Remaining debt settlement block
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${receipt.groupName} remaining",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FigmaTextMuted
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = receipt.formattedRemainingDebt,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = FigmaTextPrimary,
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = "Your ${receipt.groupName} share is settled.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        color = FigmaTextMuted,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action buttons row (Done & View details)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onDone,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FigmaPrimaryPill,
                        contentColor = FigmaTextDark
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Button(
                    onClick = onViewDetails,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FigmaSurface,
                        contentColor = FigmaTextPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Text(
                        text = "View details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun PaymentResultScreenPreview() {
    Payment_AppTheme {
        Surface(color = FigmaBackground) {
            PaymentResultScreen(
                receipt = PaymentReceipt(),
                onDone = {},
                onViewDetails = {}
            )
        }
    }
}
