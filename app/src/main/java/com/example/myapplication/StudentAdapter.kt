package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(var mContext: Context) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {
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
        holder.avt.setImageResource(list[position].avt)
        holder.address.text = list[position].address
        holder.dateOfBirth.text = list[position].dateOfBirth.toString()
        holder.majors.text = list[position].majors
        holder.name.text = list[position].name
        if (list[position].sex == 1) holder.sex.text = "| Giới tính: Nam"
        else holder.sex.text = "| Giới tính: Nữ"
    }
}