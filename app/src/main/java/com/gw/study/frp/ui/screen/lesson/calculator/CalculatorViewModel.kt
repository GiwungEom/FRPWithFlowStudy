package com.gw.study.frp.ui.screen.lesson.calculator

import com.gw.study.frp.ui.screen.ActionEvent
import com.gw.study.frp.ui.screen.FrpViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan


sealed class UserEvent: ActionEvent {
    object Plus : UserEvent()
    object Minus : UserEvent()
}

class CalculatorViewModel : FrpViewModel() {

    private val fPlusBtn = MutableSharedFlow<Unit>()
    private val fMinusBtn = MutableSharedFlow<Unit>()

    private val fPlusDelta = fPlusBtn.map { 1 }
    private val fMinusDelta = fMinusBtn.map { -1 }
    private val fDelta = merge(fPlusDelta, fMinusDelta)

    // sodium 전방 참조 루프 누적기 -> Flow 형식 수정 방식 2
    // value 참조 없이 scan 내부 누적기 사용
    val valueState =  fDelta.scan(0) { accumulator, value -> accumulator + value }

    override suspend fun actionHandle(actionEvent: ActionEvent) {
        when (actionEvent) {
            is UserEvent.Plus -> fPlusBtn.emit(Unit)
            is UserEvent.Minus -> fMinusBtn.emit(Unit)
        }
    }
/**
// sodium 전방 참조 루프 누적기 -> Flow 형식 수정 방식 1
// value 참조 후 value 결과 값 저장
//    private val _valueState = MutableStateFlow(0)
//    val valueState = _valueState.asStateFlow()

//    init {
//        fDelta.snapshotAndUpdate(_valueState) { calc, value -> calc + value }
//            .stateIn(
//                viewModelScope,
//                started = SharingStarted.Eagerly,
//                0
//            )
//    }

//    private fun <T, R> Flow<T>.snapshotAndUpdate(
//        state: MutableStateFlow<R>,
//        transform: (T, R) -> R
//    ): Flow<R> =
//        flow {
//            collect {
//                val result = transform(it, state.value)
//                emit(result)
//                state.value = result
//            }
//        }
**/
}