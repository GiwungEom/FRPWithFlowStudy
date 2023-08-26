package com.gw.study.frp.ui.screen.lesson

import com.gw.study.frp.ui.screen.route.Routes

// Lessons From FRP
object Lessons {
    val lessons: List<Lesson> = listOf(
        Lesson(
            title = "FRP Basic (Validation)",
            route = Routes.Lesson1
        ),
        Lesson(
            title = "Clear Field (Merge)",
            route = Routes.Lesson2
        ),
        Lesson(
            title = "Translate (Snapshot)",
            route = Routes.Lesson3
        )
    )
}

data class Lesson(val title: String, val route: Routes)