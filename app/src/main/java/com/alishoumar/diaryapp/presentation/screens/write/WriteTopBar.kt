package com.alishoumar.diaryapp.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.alishoumar.diaryapp.model.Diary
import com.alishoumar.diaryapp.presentation.components.DisplayAlertDialog
import com.alishoumar.diaryapp.util.toInstant
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedDiary : Diary?,
    moodName: () -> String,
    onDeleteConfirmed: () -> Unit,
    onBackPress: () -> Unit
) {
    val currentDate by remember { mutableStateOf(LocalDate.now()) }
    val currentTime by remember { mutableStateOf(LocalTime.now()) }

    val formattedDate = remember(key1 = currentDate){
        DateTimeFormatter.ofPattern("dd MMM YYYY").format(currentDate).uppercase()
    }

    val formattedTime = remember (key1 = currentTime){
        DateTimeFormatter.ofPattern("hh:mm a").format(currentTime)
    }

    val selectedDiaryDateTime = remember(selectedDiary) {
        if(selectedDiary != null) {
            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(Date.from(selectedDiary.date.toInstant())).uppercase()
        }else{
            "Unknown"
        }
    }
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Arrow Icon"
                )
            }
        },
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = moodName(),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                    )
                Text(modifier = Modifier.fillMaxWidth(),
                    text = if(selectedDiary != null) selectedDiaryDateTime
                    else "$formattedDate,$formattedTime",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                    )
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date Icon"
                )
            }
            if(selectedDiary != null){
                DeleteDiaryAction(selectedDiary = selectedDiary,
                    onDeleteConfirmed = onDeleteConfirmed)
            }
        }

    )
}

@Composable
fun DeleteDiaryAction(
    selectedDiary: Diary?,
    onDeleteConfirmed:() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var openedDialog by remember { mutableStateOf(false) }
    
    DropdownMenu(expanded = expanded,
        onDismissRequest = { expanded = false })
    {
        DropdownMenuItem(text = {
            Text(text = "Delete") },
            onClick = {
                expanded = false
                openedDialog = true
            })
    }

    DisplayAlertDialog(
        title = "Delete",
        message = "Are you sure you want to permanently delete this diary note ${selectedDiary?.title}",
        dialogOpened = openedDialog,
        onCloseDialog = { openedDialog = false },
        onYesClicked = onDeleteConfirmed)
    
    IconButton(onClick = { expanded = !expanded }) {
        Icon(imageVector = Icons.Default.MoreVert,
            contentDescription = "Over flow menu icon",
            tint = MaterialTheme.colorScheme.onSurface
            )
    }
}