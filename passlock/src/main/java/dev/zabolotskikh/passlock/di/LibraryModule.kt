package dev.zabolotskikh.passlock.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.zabolotskikh.passlock.BuildConfig
import dev.zabolotskikh.passlock.data.PasscodeEncoderImpl
import dev.zabolotskikh.passlock.data.repository.PasscodeRepositoryImpl
import dev.zabolotskikh.passlock.domain.PasscodeEncoder
import dev.zabolotskikh.passlock.domain.repository.PasscodeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LibraryModule {
    @Provides
    @Singleton
    fun providePasscodeRepository(
        @ApplicationContext context: Context, passcodeEncoder: PasscodeEncoder
    ): PasscodeRepository = PasscodeRepositoryImpl(context, passcodeEncoder)

    @LibraryScope
    @Singleton
    @Provides
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideEncoder(): PasscodeEncoder = PasscodeEncoderImpl(BuildConfig.PASSWORD_SECRET)

}