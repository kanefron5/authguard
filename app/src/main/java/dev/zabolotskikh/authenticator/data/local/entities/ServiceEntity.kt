package dev.zabolotskikh.authenticator.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.zabolotskikh.authenticator.domain.model.GenerationMethod

@Entity(tableName = "service_table")
data class ServiceEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "private_key") val privateKey: String,
    @ColumnInfo(name = "generation_method") val generationMethod: GenerationMethod,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
