package com.example.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(Student::class)], version = 3)
abstract class AppDB : RoomDatabase() {
    abstract fun studentDAO(): StudentDAO

    object DataBase {
        private var INSTANCE: AppDB? = null
        fun getDataBase(mContext: Context): AppDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(mContext, AppDB::class.java, "QuanLySinhVienDB")
                    .fallbackToDestructiveMigration().build()
            }
            return (INSTANCE as AppDB)

        }
    }
}