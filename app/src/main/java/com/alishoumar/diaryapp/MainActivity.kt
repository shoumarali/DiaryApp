package com.alishoumar.diaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.alishoumar.diaryapp.navigation.Screen
import com.alishoumar.diaryapp.navigation.SetUpNavGraph
import com.alishoumar.diaryapp.ui.theme.DiaryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DiaryAppTheme {
                val navController = rememberNavController()
                SetUpNavGraph(startDestination = Screen.Authentication.route, navController = navController)
            }
        }
    }
}
