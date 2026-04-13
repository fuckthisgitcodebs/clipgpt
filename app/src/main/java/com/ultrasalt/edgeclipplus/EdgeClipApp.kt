package com.ultrasalt.edgeclipplus

import android.app.Application
import com.ultrasalt.edgeclipplus.data.ClipDatabase

class EdgeClipApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ClipDatabase.init(this)
    }
}
