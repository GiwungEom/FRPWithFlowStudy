package com.gw.study.frp.ui.screen.lesson.reservation


enum class ReservationType {
    Departure, Return
}

data class ReservationState(
    val depReservationDate: ReservationDate,
    val retReservationDate: ReservationDate,
    val validation: Boolean
)

data class ReservationDate(
    val year: String,
    val month: String,
    val day: String
)