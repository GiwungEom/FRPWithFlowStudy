package com.gw.study.frp.ui.screen.lesson.clearfield

import com.gw.study.frp.ui.screen.ActionEvent
import com.gw.study.frp.ui.screen.FrpViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

sealed class UserEvent: ActionEvent {
    object ClearText : UserEvent()
    data class TextChanged(val text: String): UserEvent()
}

class ClearFieldViewModel : FrpViewModel() {

    private val _textState = MutableStateFlow("")
    private val buttonClickFlow: MutableSharedFlow<Unit> = MutableSharedFlow()

    private val clearTextFlow = buttonClickFlow.map { "" }
    val textState = merge(_textState, clearTextFlow)

    override suspend fun actionHandle(actionEvent: ActionEvent) {
        when (actionEvent) {
            is UserEvent.ClearText -> {
                buttonClickFlow.emit(Unit)
            }
            is UserEvent.TextChanged -> {
                _textState.value = actionEvent.text
            }
        }
    }
}