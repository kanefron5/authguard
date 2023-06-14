package dev.zabolotskikh.authguard.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zabolotskikh.authguard.data.local.ServiceRoomDatabase
import dev.zabolotskikh.authguard.data.repository.AppStateRepositoryImpl
import dev.zabolotskikh.authguard.data.repository.AuthRepositoryImpl
import dev.zabolotskikh.authguard.data.repository.ChangelogRepositoryImpl
import dev.zabolotskikh.authguard.data.repository.OtpRepositoryImpl
import dev.zabolotskikh.authguard.data.repository.ServiceRepositoryImpl
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.AuthRepository
import dev.zabolotskikh.authguard.domain.repository.ChangelogRepository
import dev.zabolotskikh.authguard.domain.repository.OtpRepository
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
    fun provideAppStateRepository(dataStore: DataStore<Preferences>): AppStateRepository {
        return AppStateRepositoryImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideChangelogRepository(@ApplicationContext context: Context): ChangelogRepository {
        return ChangelogRepositoryImpl(context.assets)
    }

    @Provides
    @Singleton
    fun provideOtpRepository(serviceRepository: ServiceRepository): OtpRepository {
        return OtpRepositoryImpl(serviceRepository)
    }

    @Provides
    @Singleton
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(produceFile = {
            context.preferencesDataStoreFile("authguard")
        })

    @Singleton
    @Provides
    fun provideFirebaseAuth(@ApplicationContext context: Context): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository = AuthRepositoryImpl(auth)
}