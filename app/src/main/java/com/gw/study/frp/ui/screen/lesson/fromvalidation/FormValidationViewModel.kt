package com.gw.study.frp.ui.screen.lesson.fromvalidation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.gw.study.frp.ui.screen.ActionEvent
import com.gw.study.frp.ui.screen.FrpViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn

sealed class UserEvent : ActionEvent {
    data class NameChange(val name: String): UserEvent()
    data class EmailMaxCountChange(val operator: Operator): UserEvent()
    data class EmailChange(val number: Int, val email: String): UserEvent()

    enum class Operator {
        Plus, Minus
    }
}

class FormValidationViewModel : FrpViewModel() {

    private val maxEmailCount = 4
    private val emailNumberInit = 2

    private val _nameState = MutableStateFlow("")
    val nameState = _nameState.asStateFlow()

    // 사용자 인터페이스 +,- 이벤트
    private val plus = MutableSharedFlow<Unit>()
    private val minus = MutableSharedFlow<Unit>()
    private val delta = merge(plus.map { 1 }, minus.map { -1 })

    // 이메일 갯수 상태
    val emailNumberState = delta.scan(emailNumberInit) { accumulator, value -> accumulator + value }
                                .stateIn(
                                    viewModelScope,
                                    started = SharingStarted.Eagerly,
                                    initialValue = emailNumberInit
                                )
    // 이메일 상태 배열
    private val _emailsState: Array<MutableStateFlow<String>> = Array(size = maxEmailCount) { MutableStateFlow("") }
    val emailsState: Array<StateFlow<String>> = Array(size = maxEmailCount) { _emailsState[it].asStateFlow() }

    // 이름 유효성 검증
    val nameValidation = _nameState.map {
        if (it.trim().isEmpty()) {
            "enter something"
        } else if (it.trim().indexOf(' ') < 0) {
            "must contain space"
        } else {
            ""
        }
    }

    // 이메일 갯수 유효성 검증
    val emailCountValidation = emailNumberState.map {
        if (it < 1 || it > maxEmailCount) {
            "must be 1 to $maxEmailCount"
        } else {
            ""
        }
    }

    // 이메일 유효성 검증 배열
    val emailValidations = Array(size = maxEmailCount) {
        // combine 시에 Cold Flow Stream 넣을 경우, emailsState 수 만큼 시작 되므로,
        // Hot Flow 형태로 적용
        emailsState[it].combine(emailNumberState) { email, number ->
            if (it < number) {
                if (email.trim().isEmpty()) {
                    "enter something"
                } else if (email.indexOf('@') < 0) {
                    "must contain @"
                } else {
                    ""
                }
            } else {
                ""
            }
        }
    }

    // 전체 유효성 상태
    val allValidationState: Flow<Boolean>

    init {
        Log.d("FormViewModel", "initialized $this")

        // 유효성 배열
        val validations = arrayOf(
            nameValidation,
            emailCountValidation,
            *emailValidations
        )

        var allValidation: Flow<Boolean> = MutableStateFlow(true)
        // 에러 메세지가 없다면 유효한 것으로 판단 하도록 책 내용 반영
        for (validation in validations) {
            val validationFlow = validation.map { it == "" }
            // 유효성 스트림 배열 -> 단일 스트림
            allValidation = allValidation.combine(validationFlow) { a, b -> a && b }
        }
        this.allValidationState = allValidation
    }

    override suspend fun actionHandle(actionEvent: ActionEvent) {
        when (actionEvent) {
            is UserEvent.NameChange -> {
                _nameState.value = actionEvent.name
            }
            is UserEvent.EmailMaxCountChange -> {
                if (actionEvent.operator == UserEvent.Operator.Plus) plus.emit(Unit)
                else minus.emit(Unit)
            }
            is UserEvent.EmailChange -> {
                _emailsState[actionEvent.number].value = actionEvent.email
            }
        }
    }
}