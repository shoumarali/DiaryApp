package com.alishoumar.diaryapp.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.alishoumar.diaryapp.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    authenticated: Boolean,
    loadingState: Boolean,
    messageBarState: MessageBarState,
    oneTapState: OneTapSignInState,
    onClick: () -> Unit,
    onTokenIdReceived:(String) -> Unit,
    onDialogDismissed:(String) -> Unit,
    navigateToHome:() -> Unit
){
    Scaffold (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
        content ={
           ContentWithMessageBar(messageBarState = messageBarState) {
               AuthenticationContent(
                   loadingState =  loadingState,
                   onButtonClick = onClick,
               )
           }
        }
    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.d("Auth", "AuthenticationScreen: $tokenId")
          onTokenIdReceived(tokenId)
        },
        onDialogDismissed = {message ->
            Log.d("Auth", "AuthenticationScreen: $message")
            onDialogDismissed(message)
        })

    LaunchedEffect(key1 = authenticated) {
        if(authenticated){
            navigateToHome()
        }
    }
}