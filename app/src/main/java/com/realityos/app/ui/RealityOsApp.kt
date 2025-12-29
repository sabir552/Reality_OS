// app/src/main/java/com/realityos/app/ui/RealityOsApp.kt
package com.realityos.app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*

object Routes {
    const val Onboarding = "onboarding"
    const val Home = "home"
    const val Rules = "rules"
    const val Modes = "modes"
    const val History = "history"
}

@Composable
fun RealityOsApp(
    state: RootState,
    onAction: (RootAction) -> Unit
) {
    val nav = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
        NavHost(
            navController = nav,
            startDestination = Routes.Onboarding
        ) {
            composable(Routes.Onboarding) {
                com.realityos.app.ui.screens.OnboardingScreen(
                    state = state,
                    onAction = onAction,
                    onContinue = {
                        nav.navigate(Routes.Home) {
                            popUpTo(Routes.Onboarding) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.Home) {
                com.realityos.app.ui.screens.HomeScreen(state, nav)
            }
            composable(Routes.Rules) {
                com.realityos.app.ui.screens.RulesScreen(nav)
            }
            composable(Routes.Modes) {
                com.realityos.app.ui.screens.ModesScreen(state, nav, onPurchaseElite = {
                    onAction(RootAction.PurchaseElite)
                })
            }
            composable(Routes.History) {
                com.realityos.app.ui.screens.HistoryScreen(nav)
            }
        }
    }
}
