package com.alishoumar.diaryapp.presentation.screens.write

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.alishoumar.diaryapp.model.Diary
import com.alishoumar.diaryapp.model.GalleryState
import com.alishoumar.diaryapp.model.Mood
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import java.time.ZonedDateTime

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    uiState: UiState,
    galleryState:GalleryState,
    onImageSelect:(Uri) -> Unit,
    pagerState: PagerState,
    moodName: () -> String,
    onTitleChange:(String) -> Unit,
    onDescriptionChange:(String) -> Unit,
    onDeleteConfirmed:() -> Unit,
    onDateTimeUpdated: (ZonedDateTime) -> Unit,
    onBackPress: () -> Unit,
    onSaveClick:(Diary) -> Unit
) {

    LaunchedEffect(key1 =  uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }

    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = uiState.selectedDiary,
                moodName = moodName,
                onDeleteConfirmed = onDeleteConfirmed,
                onDateTimeUpdated = onDateTimeUpdated,
                onBackPress = onBackPress
            )
        },
        content = {
            WriteContent(
                uiState=uiState,
                onImageSelect = onImageSelect,
                galleryState = galleryState,
                pagerState = pagerState,
                title = uiState.title,
                onTitleChange = onTitleChange,
                description = uiState.description,
                onDescriptionChange = onDescriptionChange,
                onSavedClick = onSaveClick,
                paddingValues = it
            )
        }
    )
}

