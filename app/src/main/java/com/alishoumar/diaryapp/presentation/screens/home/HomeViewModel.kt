package com.alishoumar.diaryapp.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alishoumar.diaryapp.data.respository.Diaries
import com.alishoumar.diaryapp.data.respository.MongoDB
import com.alishoumar.diaryapp.model.RequestState
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        observeAllDiaries()
    }
    private fun observeAllDiaries(){
        viewModelScope.launch {

            MongoDB.getAllDiaries().collect { result ->
                diaries.value = result
            }
        }
    }
}