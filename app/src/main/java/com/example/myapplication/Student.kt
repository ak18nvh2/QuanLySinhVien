package com.example.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Student (@ColumnInfo(name = "FullName")
                    var name: String,
                    @ColumnInfo(name = "LinkAvatar")
                    var avt : String?,
                    @ColumnInfo(name = "DateOfBirth")
                    var dateOfBirth : String,
                    @ColumnInfo(name = "Sex")
                    var sex : Int,
                    @ColumnInfo(name = "Address")
                    var address : String,
                    @ColumnInfo(name = "Majors")
                    var majors : String){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}