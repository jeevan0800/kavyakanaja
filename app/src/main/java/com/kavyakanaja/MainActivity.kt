package com.kavyakanaja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.kavyakanaja.data.PoemRepository
import com.kavyakanaja.ui.screens.PoemOfTheDayScreen
import com.kavyakanaja.ui.screens.AllPoemsScreen
import com.kavyakanaja.ui.screens.PoetsCornerScreen
import com.kavyakanaja.ui.theme.KavyaKanajaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val repository = PoemRepository(this)
        val poemOfTheDay = repository.getPoemOfTheDay()

        setContent {
            KavyaKanajaTheme {
                val navController = rememberNavController()
                val items = listOf(
                    Screen.PoemOfDay,
                    Screen.AllPoems,
                    Screen.PoetsCorner
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = { 
                                        val icon = when(screen.route) {
                                            "poemOfDay" -> Icons.Filled.Home
                                            "allPoems" -> Icons.Filled.List
                                            else -> Icons.Filled.Person
                                        }
                                        Icon(
                                            imageVector = icon, 
                                            contentDescription = null 
                                        ) 
                                    },
                                    label = { Text(screen.title) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.PoemOfDay.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.PoemOfDay.route) {
                            PoemOfTheDayScreen(poem = poemOfTheDay)
                        }
                        composable(Screen.AllPoems.route) {
                            val allPoems = repository.getPoems()
                            AllPoemsScreen(poems = allPoems, onPoemClick = { poemId ->
                                navController.navigate("poemDetail/$poemId")
                            })
                        }
                        composable(Screen.PoetsCorner.route) {
                            PoetsCornerScreen()
                        }
                        composable(
                            "poemDetail/{poemId}",
                            arguments = listOf(navArgument("poemId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val poemId = backStackEntry.arguments?.getInt("poemId")
                            val selectedPoem = poemId?.let { repository.getPoemById(it) }
                            PoemOfTheDayScreen(poem = selectedPoem)
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String, val title: String) {
    object PoemOfDay : Screen("poemOfDay", "Poem of the Day")
    object AllPoems : Screen("allPoems", "All Poems")
    object PoetsCorner : Screen("poetsCorner", "Poet's Corner")
}
