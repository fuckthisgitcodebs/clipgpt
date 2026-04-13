package com.ultrasalt.edgeclipplus.service

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ultrasalt.edgeclipplus.data.ClipDatabase
import com.ultrasalt.edgeclipplus.ui.theme.EdgeClipTheme
import kotlinx.coroutines.flow.Flow

class OverlayPanelActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )

        setContent {
            EdgeClipTheme {
                val dao = ClipDatabase.get().clipDao()
                val clipsFlow: Flow<List<com.ultrasalt.edgeclipplus.data.ClipEntity>> =
                    dao.getRecentClips(100)

                val clips by clipsFlow.collectAsState(initial = emptyList())

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                        .clickable { finish() }
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .width(320.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "EdgeClip+",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(clips) { clip ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                ) {
                                    Text(
                                        text = clip.type,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Text(
                                        text = clip.content.take(250),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
