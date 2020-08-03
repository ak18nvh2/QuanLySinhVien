package com.example.myapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile_student.*
import kotlinx.android.synthetic.main.dialog_comfirm.*
import kotlinx.android.synthetic.main.dialog_search.*
import java.lang.NumberFormatException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity(), View.OnClickListener,
    StudentAdapter.IAdapterWithActivity {
    companion object {
        var list: ArrayList<Student> = ArrayList()
        var mAdapter: StudentAdapter? = null
        val STUDENT = "STUDENT"
    }

    private fun loadData() {
        //Thread {
            list =
                AppDB.DataBase.getDataBase(this).studentDAO().getAllStudents() as ArrayList<Student>
            this.runOnUiThread(java.lang.Runnable {
                mAdapter?.setList(list)
            })
      //  }.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAdapter = StudentAdapter(this, this)
        var rv_ListStudent: RecyclerView = findViewById(R.id.rv_ListStudents)
        rv_ListStudent.layoutManager = LinearLayoutManager(this)
        rv_ListStudent.adapter = mAdapter
        loadData()
        btn_AddStudent.setOnClickListener(this)
        btn_SortByName.setOnClickListener(this)
        btn_SortByASCAge.setOnClickListener(this)
        btn_FindByName.setOnClickListener(this)
        btn_FindByAge.setOnClickListener(this)
        tv_Title.setOnClickListener(this)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v) {
            tv_Title -> loadData()
            btn_FindByName -> {
                val dialog = MaterialDialog(this)
                    .noAutoDismiss()
                    .customView(R.layout.dialog_search)

                dialog.tv_TitleSearchDialog.text = "Nhập tên muốn tìm :"
                dialog.btn_CancelDialogSearch.setOnClickListener() {
                    dialog.dismiss()
                }
                dialog.btn_AcceptDiaLogSearch.setOnClickListener() {
                    var firstName = "%" + dialog.edt_InPutSearch.text.toString() + "%"
                    Thread {
                        HomeActivity.list.clear()
                        HomeActivity.list = AppDB.DataBase.getDataBase(this).studentDAO()
                            .findByFirstName(firstName) as ArrayList<Student>
                        runOnUiThread(java.lang.Runnable {
                            mAdapter?.setList(list)
                        })
                    }.start()
                    dialog.dismiss()
                }
                dialog.show()
            }
            btn_SortByASCAge -> {
                Thread {
                    HomeActivity.list.clear()
                    HomeActivity.list = AppDB.DataBase.getDataBase(this).studentDAO()
                        .sortByAgeASC() as ArrayList<Student>
                    runOnUiThread(java.lang.Runnable {
                        mAdapter?.setList(list)
                    })
                }.start()
            }
            btn_AddStudent -> {
                val intent = Intent(this, ProfileStudentActivity::class.java)
                startActivity(intent)
            }
            btn_SortByName -> {
                Thread {
                    HomeActivity.list.clear()
                    HomeActivity.list = AppDB.DataBase.getDataBase(this).studentDAO()
                        .sortByNameASC() as ArrayList<Student>
                    runOnUiThread(java.lang.Runnable {
                        mAdapter?.setList(list)
                    })
                }.start()
            }
            btn_FindByAge -> {
                val dialog = MaterialDialog(this)
                    .noAutoDismiss()
                    .customView(R.layout.dialog_search)

                dialog.tv_TitleSearchDialog.text = "Nhập tuổi muốn tìm :"

                dialog.btn_CancelDialogSearch.setOnClickListener() {
                    dialog.dismiss()
                }

                dialog.btn_AcceptDiaLogSearch.setOnClickListener() {
                    if (dialog.edt_InPutSearch.text.toString()[0] == '-') {
                        Toast.makeText(this, "Nhập sai!", Toast.LENGTH_SHORT).show()
                    } else {
                        try {

                            var age = dialog.edt_InPutSearch.text.toString().toInt()
                            val current = LocalDateTime.now()
                            val formatter = DateTimeFormatter.ofPattern("yyyy")
                            val formatted = current.format(formatter).toInt()
                            var yearOfBirth = formatted - age
                            Thread {
                                HomeActivity.list.clear()
                                HomeActivity.list = AppDB.DataBase.getDataBase(this).studentDAO()
                                    .findByAge(yearOfBirth) as ArrayList<Student>
                                runOnUiThread(java.lang.Runnable {
                                    mAdapter?.setList(list)
                                })
                            }.start()
                            dialog.dismiss()
                        } catch (e: NumberFormatException) {
                            Toast.makeText(this, "Nhập sai!", Toast.LENGTH_LONG).show()
                        }
                    }

                }
                dialog.show()

            }
        }
    }

    override fun doSomeThingOnLongClick(student: Student) {
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
                HomeActivity.list.clear()
                HomeActivity.list = AppDB.DataBase.getDataBase(this).studentDAO()
                    .getAllStudents() as ArrayList<Student>
                this.runOnUiThread(java.lang.Runnable {
                    mAdapter?.setList(HomeActivity.list)
                    dialog.dismiss()
                    Toast.makeText(this, "Xóa thành công!", Toast.LENGTH_SHORT).show()
                })
            }.start()

        }
        dialog.show()


    }

    override fun doSomeThingOnClickAvatar(student: Student) {
        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.dialog_comfirm)

        dialog.tv_TitleOfCustomDialogConfirm.setText("Bạn có muốn sửa thông tin sinh viên này ?")
        dialog.btn_CancelDialogConfirm.setOnClickListener() {
            dialog.dismiss()
        }
        dialog.btn_AcceptDiaLogConFirm.setOnClickListener() {
            var intent = Intent(this, ProfileStudentActivity::class.java)
            var bundle = Bundle()
            bundle.putSerializable(STUDENT, student)
            intent.putExtras(bundle)
            intent.putExtra("BUTTON", 0)
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()

    }


}