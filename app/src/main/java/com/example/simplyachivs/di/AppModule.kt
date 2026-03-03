package com.example.simplyachivs.di

import android.content.Context
import androidx.room.Room
import com.example.simplyachivs.data.dao.AwardDao
import com.example.simplyachivs.data.dao.GoalDao
import com.example.simplyachivs.data.dao.StepDao
import com.example.simplyachivs.data.dao.TaskDao
import com.example.simplyachivs.data.dao.UserDao
import com.example.simplyachivs.data.dao.UserProgressDao
import com.example.simplyachivs.data.database.AppDatabase
import com.example.simplyachivs.data.repositoryImpl.AwardRepositoryImpl
import com.example.simplyachivs.data.repositoryImpl.GoalRepositoryImpl
import com.example.simplyachivs.data.repositoryImpl.SessionRepositoryImpl
import com.example.simplyachivs.data.repositoryImpl.TaskRepositoryImpl
import com.example.simplyachivs.data.repositoryImpl.UserProgressRepositoryImpl
import com.example.simplyachivs.data.repositoryImpl.UserRepositoryImpl
import com.example.simplyachivs.domain.repository.AwardRepository
import com.example.simplyachivs.domain.repository.GoalRepository
import com.example.simplyachivs.domain.repository.SessionRepository
import com.example.simplyachivs.domain.repository.TaskRepository
import com.example.simplyachivs.domain.repository.UserProgressRepository
import com.example.simplyachivs.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "simply_achieves_database")
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideGoalDao(db: AppDatabase): GoalDao = db.goalDao()

    @Provides
    fun provideStepDao(db: AppDatabase): StepDao = db.stepDao()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideUserProgressDao(db: AppDatabase): UserProgressDao = db.userProgressDao()

    @Provides
    fun provideAwardDao(db: AppDatabase): AwardDao = db.awardDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    abstract fun bindAwardRepository(impl: AwardRepositoryImpl): AwardRepository

    @Binds
    abstract fun bindGoalRepository(impl: GoalRepositoryImpl): GoalRepository

    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindUserProgressRepository(impl: UserProgressRepositoryImpl): UserProgressRepository
}
