package com.gw.study.frp.ui.screen.lesson.reservation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar

class Rule(
    val f: (Calendar, Calendar) -> Boolean
) {

    fun reify(dep: Flow<Calendar>, ret: Flow<Calendar>) = dep.combine(ret, f)

    private fun and(other: Rule): Rule {
        return Rule { dep, ret ->
            f.invoke(dep, ret) && other.f.invoke(dep, ret)
        }
    }

    operator fun plus(other: Rule): Rule = and(other)
}

val ruleForNormal = Rule(
    f = { depDate, retDate -> depDate <= retDate }
)

val ruleForChina = Rule(
    f = { depDate, retDate -> !unlucky(depDate) && !unlucky(retDate) }
)

// 중국에서의 불길한 날
fun unlucky(calendar: Calendar): Boolean {
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return day == 4 || day == 14 || day == 24
}