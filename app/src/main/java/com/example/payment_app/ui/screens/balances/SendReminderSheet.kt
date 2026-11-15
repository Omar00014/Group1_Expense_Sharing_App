package com.example.payment_app.ui.screens.balances

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Displaying a Bottom Sheet 画面下からでてくるポップアップ作成
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendReminderSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = BalancesBackground,
        contentColor = Color.White
    ) {
        SendReminderContent(onCancel = onDismiss, onSend = onDismiss)
    }
}

//Creating a Bottom Sheet ポップアップの中身作成
@Composable
fun SendReminderContent(onCancel: () -> Unit = {}, onSend: () -> Unit = {}) {
    var message by remember {
        mutableStateOf("Hi Jordan, please settle your $22 balance in Weekend Trip. Thanks!")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("Send reminder", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Weekend Trip", color = BalancesSubText, fontSize = 12.sp)
        Text("$22.00", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
        Text("Current group-net debt", color = BalancesSubText, fontSize = 11.sp)
        Text("Jordan @jordan owes Alex Taylor @alex", color = BalancesSubText, fontSize = 11.sp)

        Spacer(Modifier.height(8.dp))
        Text("Message", color = Color.White, fontSize = 12.sp)
        TextField(
            value = message,
            onValueChange = { message = it },
            minLines = 3,
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BalancesCardColor,
                unfocusedContainerColor = BalancesCardColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BalancesPillButton("Cancel", Modifier.weight(1f), onCancel)
            BalancesPillButton("Send", Modifier.weight(1f), onSend)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SendReminderContentPreview() {
    Box(modifier = Modifier.background(BalancesBackground)) {
        SendReminderContent()
    }
}