package dev.zabolotskikh.authenticator.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_state_table")
data class AppStateEntity(
    @ColumnInfo(name = "isStarted") val isStarted: Boolean,
    @ColumnInfo(name = "isAuthenticated") val isAuthenticated: Boolean,
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
)
