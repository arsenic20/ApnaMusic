package com.example.apnaMusic.hilt

import android.content.Context
import androidx.room.Room
import com.example.apnaMusic.networks.ApiService
import com.example.apnaMusic.networks.MusicRepository
import com.example.apnaMusic.roomDb.MyDao
import com.example.apnaMusic.roomDb.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.jamendo.com") // Replace with your API's base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MyDatabase::class.java,
            "my_database"
        ).build()
    }

    @Provides
    fun provideDao(database: MyDatabase): MyDao {
        return database.myDao()
    }

    @Provides
    fun provideMyRepository(apiService: ApiService): MusicRepository {
        return MusicRepository(apiService)
    }
}