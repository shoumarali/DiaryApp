package com.alishoumar.diaryapp.navigation


import android.util.Log
import android.widget.Toast
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alishoumar.diaryapp.data.respository.MongoDB
import com.alishoumar.diaryapp.model.GalleryImage
import com.alishoumar.diaryapp.model.Mood
import com.alishoumar.diaryapp.presentation.components.DisplayAlertDialog
import com.alishoumar.diaryapp.presentation.screens.auth.AuthenticationScreen
import com.alishoumar.diaryapp.presentation.screens.auth.AuthenticationViewModel
import com.alishoumar.diaryapp.presentation.screens.home.HomeScreen
import com.alishoumar.diaryapp.presentation.screens.home.HomeViewModel
import com.alishoumar.diaryapp.presentation.screens.write.WriteScreen
import com.alishoumar.diaryapp.presentation.screens.write.WriteViewModel
import com.alishoumar.diaryapp.util.Constants.APP_ID
import com.alishoumar.diaryapp.util.Constants.WRITE_SCREEN_ARGUMENT_KEY
import com.alishoumar.diaryapp.model.RequestState
import com.alishoumar.diaryapp.model.rememberGalleryState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded: () -> Unit
){

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authenticationRoute(navigateToHome = {
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        },
            onDataLoaded = onDataLoaded)
        homeRoute(navigateTorWriteScreen = {
            navController.navigate(Screen.Write.route)
        },
            navigateToAuthScreen = {
            navController.popBackStack()
            navController.navigate(Screen.Authentication.route)

        }, onDataLoaded = onDataLoaded
            , navigateToWriteWithArgs = {
                navController.navigate(Screen.Write.passDiaryId(diaryId = it))
        })
        writeRoute(onBackPress = {
            navController.popBackStack()
        })
    }
}

fun NavGraphBuilder.authenticationRoute(
    navigateToHome: () -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Authentication.route){
        val oneTapState = rememberOneTapSignInState()
        val messageBarState= rememberMessageBarState()
        val viewModel: AuthenticationViewModel = viewModel()
        val loadingState by viewModel.loadingState
        val authenticated by viewModel.authenticated

        LaunchedEffect(key1 = Unit) {
            onDataLoaded()
        }
        
        AuthenticationScreen(
            authenticated = authenticated,
            loadingState = loadingState,
            messageBarState =  messageBarState,
            oneTapState = oneTapState,
            onClick = {
//                oneTapState.open()
                viewModel.setLoading(true)
                      viewModel.signInWithMongoAnonymous(
                          onSuccess = {
                              messageBarState.addSuccess("Successfully authenticated")
                              viewModel.setLoading(false)
                          },
                          onError = {
                              messageBarState.addError(it)
                              viewModel.setLoading(false)
                          }
                      )
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
    navigateToWriteWithArgs:(String) -> Unit,
    navigateToAuthScreen:() -> Unit,
    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        var signOutDialogOpen by remember { mutableStateOf(false) }
        val viewModel:HomeViewModel = viewModel()
        val diaries by viewModel.diaries
        val scope  = rememberCoroutineScope()

        LaunchedEffect(key1 = diaries) {
            if(diaries !is RequestState.Loading){
                onDataLoaded()
            }
        }

        HomeScreen(
            diaries= diaries,
            drawerState = drawerState,
            onMenuClick = {
                scope.launch {
                    drawerState.open()
                }
            },
            onSignOutClicked = {
                               signOutDialogOpen = true
            },
            navigateToWriteScreen = navigateTorWriteScreen,
            navigateToWriteWithArgs = navigateToWriteWithArgs)

        LaunchedEffect(key1 = Unit) {
            MongoDB.configureTheRealm()
        }

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

@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeRoute(onBackPress:() -> Unit){


    composable(route = Screen.Write.route,
        arguments = listOf(navArgument(name = WRITE_SCREEN_ARGUMENT_KEY) {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        })
    ){
        val viewModel : WriteViewModel = viewModel()
        val uiState = viewModel.uiState
        val pagerState = rememberPagerState()
        val context = LocalContext.current
        val pageNumber by remember { derivedStateOf{pagerState.currentPage}}

        val  galleryState = viewModel.galleryState

        WriteScreen (
            uiState = uiState,
            galleryState = galleryState,
            onImageSelect = {
                val type = context.contentResolver.getType(it)?.split("/")?.last() ?: "jpg"
                Log.d("tag", "writeRoute: $it")
                            viewModel.addImage(image = it, imageType = "jpeg")
            },
            pagerState = pagerState,
            moodName = { Mood.entries[pageNumber].name},
            onDeleteConfirmed = {viewModel.deleteDiary(
                onSuccess = {
                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()
                    onBackPress()
                },
                onError = {message ->
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
                }
            )},
            onDateTimeUpdated = {
                viewModel.updateDateAndTime(zoneDateTime = it)
            },
            onBackPress = onBackPress,
            onTitleChange = {viewModel.setTitle(title = it)},
            onDescriptionChange = { viewModel.setDescription(description = it) },
            onSaveClick = {
                viewModel.upsertDiary(
                    diary = it.apply { mood = Mood.entries[pageNumber].name },
                    onSuccess = { onBackPress() },
                    onError = {message ->
                        Toast.makeText(
                            context,message,Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }
}