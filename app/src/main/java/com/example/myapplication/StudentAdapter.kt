package com.example.myapplication

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(var mContext: Context,var iAdapterWithActivity: IAdapterWithActivity) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {
    private var list = ArrayList<Student>()

    fun setList(list: ArrayList<Student>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avt: ImageView = itemView.findViewById(R.id.img_Avatar)
        val name: TextView = itemView.findViewById(R.id.tv_Name)
        val dateOfBirth: TextView = itemView.findViewById(R.id.tv_DateOfBirth)
        val sex: TextView = itemView.findViewById(R.id.tv_Sex)
        val address: TextView = itemView.findViewById(R.id.tv_Address)
        val majors: TextView = itemView.findViewById(R.id.tv_Majors)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.item_list_student, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.avt.setImageURI(Uri.parse(list[position].avt))
        holder.address.text = "Địa chỉ: " + list[position].address
        holder.dateOfBirth.text = "Ngày sinh: " + list[position].dateOfBirth
        holder.majors.text = "Chuyên ngành: " + list[position].majors
        holder.name.text = "Họ tên: " + list[position].name
        if (list[position].sex == 1) holder.sex.text = "| Giới tính: Nam"
        else holder.sex.text = "| Giới tính: Nữ"

        holder.itemView.setOnLongClickListener {
            iAdapterWithActivity.doSomeThingOnLongClick(list[position])
            return@setOnLongClickListener true
        }
        holder.avt.setOnClickListener(){
            iAdapterWithActivity.doSomeThingOnClickAvatar(list[position])
        }

    }
    interface IAdapterWithActivity{
        fun doSomeThingOnLongClick(student: Student)
        fun doSomeThingOnClickAvatar(student: Student)
    }
}