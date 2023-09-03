package dev.zabolotskikh.auth.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zabolotskikh.auth.data.repository.AuthRepositoryImpl
import dev.zabolotskikh.authguard.domain.repository.AuthRepository
import dev.zabolotskikh.authguard.domain.repository.DataValidator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LibraryModule {

    @LibraryScope
    @Singleton
    @Provides
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideFirebaseAuth(@ApplicationContext context: Context): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository = AuthRepositoryImpl(auth)

    @Singleton
    @Provides
    @PasswordValidator
    fun providePasswordValidator(): DataValidator<String> = dev.zabolotskikh.auth.data.repository.PasswordValidator()

    @Singleton
    @Provides
    @EmailValidator
    fun provideEmailValidator(): DataValidator<String> = dev.zabolotskikh.auth.data.repository.EmailValidator()
}