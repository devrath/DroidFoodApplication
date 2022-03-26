package com.demo.mvvmnewsapp.di

import android.app.Application
import androidx.room.Room
import com.demo.mvvmnewsapp.api.NewsApi
import com.demo.mvvmnewsapp.data.NewsArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * fallbackToDestructiveMigration : ---> This will help in scenario where you alter the schema, say you change the column name
 *         And migration is not provided, then it will instruct room to automatically delete the old schema and create new one
 * **************************
 * SCOPING: ---> SingletonComponent :: Here we provide the instances with @Singleton ---> scope so that the single instance is provided throughout the app
 *          ---> ActivityComponent  :: Here we provide the instance with @ActivityScoped ---> scope so the single instance is maintained but per individual activity
 *
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi =
        retrofit.create(NewsApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): NewsArticleDatabase =
        Room.databaseBuilder(app, NewsArticleDatabase::class.java, "news_article_database")
            .fallbackToDestructiveMigration()
            .build()

}