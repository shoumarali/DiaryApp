package com.alishoumar.diaryapp.navigation


import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alishoumar.diaryapp.presentation.components.DisplayAlertDialog
import com.alishoumar.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.alishoumar.diaryapp.presentation.screens.auth.AuthenticationViewModel
import com.alishoumar.diaryapp.presentation.screens.home.HomeScreen
import com.alishoumar.diaryapp.util.Constants.APP_ID
import com.alishoumar.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        homeRoute(navigateTorWriteScreen = {
            navController.navigate(Screen.Write.route)
        }, navigateToAuthScreen = {
            navController.popBackStack()
            navController.navigate(Screen.Authentication.route)
        })
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
//                oneTapState.open()
                viewModel.setLoading(true)
                viewModel.setAuthenticated(true)
                viewModel.setLoading(false)
                messageBarState.addSuccess("Successfully authenticated")
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

fun NavGraphBuilder.homeRoute(
    navigateTorWriteScreen: () -> Unit,
    navigateToAuthScreen:() -> Unit
) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpen by remember {
            mutableStateOf(false)
        }
        val scope  = rememberCoroutineScope()
        HomeScreen(
            drawerState = drawerState,
            onMenuClick = {
                scope.launch {
                    drawerState.open()
                }
            },
            onSignOutClicked = {
                               signOutDialogOpen = true
            },
            navigateToWriteScreen = navigateTorWriteScreen)

        DisplayAlertDialog(
            title ="Sign Out",
            message ="Are you sure you want to sign Out from Google Account?",
            dialogOpened = signOutDialogOpen,
            onCloseDialog = { signOutDialogOpen = false },
            onYesClicked = {
                scope.launch (Dispatchers.IO) {
                    val user= App.create(APP_ID).currentUser
                    if(user != null){
                        user.logOut()
                        withContext(Dispatchers.Main){
                            navigateToAuthScreen()
                        }
                    }
                    withContext(Dispatchers.Main){
                        navigateToAuthScreen()
                    }
                }
            }
        )
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