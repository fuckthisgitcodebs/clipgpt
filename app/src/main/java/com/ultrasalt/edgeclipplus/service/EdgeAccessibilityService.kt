package com.ultrasalt.edgeclipplus.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class EdgeAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // This is where we later detect:
        // - user focused a text field
        // - user clicked paste menu
        // - user copied text in another app
        //
        // We can also implement gesture triggers or auto-fill logic.

        val pkg = event.packageName?.toString() ?: "unknown"
        val cls = event.className?.toString() ?: "unknown"

        Log.d("EdgeClipAccessibility", "Event=${event.eventType} pkg=$pkg cls=$cls")
    }

    override fun onInterrupt() {
        // Required override
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("EdgeClipAccessibility", "Accessibility connected")
    }
}
