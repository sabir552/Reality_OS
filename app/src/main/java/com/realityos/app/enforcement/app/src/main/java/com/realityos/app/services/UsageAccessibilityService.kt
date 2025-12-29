// app/src/main/java/com/realityos/app/services/UsageAccessibilityService.kt
package com.realityos.app.services

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class UsageAccessibilityService : AccessibilityService() {
    @Volatile var currentPackage: String? = null
        private set

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            currentPackage = event.packageName?.toString()
        }
    }
    override fun onInterrupt() = Unit
}
