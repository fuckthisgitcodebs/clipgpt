package com.ultrasalt.edgeclipplus.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ultrasalt.edgeclipplus.service.ClipboardMonitorService
import com.ultrasalt.edgeclipplus.service.EdgeOverlayService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != "android.intent.action.BOOT_COMPLETED") return

        context.startForegroundService(Intent(context, ClipboardMonitorService::class.java))
        context.startService(Intent(context, EdgeOverlayService::class.java))
    }
}
