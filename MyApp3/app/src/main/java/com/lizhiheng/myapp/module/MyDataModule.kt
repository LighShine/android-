package com.lizhiheng.myapp.module

import android.content.Context
import androidx.room.Room
import com.lizhiheng.myapp.data_room.MyDataDao
import com.lizhiheng.myapp.data_room.MyDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MyDataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(
            context = context,
            MyDatabase::class.java, "mydata.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMyDao(db:MyDatabase): MyDataDao {
        return db.myDataDao()
    }
}