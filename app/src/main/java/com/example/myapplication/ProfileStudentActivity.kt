package com.example.myapplication

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import kotlinx.android.synthetic.main.activity_profile_student.*
import java.util.*

class ProfileStudentActivity : AppCompatActivity(), View.OnClickListener {
    var datePickerDialog: DatePickerDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_student)
        img_SelectDate.setOnClickListener(this)
        edt_InPutDateOfBirth.inputType = InputType.TYPE_NULL
    }

    override fun onClick(v: View?) {
        when (v) {
            img_SelectDate -> {
                val calendar = Calendar.getInstance()
                var day = calendar.get(Calendar.DAY_OF_MONTH)
                var month = calendar.get(Calendar.MONTH)
                var year = calendar.get(Calendar.YEAR)

                datePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        edt_InPutDateOfBirth.setText(dayOfMonth.toString() + "/" + (month + 1) + "/" + year)
                    },
                    year,
                    month,
                    day
                )
                datePickerDialog?.show()
            }
        }
    }
}