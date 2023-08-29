package com.gw.study.frp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gw.study.frp.ui.screen.LessonsScreen
import com.gw.study.frp.ui.screen.lesson.calculator.CalculatorScreen
import com.gw.study.frp.ui.screen.lesson.clearfield.ClearFieldScreen
import com.gw.study.frp.ui.screen.lesson.fromvalidation.FormValidationScreen
import com.gw.study.frp.ui.screen.lesson.reservation.ReservationScreen
import com.gw.study.frp.ui.screen.lesson.translate.TranslateScreen
import com.gw.study.frp.ui.screen.route.Routes
import com.gw.study.frp.ui.theme.FRPWithFlowStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FRPWithFlowStudyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FRPWithFlowStudyApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FRPWithFlowStudyApp() {

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("FRP Lessons", style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    if (currentDestination?.route != Routes.Home.route) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .clickable { navController.popBackStack() },
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
         },
    ) { paddingValues ->

        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Routes.Home.route
        ) {

            composable(route = Routes.Home.route) {
                LessonsScreen {
                    navController.navigate(it.route)
                }
            }

            composable(route = Routes.Lesson1.route) {
                ReservationScreen()
            }

            composable(route = Routes.Lesson2.route) {
                ClearFieldScreen()
            }

            composable(route = Routes.Lesson3.route) {
                TranslateScreen()
            }

            composable(route = Routes.Lesson4.route) {
                CalculatorScreen()
            }

            composable(route = Routes.Lesson5.route) {
                FormValidationScreen()
            }

        }
    }
}