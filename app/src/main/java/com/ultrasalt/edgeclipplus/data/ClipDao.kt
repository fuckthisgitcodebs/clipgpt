package com.ultrasalt.edgeclipplus.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClip(clip: ClipEntity): Long

    @Query("SELECT * FROM clips ORDER BY pinned DESC, timestamp DESC LIMIT :limit")
    fun getRecentClips(limit: Int = 200): Flow<List<ClipEntity>>

    @Query("DELETE FROM clips WHERE pinned = 0")
    suspend fun clearUnpinned()

    @Query("DELETE FROM clips")
    suspend fun clearAll()

    @Query("UPDATE clips SET pinned = :pinned WHERE id = :id")
    suspend fun setPinned(id: Long, pinned: Boolean)

    @Query("UPDATE clips SET favorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Long, favorite: Boolean)

    @Query("SELECT * FROM clips WHERE content LIKE '%' || :query || '%' ORDER BY pinned DESC, timestamp DESC")
    fun search(query: String): Flow<List<ClipEntity>>
}
