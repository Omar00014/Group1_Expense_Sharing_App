package com.example.payment_app.ui.screens

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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.payment_app.ui.screens.payment.FigmaBackground
import com.example.payment_app.ui.screens.payment.FigmaPrimaryPill
import com.example.payment_app.ui.screens.payment.FigmaSurface
import com.example.payment_app.ui.screens.payment.FigmaSurfaceHigh
import com.example.payment_app.ui.screens.payment.FigmaTextDark
import com.example.payment_app.ui.screens.payment.FigmaTextMuted
import com.example.payment_app.ui.screens.payment.FigmaTextPrimary
import com.example.payment_app.ui.theme.Payment_AppTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onPayMyShare: () -> Unit = {},
    onPaymentMethods: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FigmaBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Top welcome header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Welcome back,",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        color = FigmaTextMuted
                    )
                )
                Text(
                    text = "Alex Taylor",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = FigmaTextPrimary
                    )
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(FigmaSurface)
                    .clickable(onClick = onPaymentMethods),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.CreditCard,
                    contentDescription = "Payment methods",
                    tint = FigmaTextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Active Group Debt Card (Apartment)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = FigmaSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(FigmaSurfaceHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = null,
                                tint = FigmaTextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Apartment",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = FigmaTextPrimary
                                )
                            )
                            Text(
                                text = "3 members · 7 expenses",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    color = FigmaTextMuted
                                )
                            )
                        }
                    }

                    Text(
                        text = "$14.50",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = FigmaTextPrimary
                        )
                    )
                }

                Text(
                    text = "You owe Maya $14.50 for Apartment.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        color = FigmaTextMuted
                    )
                )

                Button(
                    onClick = onPayMyShare,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FigmaPrimaryPill,
                        contentColor = FigmaTextDark
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text(
                        text = "Pay my share ($14.50)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        // Quick shortcut to Payment Methods
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onPaymentMethods),
            shape = RoundedCornerShape(14.dp),
            color = FigmaSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Payment methods",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FigmaTextPrimary
                        )
                    )
                    Text(
                        text = "Manage saved cards & default card",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            color = FigmaTextMuted
                        )
                    )
                }

                Text(
                    text = "Manage",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = FigmaTextPrimary
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun HomeScreenPreview() {
    Payment_AppTheme {
        Surface(color = FigmaBackground) {
            HomeScreen()
        }
    }
}
