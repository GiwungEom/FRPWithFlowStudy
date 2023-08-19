package com.gw.study.frp.ui.screen.route

sealed class Routes(val route: String) {
    object Home : Routes(route = "Home")
    object Lesson1 : Routes(route = "Lesson1")
}