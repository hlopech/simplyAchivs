package com.example.simplyachivs.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simplyachivs.data.dao.AsceticismDao
import com.example.simplyachivs.data.dao.AwardDao
import com.example.simplyachivs.data.dao.EventDao
import com.example.simplyachivs.data.dao.GoalDao
import com.example.simplyachivs.data.dao.StepDao
import com.example.simplyachivs.data.dao.TaskDao
import com.example.simplyachivs.data.dao.UserDao
import com.example.simplyachivs.data.dao.UserProgressDao
import com.example.simplyachivs.data.entity.AsceticismCheckInEntity
import com.example.simplyachivs.data.entity.AsceticismEntity
import com.example.simplyachivs.data.entity.AwardEntity
import com.example.simplyachivs.data.entity.EventEntity
import com.example.simplyachivs.data.entity.GoalEntity
import com.example.simplyachivs.data.entity.StepEntity
import com.example.simplyachivs.data.entity.TaskEntity
import com.example.simplyachivs.data.entity.UserEntity
import com.example.simplyachivs.data.entity.UserProgressEntity
import com.example.simplyachivs.data.entity.converters.Converters

@Database(
    entities =
        [UserProgressEntity::class,
            UserEntity::class,
            TaskEntity::class,
            StepEntity::class,
            GoalEntity::class,
            AwardEntity::class,
            EventEntity::class,
            AsceticismEntity::class,
            AsceticismCheckInEntity::class],
    version = 5,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userProgressDao(): UserProgressDao
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun stepDao(): StepDao
    abstract fun goalDao(): GoalDao
    abstract fun awardDao(): AwardDao
    abstract fun eventDao(): EventDao
    abstract fun asceticismDao(): AsceticismDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "simply_achieves_database"
                ).fallbackToDestructiveMigration(false).build().also { INSTANCE = it }
            }
        }
    }


}