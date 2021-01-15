package com.example.cobasqlite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_content.view.*

class ItemAdapter (val context: Context, val items: ArrayList<EmpModelClass>)
    : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name = view.tvName
        val email = view.tvEmail
        val ll_content = view.ll_content
        val update = view.iv_update
        val delete = view.iv_delete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_content, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        holder.name.text = item.name
        holder.email.text = item.email

        if(position % 2 == 0) {
            holder.ll_content.setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
        } else {
            holder.ll_content.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.update.setOnClickListener {
            if(context is MainActivity) {
                context.updateRecord(item)
            }
        }

        holder.delete.setOnClickListener {
            if(context is MainActivity) {
                context.deleteEmployee(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}