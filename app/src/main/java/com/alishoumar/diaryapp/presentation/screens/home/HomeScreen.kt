package com.alishoumar.diaryapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alishoumar.diaryapp.R
import com.alishoumar.diaryapp.model.Diary
import com.alishoumar.diaryapp.model.Mood
import com.alishoumar.diaryapp.presentation.components.DiaryHolder
import io.realm.kotlin.ext.realmListOf


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    drawerState: DrawerState,
    onMenuClick: () -> Unit,
    onSignOutClicked: () -> Unit,
    navigateToWriteScreen:() -> Unit
){

   NavigationDrawer(
       drawerState = drawerState,
       onSignOutClicked = onSignOutClicked
   ) {
       Scaffold (
           topBar = {
               HomeTopBar(onMenuClick = onMenuClick)
           },
           floatingActionButton = {
               FloatingActionButton(onClick = navigateToWriteScreen) {
                   Icon(
                       imageVector = Icons.Default.Edit,
                       contentDescription = "New Diary Icon"
                   )
               }
           },
           content = {
               paddingValues ->
               Surface (
                   modifier = Modifier.padding(paddingValues)
               ){
                   
               }
           }
       )
   }
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
){
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
          ModalDrawerSheet {

                  Box(
                      modifier = Modifier
                          .fillMaxWidth()
                          .height(250.dp),
                      contentAlignment = Center
                  ){
                      Image(
                          painter = painterResource(id = R.drawable.logo),
                          contentDescription = "Logo Image"
                      )
                  }
                  NavigationDrawerItem(
                      label = {
                          Row (modifier = Modifier.padding(horizontal = 12.dp)) {
                              Image(
                                  painter = painterResource(id = R.drawable.google_logo),
                                  contentDescription ="Google Logo"
                              )
                              Spacer(modifier = Modifier.width(12.dp))
                              Text(
                                  text = "Sign Out",
                                  color = MaterialTheme.colorScheme.onSurface
                              )
                          }
                      },
                      selected =false,
                      onClick = onSignOutClicked
                  )
          }
        },
        content = content
    )
}

@Preview
@Composable
private fun prevHomeScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    HomeScreen(drawerState = drawerState, onMenuClick = { }, onSignOutClicked = {}) {

    }
}