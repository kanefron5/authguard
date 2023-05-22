package dev.zabolotskikh.authguard.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.domain.model.Passcode

data class PasscodeEntity(
    @ColumnInfo(name = "last_authorized_timestamp") val lastAuthorizedTimestamp: Long,
    @ColumnInfo(name = "passcode_hash") val passcodeHash: String,
)

fun PasscodeEntity.toPasscode(): Passcode = Passcode(
    this.lastAuthorizedTimestamp,
    this.passcodeHash
)
