package dev.zabolotskikh.authguard.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.zabolotskikh.authguard.data.local.entities.AppStateEntity
import dev.zabolotskikh.authguard.data.local.entities.PasscodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PasscodeDao {
    @Query("SELECT * FROM app_state_table where id=0")
    fun getPasscode(): Flow<PasscodeEntity?>

    @Query("UPDATE app_state_table set passcode_hash = :hash, last_authorized_timestamp = :timestamp  where id=0")
    fun updateState(hash: String, timestamp: Long)

    @Query("UPDATE app_state_table set passcode_hash = NULL, last_authorized_timestamp = NULL where id=0")
    fun resetPasscode()
}