package com.gabriel.ribeiro.cademeupet.di

import com.gabriel.ribeiro.cademeupet.data.FirebaseInstances
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ModulesProvides {

    @Singleton
    @Provides
    fun providesFirebaseInstances() = FirebaseInstances
}