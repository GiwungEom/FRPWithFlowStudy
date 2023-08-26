package com.gw.study.frp.ui.screen.lesson.translate

import androidx.lifecycle.viewModelScope
import com.gw.study.frp.ui.screen.ActionEvent
import com.gw.study.frp.ui.screen.FrpViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class UserAction : ActionEvent {
    data class TextChanged(val text: String) : UserAction()
    object ButtonClicked : UserAction()
}

class TranslateViewModel : FrpViewModel() {

    private val _textState = MutableStateFlow("")
    val textState = _textState.asStateFlow()

    private val fButtonClicked = MutableSharedFlow<Unit>()

    val translateTextState = fButtonClicked.snapshot(_textState)
        .map { it.trim().replace(" |$".toRegex(), "us ").trim() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ""
        )

    // sodium 라이브러리의 snapshot 의 경우
    // Stream<T> 이벤트 발생시 캡쳐 할 Cell 상태로 부터 상태 Cell<T>을 얻는다.
    private fun <T, R> Flow<T>.snapshot(stateFlow: StateFlow<R>): Flow<R> =
        flow {
            collect {
                emit(stateFlow.value)
            }
        }

    override suspend fun actionHandle(actionEvent: ActionEvent) {
        when (actionEvent) {
            is UserAction.TextChanged -> {
                _textState.value = actionEvent.text
            }
            is UserAction.ButtonClicked -> {
                fButtonClicked.emit(Unit)
            }
        }
    }
}