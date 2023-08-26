package com.gw.study.frp.ui.screen.lesson.translate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslateScreen(
    modifier: Modifier = Modifier,
    viewModel: TranslateViewModel = viewModel()
) {
    val translatedTextState by viewModel.translateTextState.collectAsState()
    val textState by viewModel.textState.collectAsState()
    val onTextChanged = { text: String ->
        viewModel.sendEvent(UserAction.TextChanged(text = text))
    }
    val onTranslateClicked = {
        viewModel.sendEvent(UserAction.ButtonClicked)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = textState,
                onValueChange = onTextChanged
            )
            Button(onClick = onTranslateClicked) {
                Text(text = "Translate")
            }
        }

        Text(
            text = translatedTextState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(color = Color.Green))
    }
}

@Preview
@Composable
fun TranslateScreenPreview() {
    TranslateScreen()
}