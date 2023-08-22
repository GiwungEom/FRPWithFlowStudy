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


@Composable
fun AirplaneReservationScreen(
    modifier: Modifier = Modifier,
    viewModel: AirplaneReservationViewModel = viewModel()
) {

    val reservationState: ReservationState by viewModel.reservationState.collectAsState()
    val onChangeDate = { type: ReservationType, date: ReservationDate ->
        viewModel.sendEventAction(
            UserAction.ChangeReservation(type, date)
        )
    }
    val onButtonClick = {
        viewModel.sendEventAction(
            UserAction.PressOkButton
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
            date = reservationState.depReservationDate,
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
            date = reservationState.retReservationDate,
            yearRange = viewModel.yearRange,
            monthRange = viewModel.monthRange,
            dayRange = viewModel.dayRange,
            onChangeDate = onChangeDate
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = onButtonClick,
            enabled = reservationState.validation,
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
    date: ReservationDate,
    yearRange: IntRange,
    monthRange: IntRange,
    dayRange: IntRange,
    onChangeDate: (ReservationType, ReservationDate) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = title)

        ReserveDropDownView(yearRange) {
            onChangeDate(type, date.copy(year = it))
        }
        ReserveDropDownView(monthRange) {
            onChangeDate(type, date.copy(month = it))
        }
        ReserveDropDownView(dayRange) {
            onChangeDate(type, date.copy(day = it))
        }
    }
}

@Composable
fun ReserveDropDownView(
    rangeDate: IntRange,
    onDateSelected: (String) -> Unit
) {
    var expandState by remember { mutableStateOf(false) }
    var dateState by remember { mutableStateOf(rangeDate.first.toString()) }

    Column {
        Button(
            onClick = { expandState = true }
        ) {
            Text(text = dateState)
        }

        DropdownMenu(
            expanded = expandState,
            onDismissRequest = { expandState = false }
        ) {
            for (date in rangeDate) {
                DropdownMenuItem(
                    text = { Text(text = date.toString()) },
                    onClick = {
                        dateState = date.toString()
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
    AirplaneReservationScreen()
}