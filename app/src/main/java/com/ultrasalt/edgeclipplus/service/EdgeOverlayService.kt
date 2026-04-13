package com.ultrasalt.edgeclipplus.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kotlin.math.abs

class EdgeOverlayService : Service() {

    private lateinit var wm: WindowManager
    private var edgeView: View? = null

    private var startX = 0f
    private var startY = 0f
    private var triggered = false

    override fun onCreate() {
        super.onCreate()

        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        createEdgeSwipeZone()
    }

    private fun createEdgeSwipeZone() {
        val v = View(this)

        val layoutType =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE

        val params = WindowManager.LayoutParams(
            30, // swipe strip width px-ish (we can scale later)
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.END or Gravity.TOP
        params.x = 0
        params.y = 0

        v.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.rawX
                    startY = event.rawY
                    triggered = false
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - startX
                    val dy = event.rawY - startY

                    if (!triggered && abs(dx) > 150 && abs(dy) < 200) {
                        triggered = true
                        openPanel()
                    }
                    true
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> true
                else -> false
            }
        }

        edgeView = v
        wm.addView(v, params)
    }

    private fun openPanel() {
        val intent = Intent(this, OverlayPanelActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onDestroy() {
        edgeView?.let {
            wm.removeView(it)
        }
        edgeView = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
