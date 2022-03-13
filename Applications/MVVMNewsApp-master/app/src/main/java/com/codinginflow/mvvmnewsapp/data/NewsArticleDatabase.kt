package com.codinginflow.mvvmnewsapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Abstract class:: ---> Because room later generates the code for this class
 * Why not make it Interface:: ---> This class need to extend the room-database and interface cannot extend the class so we make class abstract
 */
@Database(entities = [NewsArticle::class,BreakingNews::class], version = 1)
abstract class NewsArticleDatabase : RoomDatabase() {

    abstract fun newsArticleDao() : NewsArticleDao

}