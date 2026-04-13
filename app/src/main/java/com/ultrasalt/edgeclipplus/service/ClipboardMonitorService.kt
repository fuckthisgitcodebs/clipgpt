package com.ultrasalt.edgeclipplus.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ultrasalt.edgeclipplus.R
import com.ultrasalt.edgeclipplus.data.ClipDatabase
import com.ultrasalt.edgeclipplus.data.ClipEntity
import com.ultrasalt.edgeclipplus.util.ClipClassifier
import com.ultrasalt.edgeclipplus.util.HashUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ClipboardMonitorService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var clipboardManager: ClipboardManager
    private lateinit var listener: ClipboardManager.OnPrimaryClipChangedListener

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        startForeground(1001, buildNotification())

        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        listener = ClipboardManager.OnPrimaryClipChangedListener {
            val clipData = clipboardManager.primaryClip ?: return@OnPrimaryClipChangedListener
            if (clipData.itemCount <= 0) return@OnPrimaryClipChangedListener

            val item = clipData.getItemAt(0)
            val text = item.coerceToText(this)?.toString() ?: return@OnPrimaryClipChangedListener
            val clean = text.trim()

            if (clean.isEmpty()) return@OnPrimaryClipChangedListener
            if (clean.length > 10000) return@OnPrimaryClipChangedListener

            scope.launch {
                val type = ClipClassifier.classify(clean)
                val hash = HashUtil.sha256(clean)

                val entity = ClipEntity(
                    content = clean,
                    type = type,
                    timestamp = System.currentTimeMillis(),
                    pinned = false,
                    favorite = false,
                    sourceApp = null,
                    hash = hash
                )

                ClipDatabase.get().clipDao().insertClip(entity)
            }
        }

        clipboardManager.addPrimaryClipChangedListener(listener)
    }

    override fun onDestroy() {
        if (::clipboardManager.isInitialized && ::listener.isInitialized) {
            clipboardManager.removePrimaryClipChangedListener(listener)
        }
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "edgeclip_clipboard",
                "EdgeClip Clipboard Monitor",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = "Keeps clipboard history active"
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, "edgeclip_clipboard")
            .setContentTitle("EdgeClip+ Running")
            .setContentText("Clipboard monitoring active")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .build()
    }
}
