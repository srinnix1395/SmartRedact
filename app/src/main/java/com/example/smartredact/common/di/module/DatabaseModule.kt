package com.example.smartredact.common.di.module

//import android.content.Context
//import androidx.room.Room
//import com.okprobe.printer.common.constant.Constant
//import com.okprobe.printer.data.local.database.AppDatabase
//import com.okprobe.printer.data.local.database.dao.RecentSearchDao
//import dagger.Module
//import dagger.Provides
//import javax.inject.Singleton
//
//@Module
//class DatabaseModule {
//
//    private val roomDB: AppDatabase
//
//    constructor(context: Context){
//        roomDB = Room.databaseBuilder(context, AppDatabase::class.java, Constant.Database.DATABASE_NAME).build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideRecentSearchDao(): RecentSearchDao {
//        return roomDB.recentSearchDao()
//    }
//}