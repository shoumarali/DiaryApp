package com.alishoumar.diaryapp.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alishoumar.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.alishoumar.diaryapp.presentation.screens.auth.AuthenticationViewModel
import com.alishoumar.diaryapp.util.Constants.APP_ID
import com.alishoumar.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.launch

@Composable
fun SetUpNavGraph(startDestination: String, navController: NavHostController){

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationRoute(navigateToHome = {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        })
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(navigateToHome:() -> Unit) {
    composable(route = Screen.Authentication.route){
        val oneTapState = rememberOneTapSignInState()
        val messageBarState= rememberMessageBarState()
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val authenticated by viewModel.authenticated

        AuthenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            messageBarState =  messageBarState,
            oneTapState = oneTapState,
            onClick = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onTokenIdReceived = {tokenId ->
                viewModel.signInWithMongoAtlas(tokenId = tokenId,
                    onSuccess = {
                            messageBarState.addSuccess("Successfully authenticated")
                            viewModel.setLoading(false)
                    },
                    onError ={
                        messageBarState.addError(it)
                        viewModel.setLoading(false)
                    } )
            },
            onDialogDismissed = {
                messageBarState.addError(Exception(""))
            },
            navigateToHome = navigateToHome)
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = Screen.Home.route){
        val scope = rememberCoroutineScope()
        Column(
            modifier =  Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           Button(onClick = {
               scope.launch {
                   App.create(APP_ID).currentUser?.logOut()
               }
           }) {
               Text(text = "LogOut")
           }
        }
    }
}

fun NavGraphBuilder.writeRoute(){
    composable(route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ){

    }
}