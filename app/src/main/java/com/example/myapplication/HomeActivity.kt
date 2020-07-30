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
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_comfirm.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity(), View.OnClickListener,
    StudentAdapter.IAdapterWithActivity {
    private var list: ArrayList<Student> = ArrayList()
    private var mAdapter: StudentAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAdapter = StudentAdapter(this,this)
        var rv_ListStudent: RecyclerView = findViewById(R.id.rv_ListStudents)
        rv_ListStudent.layoutManager = LinearLayoutManager(this)
//        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//        val formatted = current.format(formatter)
        rv_ListStudent.adapter = mAdapter
        Thread {
            list =
                AppDB.DataBase.getDataBase(this).studentDAO().getAllStudents() as ArrayList<Student>
            this.runOnUiThread(java.lang.Runnable {
                mAdapter?.setList(list)
            })
        }.start()
        btn_AddStudent.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_AddStudent -> {
                val intent = Intent(this, ProfileStudentActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun doSomeThing(student: Student) {
        //create dialog
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.dialog_comfirm)

        dialog.tv_TitleOfCustomDialogConfirm.setText("Bạn có chắc chắn xóa sinh viên này ?")
        dialog.btn_CancelDialogConfirm.setOnClickListener() {
            dialog.dismiss()
        }
        dialog.btn_AcceptDiaLogConFirm.setOnClickListener() {
            Thread {
                AppDB.DataBase.getDataBase(this).studentDAO().deleteStudentById(student.id)
                this.list.clear()
                this.list = AppDB.DataBase.getDataBase(this).studentDAO()
                    .getAllStudents() as ArrayList<Student>
                this.runOnUiThread(java.lang.Runnable {
                    mAdapter?.setList(this.list)
                    dialog.dismiss()
                    Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show()
                })
            }.start()

        }
        dialog.show()


    }


}