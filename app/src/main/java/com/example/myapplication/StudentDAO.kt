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
    fun deleteStudentById(id: Int)

    @Query("update Student set LinkAvatar = :avt, FullName =:name, FirstName =:firstName, DateOfBirth= :dateOfBirth, YearOfBirth=:yearOfBirth,  Sex =:sex, Address=:address, Majors=:majors where id=:id")
    fun upDateInfor(
        id: Int,
        avt: String?,
        name: String,
        dateOfBirth: String,
        sex: Int,
        address: String,
        majors: String,
        yearOfBirth: Int,
        firstName: String
    )

    @Query("select * from Student order by FirstName ASC")
    fun sortByNameASC(): List<Student>

    @Query("select * from Student order by YearOfBirth DESC")
    fun sortByAgeASC(): List<Student>

    @Query("select * from Student where FirstName like :firstName")
    fun findByFirstName(firstName: String): List<Student>

    @Query("select * from Student where YearOfBirth=:yearOfBirth")
    fun findByAge(yearOfBirth: Int): List<Student>
}