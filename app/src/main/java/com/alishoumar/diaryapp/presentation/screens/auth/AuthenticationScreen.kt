package com.alishoumar.diaryapp.presentation.screens.auth

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.alishoumar.diaryapp.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    messageBarState: MessageBarState,
    oneTapState: OneTapSignInState,
    onClick: () -> Unit
){
    Scaffold (
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
            messageBarState.addSuccess("Successfully Authenticated")
        },
        onDialogDismissed = {message ->
            Log.d("Auth", "AuthenticationScreen: $message")
            messageBarState.addError(Exception(message))
        })
}