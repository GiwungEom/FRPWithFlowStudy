package com.gw.study.frp.ui.screen.lesson.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UserAction {
    class ChangeReservation(
        val type: ReservationType,
        val date: ReservationDate
    ) : UserAction()
    object PressOkButton: UserAction()
}

class AirplaneReservationViewModel : ViewModel() {

    val monthRange = (1..12)
    val yearRange = (2023..2025)
    val dayRange = (1..31)

    private var eventChannel: Channel<UserAction> = Channel()

    init {
        viewModelScope.launch {
            for (event in eventChannel) {
                launch {
                    when (event) {
                        is UserAction.ChangeReservation -> {
                            changeReservation(
                                event.type,
                                event.date
                            )
                        }
                        is UserAction.PressOkButton -> {

                        }
                    }
                }
            }
        }
    }

    private fun changeReservation(
        reservationType: ReservationType,
        reservationDate: ReservationDate
    ) {
        if (reservationType == ReservationType.Departure) {
            _reservationDateState.value = _reservationDateState.value.copy(
                depReservationDate = reservationDate
            )
        } else {
            _reservationDateState.value = _reservationDateState.value.copy(
                retReservationDate = reservationDate
            )
        }
    }

    private val _reservationDateState = MutableStateFlow(
        ReservationState(
            depReservationDate = ReservationDate(
                year = yearRange.first.toString(),
                month = monthRange.first.toString(),
                day = dayRange.first.toString()
            ),
            retReservationDate = ReservationDate(
                year = yearRange.first.toString(),
                month = monthRange.first.toString(),
                day = dayRange.first.toString()
            ),
            validation = false
        )
    )
    
    val reservationState = _reservationDateState.asStateFlow()

    fun sendEventAction(userAction: UserAction) {
        viewModelScope.launch {
            eventChannel.send(userAction)
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventChannel.close()
    }

}