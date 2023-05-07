package dev.zabolotskikh.authenticator.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zabolotskikh.authenticator.data.local.ServiceRoomDatabase
import dev.zabolotskikh.authenticator.data.repository.AppStateRepositoryImpl
import dev.zabolotskikh.authenticator.data.repository.ServiceRepositoryImpl
import dev.zabolotskikh.authenticator.domain.repository.AppStateRepository
import dev.zabolotskikh.authenticator.domain.repository.ServiceRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ServiceRoomDatabase {
        return ServiceRoomDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideServiceRepository(database: ServiceRoomDatabase): ServiceRepository {
        return ServiceRepositoryImpl(database.serviceDao())
    }

    @Provides
    @Singleton
    fun provideAppStateRepository(database: ServiceRoomDatabase): AppStateRepository {
        return AppStateRepositoryImpl(database.appStateDao())
    }
}