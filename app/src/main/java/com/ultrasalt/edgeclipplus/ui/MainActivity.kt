package com.ultrasalt.edgeclipplus.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ultrasalt.edgeclipplus.service.ClipboardMonitorService
import com.ultrasalt.edgeclipplus.service.EdgeOverlayService
import com.ultrasalt.edgeclipplus.ui.theme.EdgeClipTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EdgeClipTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text("EdgeClip+ Control Panel", style = MaterialTheme.typography.headlineSmall)

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { requestOverlayPermission() }) {
                        Text("Grant Overlay Permission")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = { openAccessibilitySettings() }) {
                        Text("Enable Accessibility Service")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = { startServices() }) {
                        Text("Start Services")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(onClick = { stopServices() }) {
                        Text("Stop Services")
                    }
                }
            }
        }
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
        }
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    private fun startServices() {
        startForegroundService(Intent(this, ClipboardMonitorService::class.java))
        startService(Intent(this, EdgeOverlayService::class.java))
    }

    private fun stopServices() {
        stopService(Intent(this, ClipboardMonitorService::class.java))
        stopService(Intent(this, EdgeOverlayService::class.java))
    }
}
