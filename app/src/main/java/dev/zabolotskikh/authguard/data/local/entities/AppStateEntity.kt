package dev.zabolotskikh.authguard.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.zabolotskikh.authguard.domain.model.AppState

@Entity(tableName = "app_state_table")
data class AppStateEntity(
    @ColumnInfo(name = "is_started") val isStarted: Boolean,
    @ColumnInfo(name = "is_remote_mode") val isRemoteMode: Boolean,
    @ColumnInfo(name = "is_private_mode") val isPrivateMode: Boolean,
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
)

fun AppStateEntity?.toAppState(): AppState = AppState(
    isStarted = this?.isStarted ?: false,
    isRemoteMode = this?.isRemoteMode ?: false,
    isPrivateMode = this?.isPrivateMode ?: false,
)
