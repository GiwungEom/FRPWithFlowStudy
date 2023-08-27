package com.gw.study.frp.ui.screen.route

sealed class Routes(val route: String) {
    object Home : Routes(route = "Home")
    object Lesson1 : Routes(route = "Lesson1")
    object Lesson2 : Routes(route = "Lesson2")
    object Lesson3 : Routes(route = "Lesson3")
    object Lesson4 : Routes(route = "Lesson4")
}