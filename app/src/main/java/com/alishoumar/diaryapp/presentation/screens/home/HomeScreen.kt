package com.alishoumar.diaryapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.alishoumar.diaryapp.R
import com.alishoumar.diaryapp.data.respository.Diaries
import com.alishoumar.diaryapp.model.RequestState


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun HomeScreen(
    diaries: Diaries,
    drawerState: DrawerState,
    onMenuClick: () -> Unit,
    onSignOutClicked: () -> Unit,
    navigateToWriteScreen:() -> Unit,
    navigateToWriteWithArgs: (String) -> Unit
){
    var padding by remember { mutableStateOf(PaddingValues()) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
   NavigationDrawer(
       drawerState = drawerState,
       onSignOutClicked = onSignOutClicked
   ) {
       Scaffold (
           modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
           topBar = {
               HomeTopBar(scrollBehavior = scrollBehavior ,onMenuClick = onMenuClick)
           },
           floatingActionButton = {
               FloatingActionButton(
                   modifier = Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                   onClick = navigateToWriteScreen) {
                   Icon(
                       imageVector = Icons.Default.Edit,
                       contentDescription = "New Diary Icon"
                   )
               }
           },
           content = {
               paddingValues ->
               padding = paddingValues
                   when(diaries){
                       is RequestState.Success ->{
                           HomeContent(
                               paddingValues = paddingValues,
                               diaryNotes = diaries.data,
                               onClick = navigateToWriteWithArgs
                           )
                       }
                       is RequestState.Error ->{
                           EmptyPage(title = "Error" , subTitle = "${diaries.error.message}")
                       }
                       is RequestState.Loading ->{
                           EmptyPage(title = "Loading" , subTitle = "Please wait")
                       }
                       else -> {}
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
}