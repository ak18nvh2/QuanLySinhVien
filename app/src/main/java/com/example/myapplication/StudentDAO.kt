package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDAO {
    @Insert()
    fun saveStudent(student: Student)

    @Query("select * from Student")
    fun getAllStudents(): List<Student>

    @Query("delete from Student where id = :id")
    fun deleteStudentById(id : Int)
}