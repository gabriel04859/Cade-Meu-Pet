package com.gabriel.ribeiro.cademeupet.di

import com.gabriel.ribeiro.cademeupet.repository.LoginAndRegisterRepositoryImplemented
import com.gabriel.ribeiro.cademeupet.repository.NewPostRepositoryImple
import com.gabriel.ribeiro.cademeupet.repository.contracts.LoginAndRegisterRepository
import com.gabriel.ribeiro.cademeupet.repository.contracts.NewPostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ModulesAbstracts {

    @Singleton
    @Binds
    abstract fun bindLoginAndRegisterRepository(loginAndRegisterRepositoryImplemented :
                                       LoginAndRegisterRepositoryImplemented)
    : LoginAndRegisterRepository

    @Singleton
    @Binds
    abstract fun bindNewPostViewModel(newPostRepositoryImple: NewPostRepositoryImple) : NewPostRepository
}