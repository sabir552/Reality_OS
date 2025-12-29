// app/src/main/java/com/realityos/app/ui/MainActivity.kt
package com.realityos.app.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.realityos.app.billing.BillingManager
import com.realityos.app.ui.theme.RealityOsTheme

class MainActivity : ComponentActivity(), BillingManager.Listener {

    private val vm: RootViewModel by viewModels()
    private lateinit var billing: BillingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        billing = BillingManager(this, this).also { it.start() }

        setContent {
            RealityOsTheme {
                val state by vm.state.collectAsState()
                RealityOsApp(
                    state = state,
                    onAction = ::handleAction
                )
            }
        }
    }

    private fun handleAction(action: RootAction) {
        when (action) {
            RootAction.RequestUsagePermission ->
                startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            RootAction.RequestAccessibilityPermission ->
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            RootAction.RequestOverlayPermission ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    startActivity(
                        Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:$packageName")
                        )
                    )
                }
            RootAction.PurchaseElite ->
                billing.purchaseElite(this as Activity)
        }
    }

    override fun onElitePurchased() {
        vm.onElitePurchased()
    }

    override fun onBillingError() {
        // MVP: no-op / you can show a snackbar
    }
}
