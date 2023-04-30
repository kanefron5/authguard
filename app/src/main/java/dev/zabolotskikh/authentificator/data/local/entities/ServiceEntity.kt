package dev.zabolotskikh.authentificator.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_table")
data class ServiceEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "private_key") val privateKey: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
