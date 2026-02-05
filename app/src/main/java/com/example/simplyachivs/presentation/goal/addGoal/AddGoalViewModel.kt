package com.example.simplyachivs.presentation.goal.addGoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplyachivs.domain.model.goal.Step
import com.example.simplyachivs.domain.model.goal.StepStatus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.collections.map

class AddGoalViewModel : ViewModel() {

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

                //addition goal logic
                sendEffect(AddGoalEffect.NavigateToGoals)

            }

            is AddGoalIntent.AddNewStep -> {

                _state.update {
                    it.copy(
                        steps = it.steps + Step(
                            UUID.randomUUID(),
                            _state.value.id!!,
                            intent.stepName,
                            StepStatus.ACTIVE,
                            _state.value.steps.size + 1
                        ), newStepName = ""
                    )
                }

            }

            is AddGoalIntent.ChangeNewStepName -> _state.update { it.copy(newStepName = intent.name) }
            is AddGoalIntent.SelectGoalComplexity -> {
                _state.update { it.copy(complexity = intent.complexity) }
                viewModelScope.launch {
                    _effect.emit(AddGoalEffect.SelectComplexity)
                }
            }

            is AddGoalIntent.ChangeGoalDescription -> _state.update { it.copy(goalDescription = intent.description) }
            is AddGoalIntent.ChangeGoalName -> _state.update { it.copy(goalName = intent.name) }
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