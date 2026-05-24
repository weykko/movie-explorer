package ru.urfu.movie_explorer.feature.profile.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.urfu.movie_explorer.feature.profile.ProfileViewModel
import ru.urfu.movie_explorer.feature.profile.edit.EditProfileViewModel

val profileModule = module {
    viewModel { ProfileViewModel(observeUserProfile = get()) }
    viewModel {
        EditProfileViewModel(
            observeUserProfile = get(),
            updateUserProfile = get(),
            reminderScheduler = get(),
        )
    }
}
