package com.ultrasalt.edgeclipplus.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "clips",
    indices = [
        Index(value = ["hash"], unique = true),
        Index(value = ["timestamp"])
    ]
)
data class ClipEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val content: String,
    val type: String,
    val timestamp: Long,
    val pinned: Boolean = false,
    val favorite: Boolean = false,
    val sourceApp: String? = null,
    val hash: String
)
