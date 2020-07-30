package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity(),View.OnClickListener {
    private var list : ArrayList<Student> = ArrayList()
    private var mAdapter : StudentAdapter? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAdapter = StudentAdapter(this)
        var rv_ListStudent : RecyclerView = findViewById(R.id.rv_ListStudents)
        rv_ListStudent.layoutManager = LinearLayoutManager(this)
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter)
        for ( i in 1..12){
            list.add(Student("Họ tên: Nguyễn Trung Minh Hiếu",R.mipmap.ic_launcher_round, "Ngày sinh: " +formatted,
                i%2,"Địa chỉ: Sơn Đồng, Hoài Đức, Hà Nội","Ngành: Công nghệ thông tin"))
        }
        mAdapter?.setList(list)
        rv_ListStudent.adapter = mAdapter
        btn_AddStudent.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            btn_AddStudent -> {
                val intent = Intent(this, ProfileStudentActivity::class.java)
                startActivity(intent)
            }
        }
    }


}