package com.example.cobasqlite

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_delete.*
import kotlinx.android.synthetic.main.dialog_update.*
import kotlinx.android.synthetic.main.item_content.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupData()

        btnAddRecord.setOnClickListener {
            addRecord()
        }
    }

    private fun setupData() {
        if(getItemList().size > 0) {
            rv_record.visibility = View.VISIBLE
            tvNoRecord.visibility = View.GONE

            rv_record.layoutManager = LinearLayoutManager(this)
            val adapter = ItemAdapter(this, getItemList())
            rv_record.adapter = adapter
        } else {
            rv_record.visibility = View.GONE
            tvNoRecord.visibility = View.VISIBLE
        }
    }

    private fun getItemList(): ArrayList<EmpModelClass> {
        val database = DatabaseHelper(this)
        return database.viewEmployee()
    }

    fun addRecord() {
        val name = et_name.text.toString()
        val email = et_email.text.toString()
        val databaseHandler = DatabaseHelper(this)

        if(!name.isEmpty() && !email.isEmpty()) {
            val status = databaseHandler.addEmployee(EmpModelClass(0, name, email))
            if(status > 1) {
                Toast.makeText(applicationContext, "Record Saved", Toast.LENGTH_SHORT).show()
                et_name.text.clear()
                et_email.text.clear()
                setupData()
            }
        } else {
            Toast.makeText(applicationContext, " Name or Email cannot be blank", Toast.LENGTH_SHORT).show()
        }

    }

    fun updateRecord(empModel: EmpModelClass) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_update)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.et_updateName.setText(empModel.name)
        dialog.et_updateEmail.setText(empModel.email)

        dialog.tv_update.setOnClickListener {
            val name = dialog.et_updateName.text.toString()
            val email = dialog.et_updateEmail.text.toString()

            val databaseHandler = DatabaseHelper(this)

            if(!name.isEmpty() && !email.isEmpty()) {
                val status = databaseHandler.updateEmployee(EmpModelClass(empModel.id, name, email))
                if(status > -1) {
                    Toast.makeText(applicationContext, "Record Updated", Toast.LENGTH_SHORT).show()
                    setupData()
                    dialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext, " Name or Email cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.tv_cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun deleteEmployee(emp: EmpModelClass) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setMessage("Are you sure wants to delete ${emp.name}")
        builder.setIcon(R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->
            val database = DatabaseHelper(this)
            val status = database.deleteEmployee(EmpModelClass(emp.id, "", ""))

            if(status > -1) {
                Toast.makeText(applicationContext, "Record deleted successfully", Toast.LENGTH_SHORT).show()
                setupData()
            }
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }
}
