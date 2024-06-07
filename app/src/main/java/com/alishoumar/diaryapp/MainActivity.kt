package com.alishoumar.diaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.alishoumar.diaryapp.navigation.Screen
import com.alishoumar.diaryapp.navigation.SetUpNavGraph
import com.alishoumar.diaryapp.ui.theme.DiaryAppTheme
import com.alishoumar.diaryapp.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DiaryAppTheme {
                val navController = rememberNavController()
                SetUpNavGraph(
                    startDestination = Screen.Authentication.route,
                    navController = navController)
            }
        }
    }
}

private fun getStartDestination(): String{
    val user = App.create(APP_ID).currentUser
    return if(user != null && user.loggedIn) Screen.Home.route
    else Screen.Authentication.route
}
