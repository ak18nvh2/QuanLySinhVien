package com.example.myapplication

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile_student.*
import kotlinx.android.synthetic.main.dialog_comfirm.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ProfileStudentActivity : AppCompatActivity(), View.OnClickListener {
    var datePickerDialog: DatePickerDialog? = null
    private val REQUEST_SELECT_IMAGE = 1
    private var BUTTON_TYPE = 1
    private var student: Student = Student("", "", "", "", 0, 1,"","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_student)
        init()
    }

    fun init() {
        edt_InPutDateOfBirth.isEnabled = false
        img_SelectDate.setOnClickListener(this)
        edt_InPutDateOfBirth.inputType = InputType.TYPE_NULL
        btn_SelectAvatar.setOnClickListener(this)
        btn_SaveInfor.setOnClickListener(this)
        btn_Back.setOnClickListener(this)
        var intent = intent
        BUTTON_TYPE = intent.getIntExtra("BUTTON", 1)
        var bundle = intent.extras
        if (bundle != null) {
            this.student = bundle.getSerializable(HomeActivity.STUDENT) as Student
            img_AvatarProfile.setImageURI(Uri.parse(student.avt))
            edt_InPutName.setText(student.name)
            edt_InPutDateOfBirth.setText((student.dateOfBirth))
            edt_InPutAddress.setText(student.address)
            edt_InPutMajors.setText(student.majors)
            if (student.sex == 1) {
                rb_Nam.isChecked = true
            } else {
                rb_Nu.isChecked = true
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun selectImage(view: View) {
        val i = Intent(
            Intent.ACTION_OPEN_DOCUMENT,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(i, REQUEST_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                this.student.avt = data?.getData().toString()
                img_AvatarProfile.setImageURI(Uri.parse(this.student.avt))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onClick(v: View?) {
        when (v) {
            btn_Back -> {
                finish()
            }
            img_SelectDate -> {
                val calendar = Calendar.getInstance()
                var day = calendar.get(Calendar.DAY_OF_MONTH)
                var month = calendar.get(Calendar.MONTH)
                var year = calendar.get(Calendar.YEAR)

                datePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        edt_InPutDateOfBirth.setText(dayOfMonth.toString() + "/" + (month + 1) + "/" + year)
                        this.student.yearOfBirth = year
                    },
                    year,
                    month,
                    day
                )
                datePickerDialog?.show()
            }
            btn_SelectAvatar -> selectImage(btn_SelectAvatar)
            btn_SaveInfor -> {
                //create dialog
                if (BUTTON_TYPE == 1) {
                    val dialog = MaterialDialog(this)
                        .noAutoDismiss()
                        .customView(R.layout.dialog_comfirm)


                    dialog.btn_AcceptDiaLogConFirm.setOnClickListener() {
                        this.student.name = edt_InPutName.text.toString()
                        var i=this.student.name.length - 1
                        while (this.student.name[i-1] != ' ' && i>0){
                            i--;
                        }
                        this.student.firstName = this.student.name.substring(i,this.student.name.length)
                        this.student.dateOfBirth = edt_InPutDateOfBirth.text.toString()
                        if (rb_Nam.isChecked) {
                            this.student.sex = 1
                        } else {
                            this.student.sex = 0
                        }
                        this.student.address = edt_InPutAddress.text.toString()
                        this.student.majors = edt_InPutMajors.text.toString()
                        Thread {
                            try {
                                AppDB.DataBase.getDataBase(this).studentDAO()
                                    .saveStudent(this.student)
                                HomeActivity.list.clear()
                                HomeActivity.list = AppDB.DataBase.getDataBase(this).studentDAO()
                                    .getAllStudents() as ArrayList<Student>
                                this.runOnUiThread(java.lang.Runnable {

                                    HomeActivity.mAdapter?.setList(HomeActivity.list)
                                    Toast.makeText(
                                        this,
                                        "Lưu thành công !",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog.dismiss()
                                    finish()
                                })
                            } catch (e: Exception) {
                                e.printStackTrace()
                                this.runOnUiThread(java.lang.Runnable {
                                    Toast.makeText(this, "Lỗi !", Toast.LENGTH_SHORT).show()
                                })
                            }
                        }.start()
                    }
                    dialog.btn_CancelDialogConfirm.setOnClickListener() {
                        dialog.dismiss()
                    }

                    when {
                        edt_InPutAddress.text.toString() == "" -> {
                            Toast.makeText(
                                this,
                                "Không được để trống địa chỉ!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        this.student.avt == "" -> {
                            Toast.makeText(
                                this,
                                "Không được để trống avatar!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        edt_InPutDateOfBirth.text.toString() == "" -> {
                            Toast.makeText(this, "Cần chọn ngày sinh!", Toast.LENGTH_SHORT).show()
                        }
                        edt_InPutMajors.text.toString() == "" -> {
                            Toast.makeText(
                                this,
                                "Không được để trống chuyên ngành!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        edt_InPutName.text.toString() == "" -> {
                            Toast.makeText(this, "Không được để trống tên!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        else -> {
                            dialog.show()
                        }
                    }
                } else {
                    val dialog = MaterialDialog(this)
                        .noAutoDismiss()
                        .customView(R.layout.dialog_comfirm)


                    dialog.btn_AcceptDiaLogConFirm.setOnClickListener() {

                        this.student.name = edt_InPutName.text.toString()
                        var i=this.student.name.length - 1
                        while (this.student.name[i-1] != ' ' && i>0){
                            i--;
                        }
                        this.student.firstName = this.student.name.substring(i,this.student.name.length)
                        this.student.dateOfBirth = edt_InPutDateOfBirth.text.toString()
                        if (rb_Nam.isChecked) {
                            this.student.sex = 1
                        } else {
                            this.student.sex = 0
                        }
                        this.student.address = edt_InPutAddress.text.toString()
                        this.student.majors = edt_InPutMajors.text.toString()
                        Thread {
                            try {
                                AppDB.DataBase.getDataBase(this).studentDAO()
                                    .upDateInfor(student.id,student.avt,student.name,student.dateOfBirth,student.sex,student.address,student.majors,this.student.yearOfBirth,this.student.firstName)
                                HomeActivity.list.clear()
                                HomeActivity.list = AppDB.DataBase.getDataBase(this).studentDAO()
                                    .getAllStudents() as ArrayList<Student>
                                this.runOnUiThread(java.lang.Runnable {

                                    HomeActivity.mAdapter?.setList(HomeActivity.list)
                                    Toast.makeText(
                                        this,
                                        "Lưu thành công !",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog.dismiss()
                                    finish()
                                })
                            } catch (e: Exception) {
                                e.printStackTrace()
                                this.runOnUiThread(java.lang.Runnable {
                                    Toast.makeText(this, "Lỗi !", Toast.LENGTH_SHORT).show()
                                })
                            }
                        }.start()
                    }
                    dialog.btn_CancelDialogConfirm.setOnClickListener() {
                        dialog.dismiss()
                    }

                    when {
                        edt_InPutAddress.text.toString() == "" -> {
                            Toast.makeText(
                                this,
                                "Không được để trống địa chỉ!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        this.student.avt == "" -> {
                            Toast.makeText(
                                this,
                                "Không được để trống avatar!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        edt_InPutDateOfBirth.text.toString() == "" -> {
                            Toast.makeText(this, "Cần chọn ngày sinh!", Toast.LENGTH_SHORT).show()
                        }
                        edt_InPutMajors.text.toString() == "" -> {
                            Toast.makeText(
                                this,
                                "Không được để trống chuyên ngành!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        edt_InPutName.text.toString() == "" -> {
                            Toast.makeText(this, "Không được để trống tên!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        else -> {
                            dialog.show()
                        }
                    }
                }
            }

        }
    }
}