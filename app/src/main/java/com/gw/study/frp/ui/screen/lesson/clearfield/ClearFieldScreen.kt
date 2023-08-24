package com.gw.study.frp.ui.screen.lesson.clearfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClearFieldScreen(
    modifier: Modifier = Modifier,
    viewModel: ClearFieldViewModel = viewModel()
) {
    val textState by viewModel.textState.collectAsState(initial = "")
    val onTextChanged = { text: String ->
        viewModel.sendEvent(UserEvent.TextChanged(text = text))
    }
    val onButtonClicked = {
        viewModel.sendEvent(UserEvent.ClearText)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = onTextChanged,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = onButtonClicked) {
            Text(text = "Clear Text")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ClearFieldScreenPreview() {
    ClearFieldScreen()
}