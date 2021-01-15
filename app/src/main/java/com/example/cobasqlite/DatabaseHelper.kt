package com.example.cobasqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "EmployeeDatabase"
        private const val TABLE_NAME = "EmployeeTable"

        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE "+ TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    //insert data to database
    fun addEmployee(emp: EmpModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_EMAIL, emp.email)

        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }

    //read data
    fun viewEmployee(): ArrayList<EmpModelClass> {
        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val query = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: SQLException) {
            db.execSQL(query)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))

                val emp = EmpModelClass(id = id, name = name, email = email)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    //delete data
    fun deleteEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)

        val success = db.delete(TABLE_NAME, KEY_ID + "=" + emp.id, null)
        db.close()
        return success
    }

    //update data
    fun updateEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_EMAIL, emp.email)

        val success = db.update(TABLE_NAME, contentValues, KEY_ID + "=" + emp.id, null)
        db.close()
        return success
    }


}