package com.gw.study.frp.ui.screen.lesson.fromvalidation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun FormValidationScreen(
    modifier: Modifier = Modifier,
    viewModel: FormValidationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val allValidationState by viewModel.allValidationState.collectAsState(initial = false)
    val emailValidationStateList = viewModel.emailValidations.map { it.collectAsState(initial = "") }
    val nameValidationState by viewModel.nameValidation.collectAsState(initial = "")
    val emailCountValidationState by viewModel.emailCountValidation.collectAsState(initial = "")

    val nameState by viewModel.nameState.collectAsState("")
    val emailStateList = viewModel.emailsState.map { it.collectAsState() }
    val emailCountState by viewModel.emailNumberState.collectAsState(0)

    // event
    val nameChange = { name: String -> viewModel.sendEvent(UserEvent.NameChange(name = name)) }
    val emailCountChange = { operator: UserEvent.Operator -> viewModel.sendEvent(UserEvent.EmailMaxCountChange(operator = operator))}
    val emailChange = { number: Int, email: String -> viewModel.sendEvent(UserEvent.EmailChange(number = number, email = email))}

    // compositionLocal
    // constraintSet
    // slot api
    FormInformationView(
        modifier = modifier,
        allValidationState = allValidationState,
        emailValidationStateList = emailValidationStateList,
        nameValidationState = nameValidationState,
        emailCountValidationState = emailCountValidationState,
        nameState = nameState,
        emailStateList = emailStateList,
        emailCount = emailCountState,
        nameChange = nameChange,
        emailCountChange = emailCountChange,
        emailChange = emailChange
    )
}

@Composable
fun FormInformationView(
    modifier: Modifier = Modifier,
    allValidationState: Boolean,
    emailValidationStateList: List<State<String>>,
    nameValidationState: String,
    emailCountValidationState: String,
    nameState: String,
    emailStateList: List<State<String>>,
    emailCount: Int,
    nameChange: (String) -> Unit,
    emailCountChange: (UserEvent.Operator) -> Unit,
    emailChange: (Int, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultForm(
            title = "Name",
            onDataChange = nameChange,
            validationState = nameValidationState,
            text = nameState
        )

        InformationItem(
            modifier = Modifier,
            text = "Number of Email",
            onDataChange = emailCountChange,
            validationState = emailCountValidationState
        ) {
            SpinnerItem(
                modifier = Modifier.weight(0.5f),
                count = emailCount,
                onButtonClick = it,
            )
        }

        emailStateList.forEachIndexed { index, state ->
            DefaultForm(
                title = "Email #${index + 1}",
                onDataChange = { emailChange(index, it) },
                validationState = emailValidationStateList[index].value,
                text = state.value,
                enabled = index < emailCount
            )
        }

        Button(
            onClick = {  },
            modifier = Modifier.padding(10.dp),
            enabled = allValidationState
        ) {
            Text(text = "Validation")
        }
    }
}

@Composable
fun SpinnerItem(
    modifier: Modifier = Modifier,
    count: Int,
    onButtonClick: (UserEvent.Operator) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .height(80.dp)
            .then(modifier)
    ) {
        val (text, plus, minus) = createRefs()
        Box(
            modifier = Modifier.constrainAs(text) {
                linkTo(parent.start, plus.start)
                linkTo(parent.top, parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                textAlign = TextAlign.Start,
                modifier = Modifier
            )
        }

        Button(
            onClick = { onButtonClick(UserEvent.Operator.Plus) },
            modifier = Modifier.constrainAs(plus) {
                linkTo(text.end, parent.end, startMargin = 5.dp, endMargin = 5.dp)
                linkTo(parent.top, minus.top, topMargin = 5.dp, bottomMargin = 5.dp)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        ) {
            Text(text = "+")
        }

        Button(
            onClick = { onButtonClick(UserEvent.Operator.Minus) },
            modifier = Modifier.constrainAs(minus) {
                linkTo(text.end, parent.end, startMargin = 5.dp, endMargin = 5.dp)
                linkTo(plus.bottom, parent.bottom, bottomMargin = 5.dp)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        ) {
            Text(text = "-")
        }
    }
}

@Composable
fun DefaultForm(
    modifier: Modifier = Modifier,
    title: String,
    onDataChange: (String) -> Unit,
    validationState: String,
    text: String,
    enabled: Boolean = true
) {
    InformationItem(
        modifier = modifier,
        text = title,
        onDataChange = onDataChange,
        validationState = validationState
    ) {
        InformationEdit(
            modifier = Modifier.weight(0.5f),
            text = text,
            onDataChange = it,
            enabled = enabled
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationEdit(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onDataChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onDataChange,
        modifier = modifier,
        enabled = enabled
    )
}

// 정보 입력 기본 프레임
@Composable
fun <T> InformationItem(
    modifier: Modifier = Modifier,
    text: String,
    onDataChange: (T) -> Unit,
    validationState: String,
    content: @Composable RowScope.(onDataChange: (T) -> Unit) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(5.dp)
                .weight(0.25f)
        )
        content(onDataChange)
        Text(
            text = "<-- $validationState",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(5.dp)
                .weight(0.25f)
        )
    }
}

@Preview
@Composable
fun FormValidationScreenPreview() {
    FormValidationScreen()
}