package com.lizhiheng.myapp.module

import android.app.Application
import androidx.room.Room
import com.lizhiheng.myapp.image_room.MyImagesDao
import com.lizhiheng.myapp.image_room.MyImagesDataBase

import com.lizhiheng.myapp.repository.ImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object MyImageModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): MyImagesDataBase {
        return Room.databaseBuilder(app, MyImagesDataBase::class.java, "image_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMyImagesDao(db: MyImagesDataBase): MyImagesDao {
        return db.myImagesDao()
    }
}
