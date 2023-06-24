package com.example.bluromatic.ui

sealed interface BlurUiState {
    object Default : BlurUiState
    object Loading : BlurUiState
    data class Complete(val outputUri: String) : BlurUiState
}