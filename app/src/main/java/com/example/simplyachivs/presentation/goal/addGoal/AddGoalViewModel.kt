package com.example.simplyachivs.presentation.goal.addGoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.domain.model.goal.StepStatus
import com.example.simplyachivs.domain.usecase.goal.AddGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    private val addGoalUseCase: AddGoalUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<AddGoalUiState>(AddGoalUiState())
    val state = _state.asStateFlow()


    private val _effect = MutableSharedFlow<AddGoalEffect>(replay = 0, extraBufferCapacity = 1)
    val effect = _effect.asSharedFlow()

    private fun sendEffect(effect: AddGoalEffect) {
        viewModelScope.launch {
            _effect.emit(effect)

        }
    }
    fun moveStep(from: Int, to: Int) {
        _state.update { current ->
            val steps = current.steps.toMutableList()

            if (from !in steps.indices || to !in steps.indices) return@update current

            val moved = steps.removeAt(from)
            steps.add(to, moved)

            val updatedSteps = steps.mapIndexed { index, step ->
                step.copy(position = index + 1)
            }

            current.copy(steps = updatedSteps)
        }
    }
    init {
        _state.update { it.copy(id = UUID.randomUUID()) }
    }

    fun processIntent(intent: AddGoalIntent) {

        when (intent) {
            AddGoalIntent.GoBack -> sendEffect(AddGoalEffect.NavigateToGoals)
            AddGoalIntent.OpenImagePicker -> sendEffect(AddGoalEffect.LaunchImagePicker)
            AddGoalIntent.AddNewGoal -> {
                val name = _state.value.goalName.trim()
                val nameError = when {
                    name.isBlank() -> "Введите название цели"
                    name.length < 3 -> "Название должно быть не менее 3 символов"
                    else -> null
                }
                if (nameError != null) {
                    _state.update { it.copy(goalNameError = nameError) }
                    return
                }
                _state.update { it.copy(goalNameError = null) }
                sendEffect(AddGoalEffect.NavigateToGoals)
            }

            is AddGoalIntent.AddNewStep -> {
                val stepName = intent.stepName.trim()
                val stepError = when {
                    stepName.isBlank() -> "Введите название шага"
                    stepName.length < 2 -> "Минимум 2 символа"
                    else -> null
                }
                if (stepError != null) {
                    _state.update { it.copy(newStepNameError = stepError) }
                    return
                }
                _state.update {
                    it.copy(
                        newStepNameError = null,
                        steps = it.steps + Step(
                            UUID.randomUUID(),
                            _state.value.id!!,
                            stepName,
                            StepStatus.ACTIVE,
                            _state.value.steps.size + 1
                        ),
                        newStepName = ""
                    )
                }
            }

            is AddGoalIntent.ChangeNewStepName -> _state.update {
                it.copy(newStepName = intent.name.take(50), newStepNameError = null)
            }
            is AddGoalIntent.SelectGoalComplexity -> {
                _state.update { it.copy(complexity = intent.complexity) }
                viewModelScope.launch {
                    _effect.emit(AddGoalEffect.SelectComplexity)
                }
            }

            is AddGoalIntent.ChangeGoalDescription -> _state.update {
                it.copy(goalDescription = intent.description.take(200))
            }
            is AddGoalIntent.ChangeGoalName -> _state.update {
                it.copy(goalName = intent.name.take(50), goalNameError = null)
            }
            is AddGoalIntent.DeleteStep -> {

                _state.update {
                    it.copy(steps = _state.value.steps.map { step ->
                        if (step.position > intent.position) {
                            step.copy(position = step.position - 1)
                        } else {
                            step
                        }
                    })
                }
                _state.update { it.copy(steps = _state.value.steps.filter { it.id != intent.stepId }) }


            }

            is AddGoalIntent.SelectGoalImage -> _state.update { it.copy(goalImage = intent.imageUri) }
        }

    }


}