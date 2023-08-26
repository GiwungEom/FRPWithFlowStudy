package com.gw.study.frp.ui.screen.lesson.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = viewModel()
) {
    val valueState by viewModel.valueState.collectAsState(0)

    val onPlusClicked = {
        viewModel.sendEvent(UserEvent.Plus)
    }

    val onMinusClicked = {
        viewModel.sendEvent(UserEvent.Minus)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onPlusClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Plus")
            }
            Button(
                onClick = onMinusClicked,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Minus")
            }
        }
        Text(
            text = valueState.toString(),
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun CalculatorScreenPreview() {
    CalculatorScreen()
}