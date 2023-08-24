package com.gw.study.frp.ui.screen.lesson.reservation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gw.study.frp.ui.screen.UiState
import com.gw.study.frp.ui.screen.toValue


@Composable
fun ReservationScreen(
    modifier: Modifier = Modifier,
    viewModel: ReservationViewModel = viewModel(
        modelClass = ReservationViewModel::class.java,
        factory = ReservationViewModel.Factory
    )
) {
    val depDate by viewModel.depDateState.collectAsState(initial = UiState.Loading)
    val retDate by viewModel.retDateState.collectAsState(initial = UiState.Loading)

    val dateValidation by viewModel.dateValidation.collectAsState()

    val onChangeDate = { type: ReservationType, date: ReservationDate ->
        viewModel.sendEvent(
            UserEvent.ChangeReservation(type, date)
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // departure
        CalenderSelector(
            title = "Departure",
            type = ReservationType.Departure,
            date = depDate,
            yearRange = viewModel.yearRange,
            monthRange = viewModel.monthRange,
            dayRange = viewModel.dayRange,
            onChangeDate = onChangeDate
        )
        Spacer(modifier = Modifier.height(15.dp))
        // return
        CalenderSelector(
            title = "Return",
            type = ReservationType.Return,
            date = retDate,
            yearRange = viewModel.yearRange,
            monthRange = viewModel.monthRange,
            dayRange = viewModel.dayRange,
            onChangeDate = onChangeDate
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {},
            enabled = dateValidation,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(text = "Ok", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun CalenderSelector(
    title: String,
    type: ReservationType,
    date: UiState<ReservationDate>,
    yearRange: IntRange,
    monthRange: IntRange,
    dayRange: IntRange,
    onChangeDate: (ReservationType, ReservationDate) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = title)

        when (date) {
            is UiState.State -> {
                ReserveDropDownView(date.toValue().year, yearRange) {
                    onChangeDate(type, date.toValue().copy(year = it))
                }
                ReserveDropDownView(date.toValue().month, monthRange) {
                    onChangeDate(type, date.toValue().copy(month = it))
                }
                ReserveDropDownView(date.toValue().day, dayRange) {
                    onChangeDate(type, date.toValue().copy(day = it))
                }
            }
            is UiState.Loading -> {
                Text(text = "State is Loading..")
            }
        }
    }
}

@Composable
fun ReserveDropDownView(
    dateText: String,
    rangeDate: IntRange,
    onDateSelected: (String) -> Unit
) {
    var expandState by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { expandState = true }
        ) {
            Text(text = dateText)
        }

        DropdownMenu(
            expanded = expandState,
            onDismissRequest = { expandState = false }
        ) {
            for (date in rangeDate) {
                DropdownMenuItem(
                    text = { Text(text = date.toString()) },
                    onClick = {
                        onDateSelected(date.toString())
                        expandState = false
                    }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AirplaneReservationScreenPreview() {
    ReservationScreen()
}