package dev.zabolotskikh.authentificator.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.zabolotskikh.authentificator.data.local.entities.AppStateEntity
import dev.zabolotskikh.authentificator.data.local.entities.ServiceEntity
import dev.zabolotskikh.authentificator.domain.model.AppState
import kotlinx.coroutines.flow.Flow

@Dao
interface AppStateDao {
    @Query("SELECT * FROM app_state_table where id=0")
    fun getState(): Flow<AppStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateState(appState: AppStateEntity)

    @Query("DELETE FROM app_state_table where id=0")
    fun resetState()
}