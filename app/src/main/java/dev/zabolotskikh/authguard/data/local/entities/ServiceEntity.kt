package dev.zabolotskikh.authguard.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.zabolotskikh.authguard.domain.model.GenerationMethod
import dev.zabolotskikh.authguard.domain.model.Service

@Entity(tableName = "service_table")
data class ServiceEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "private_key") val privateKey: String,
    @ColumnInfo(name = "generation_method") val generationMethod: GenerationMethod,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)

fun ServiceEntity.toService(): Service = Service(
    name = name,
    privateKey = privateKey,
    id = id,
    generationMethod = generationMethod,
    isFavorite = isFavorite
)