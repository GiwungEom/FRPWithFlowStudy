package com.gw.study.frp.ui.screen.lesson.reservation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.gw.study.frp.ui.screen.ActionEvent
import com.gw.study.frp.ui.screen.FrpViewModel
import com.gw.study.frp.ui.screen.UiState
import com.gw.study.frp.ui.screen.toValue
import com.gw.study.frp.ui.screen.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

sealed class UserEvent: ActionEvent {
    class ChangeReservation(
        val type: ReservationType,
        val date: ReservationDate
    ) : UserEvent()
}

class ReservationViewModel : FrpViewModel() {

    val monthRange = (1..12)
    val yearRange = (2023..2025)
    val dayRange = (1..31)

    init {
        Log.d("ReservationViewModel", "$this initialized")
    }

    // 출발 날짜 상태
    private val _depDateState: MutableStateFlow<UiState<Calendar>> = MutableStateFlow(
        UiState.State(Calendar.getInstance())
    )

    // 리턴 날짜 상태
    private val _retDateState: MutableStateFlow<UiState<Calendar>> = MutableStateFlow(
        UiState.State(Calendar.getInstance())
    )

    val depDateState = _depDateState.map(transferToDate())
    val retDateState = _retDateState.map(transferToDate())

    private fun transferToDate(): (UiState<Calendar>) -> UiState<ReservationDate> {
        fun Calendar.toReservationDate(): ReservationDate =
            ReservationDate(
                year = get(Calendar.YEAR).toString(),
                month = (get(Calendar.MONTH) + 1).toString(),
                day = get(Calendar.DAY_OF_MONTH).toString()
            )

        return {
            when (it) {
                is UiState.State -> UiState.State(it.toValue().toReservationDate())
                is UiState.Loading -> it
            }
        }
    }

    // 두 날짜 상태 중 하나의 상태 변경 시 유효성 체크 진행
    private val _dateValidation = _depDateState.combine(_retDateState) { depDate, retDate ->
        depDate.toValue() <= retDate.toValue()
    }

    // 유효성 상태
    val dateValidation = _dateValidation.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        false
    )

    // 사용자 인터페이스 액션 이벤트 처리
    override suspend fun actionHandle(actionEvent: ActionEvent) {
        when (actionEvent) {
            is UserEvent.ChangeReservation -> {
                changeReservation(
                    actionEvent.type,
                    actionEvent.date
                )
            }
        }
    }

    // 사용자 인터페이스 날짜 상태 변경 시 내부 날짜 상태 업데이트 진행
    private fun changeReservation(
        reservationType: ReservationType,
        reservationDate: ReservationDate
    ) {
        if (reservationType == ReservationType.Departure) {
            _depDateState.updateState {
                Calendar.getInstance().apply {
                    set(Calendar.YEAR, reservationDate.year.toInt())
                    set(Calendar.MONTH, reservationDate.month.toInt() - 1)
                    set(Calendar.DAY_OF_MONTH, reservationDate.day.toInt())
                }
            }
        } else {
            _retDateState.updateState {
                Calendar.getInstance().apply {
                    set(Calendar.YEAR, reservationDate.year.toInt())
                    set(Calendar.MONTH, reservationDate.month.toInt() - 1)
                    set(Calendar.DAY_OF_MONTH, reservationDate.day.toInt())
                }
            }
        }
    }
}