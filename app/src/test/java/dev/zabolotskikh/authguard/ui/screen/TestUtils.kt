@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.zabolotskikh.authguard.ui.screen

import dev.zabolotskikh.authguard.data.ChangelogRepositoryImpl
import dev.zabolotskikh.authguard.data.TestAppStateRepositoryImpl
import dev.zabolotskikh.authguard.data.TestOtpRepositoryImpl
import dev.zabolotskikh.authguard.data.TestServiceRepositoryImpl
import dev.zabolotskikh.authguard.domain.repository.AppStateRepository
import dev.zabolotskikh.authguard.domain.repository.ChangelogRepository
import dev.zabolotskikh.authguard.domain.repository.OtpRepository
import dev.zabolotskikh.authguard.domain.repository.ServiceRepository
import dev.zabolotskikh.authguard.ui.screen.services.ServiceViewModel
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsViewModel
import dev.zabolotskikh.authguard.ui.screen.welcome.WelcomeViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

fun getServiceViewModelInstance(
    appStateRepository: AppStateRepository = TestAppStateRepositoryImpl(),
    serviceRepository: ServiceRepository = TestServiceRepositoryImpl(),
    otpRepository: OtpRepository = TestOtpRepositoryImpl(serviceRepository),
    dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
) = ServiceViewModel(
    serviceRepository, appStateRepository, otpRepository, dispatcher
)

fun getSettingsViewModelInstance(
    appStateRepository: AppStateRepository = TestAppStateRepositoryImpl(),
    serviceRepository: ServiceRepository = TestServiceRepositoryImpl(),
    changelogRepository: ChangelogRepository = ChangelogRepositoryImpl(),
    dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
) = SettingsViewModel(appStateRepository, serviceRepository, changelogRepository, dispatcher)

fun getWelcomeViewModelInstance(
    appStateRepository: AppStateRepository = TestAppStateRepositoryImpl(),
    dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
) = WelcomeViewModel(appStateRepository, dispatcher)
