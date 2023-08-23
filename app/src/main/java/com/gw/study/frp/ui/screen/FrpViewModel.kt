package com.gw.study.frp.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

// 뷰 인터페이스 액션 이벤트
interface ActionEvent

// 뷰 상태 공통
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class State<T>(val value: T) : UiState<T>()
}

fun <T> UiState<T>.toState(): UiState.State<T> = this as UiState.State<T>
fun <T> UiState<T>.toValue(): T = toState().value

// 상태 업데이트 공통 - UiState 내부 데이터를 receiver 로 보낸뒤 객체 수정 후 UiState 로 변경.
fun <T> MutableStateFlow<UiState<T>>.updateState(
    block: T.() -> T
) {
    value = UiState.State(
        value.toValue().block()
    )
}

// 공통 기능 모음 뷰 모델
abstract class FrpViewModel : ViewModel() {

    // 사용자 뷰 인터페이스 이벤트 채널
    private val actionEventChannel: Channel<ActionEvent> = Channel()

    init {
        viewModelScope.launch {
            // 이벤트 동시 실행
            for (event in actionEventChannel) {
                launch {
                    ensureActive()
                    actionHandle(event)
                }
            }
        }
    }

    abstract suspend fun actionHandle(actionEvent: ActionEvent)

    fun sendEvent(actionEvent: ActionEvent) {
        viewModelScope.launch {
            actionEventChannel.send(actionEvent)
        }
    }

    override fun onCleared() {
        super.onCleared()
        actionEventChannel.close()
    }
}