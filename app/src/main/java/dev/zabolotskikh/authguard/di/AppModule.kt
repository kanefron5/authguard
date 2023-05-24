package dev.zabolotskikh.authguard.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zabolotskikh.authguard.data.local.ServiceRoomDatabase
import dev.zabolotskikh.authguard.data.repository.AppStateRepositoryImpl
import dev.zabolotskikh.authguard.data.repository.OtpRepositoryImpl
import dev.zabolotskikh.authguard.data.repository.PasscodeRepositoryImpl
import dev.zabolotskikh.authguard.data.repository.ServiceRepositoryImpl
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.OtpRepository
import dev.zabolotskikh.authguard.domain.repository.PasscodeRepository
import dev.zabolotskikh.authguard.domain.repository.ServiceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

    @Provides
    @Singleton
    fun provideOtpRepository(serviceRepository: ServiceRepository): OtpRepository {
        return OtpRepositoryImpl(serviceRepository)
    }

    @Provides
    @Singleton
    fun providePasscodeRepository(appStateRepository: AppStateRepository): PasscodeRepository {
        return PasscodeRepositoryImpl(appStateRepository)
    }

    @Singleton
    @Provides
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.IO

}