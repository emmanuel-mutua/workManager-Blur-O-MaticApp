/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bluromatic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.data.BlurAmountData
import com.example.bluromatic.data.BluromaticRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * [BlurViewModel] starts and stops the WorkManger and applies blur to the image. Also updates the
 * visibility states of the buttons depending on the states of the WorkManger.
 */
@HiltViewModel
class BlurViewModel @Inject constructor(
    private val bluromaticRepository: BluromaticRepository
) : ViewModel() {

    //private val bluromaticRepository: BluromaticRepository

    internal val blurAmount = BlurAmountData.blurAmount

    val blurUiState: StateFlow<BlurUiState> = bluromaticRepository.outputWorkInfo.map {
        info ->
        var outputImageUri = info.outputData.getString(KEY_IMAGE_URI)
        when{
            info.state.isFinished && !outputImageUri.isNullOrEmpty() -> {
                BlurUiState.Complete(outputUri = outputImageUri)
            }
            info.state == WorkInfo.State.CANCELLED -> {
                BlurUiState.Default
            }
            else -> BlurUiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BlurUiState.Default
    )

    //stateIn() convert flow to a stateflow

    /**
     * Call the method from repository to create the WorkRequest to apply the blur
     * and save the resulting image
     * @param blurLevel The amount to blur the image
     */
    fun applyBlur(blurLevel: Int) {
        bluromaticRepository.applyBlur(blurLevel)
    }

    fun cancelWork(){
        bluromaticRepository.cancelWork()
    }

//    /**
//     * Factory for [BlurViewModel] that takes [BluromaticRepository] as a dependency
//     */
//    companion object {
//        val Factory: ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                val bluromaticRepository =
//                    (this[APPLICATION_KEY] as BluromaticApplication).container.bluromaticRepository
//                BlurViewModel(
//                    bluromaticRepository = bluromaticRepository
//                )
//            }
//        }
//    }
}

