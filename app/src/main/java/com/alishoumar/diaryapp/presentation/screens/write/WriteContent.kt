package com.alishoumar.diaryapp.presentation.screens.write

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alishoumar.diaryapp.model.Diary
import com.alishoumar.diaryapp.model.GalleryState
import com.alishoumar.diaryapp.model.Mood
import com.alishoumar.diaryapp.presentation.components.GalleryUploader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WriteContent(
    uiState: UiState,
    galleryState: GalleryState,
    onImageSelect:(Uri) -> Unit,
    pagerState: PagerState,
    title:String,
    onTitleChange:(String) -> Unit,
    description:String,
    onDescriptionChange:(String) -> Unit,
    onSavedClick:(Diary) -> Unit,
    paddingValues: PaddingValues) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }
    
    Column (
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp),

        verticalArrangement = Arrangement.SpaceBetween

    ){
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            HorizontalPager(
                state = pagerState ,
                count = Mood.entries.size
            ) {
                page ->
                AsyncImage(
                    modifier = Modifier.size(120.dp),
                    model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(Mood.entries[page].icon)
                    .crossfade(true)
                    .build(),
                    contentDescription = "Mood Image")
            }
            Spacer(modifier = Modifier.height(30.dp))



            TextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Title") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    focusedPlaceholderColor =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(

                            onNext = {
                                scope.launch {

                                    scrollState.scrollTo(Int.MAX_VALUE)
                                }

                                focusManager.moveFocus(FocusDirection.Down)
                            }
                ),
                maxLines = 1,
                singleLine = true
            )

            TextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Tell me about it") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    focusedPlaceholderColor =  MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                    }
                )
            )
        }

        Column (
            verticalArrangement = Arrangement.Bottom
        ){
            Spacer(modifier = Modifier.height(12.dp))
            GalleryUploader(
                galleryState = galleryState,
                onAddClick = {
                             focusManager.clearFocus()
                },
                onImageSelect = onImageSelect,
                onImageClicked = {}
                )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                onClick = {
                          if(uiState.title.isNotEmpty() && uiState.description.isNotEmpty()){
                              onSavedClick(
                                  Diary().apply {
                                      this.title = uiState.title
                                      this.description = uiState.description
                                      this.images = galleryState.images.map { it.remoteImagePath }.toRealmList()

                                  }
                              )
                          } else {
                              Toast
                                  .makeText(context,"Fields cannot be empty",Toast.LENGTH_SHORT)
                                  .show()
                          }
                },
                shape = Shapes().small
                ) {
                Text(text = "Save")
            }
        }
    }
}