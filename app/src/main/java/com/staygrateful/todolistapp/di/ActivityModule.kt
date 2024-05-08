package com.staygrateful.todolistapp.di

import com.staygrateful.todolistapp.domain.interactor.EditpageInteractor
import com.staygrateful.todolistapp.domain.interactor.HomepageInteractor
import com.staygrateful.todolistapp.domain.usecase.EditpageUseCase
import com.staygrateful.todolistapp.domain.usecase.HomepageUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Dagger Hilt module for providing dependencies related to activities.
 *
 * This Dagger module is responsible for providing dependencies related to activities,
 * such as UseCases for the home page and edit page interactions.
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityModule {

    /**
     * Binds the HomepageUseCase interface to the HomepageInteractor implementation.
     *
     * This method binds the HomepageUseCase interface to the HomepageInteractor implementation,
     * allowing the use of HomepageUseCase in activities.
     *
     * @param interactor The implementation of HomepageInteractor.
     * @return An instance of HomepageUseCase.
     */
    @Binds
    abstract fun bindHomeUseCase(interactor: HomepageInteractor): HomepageUseCase

    /**
     * Binds the EditpageUseCase interface to the EditpageInteractor implementation.
     *
     * This method binds the EditpageUseCase interface to the EditpageInteractor implementation,
     * allowing the use of EditpageUseCase in activities.
     *
     * @param interactor The implementation of EditpageInteractor.
     * @return An instance of EditpageUseCase.
     */
    @Binds
    abstract fun bindEditUseCase(interactor: EditpageInteractor): EditpageUseCase
}
