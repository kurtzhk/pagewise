package com.pagewisegroup.pagewise

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, "PagewiseDB", null, 1){

    // Creates the tables for our database. Assignments, classes, students,
    // and a join table between students and classes.
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ASSIGNMENTS(" +
                "assignment_id INT PRIMARY KEY," +
                "name TEXT" +
                "class_id INT NOT NULL," + //links assignments to their class.
                "due_date INT NOT NULL," + //will be stored in unix epoch time.
                "page_start INT NOT NULL," +
                "page_send INT NOT NULL," +
                "estimated_ttc INT)") /*unsure if this is needed, might decide to store reading
                                        speed in student table and just dynamically calculate
                                        ttc at runtime. need feedback. */
        db?.execSQL("CREATE TABLE IF NOT EXISTS CLASSES(" +
                "class_id INT PRIMARY KEY," +
                "name TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS STUDENTS(" +
                "student_id INT PRIMARY KEY," +
                "last_name TEXT," + //do we need to store names? not sure if it's helpful.
                "first_name TEXT," +
                "read_speed REAL)") //pages per hour
        // join table facilitates many to many relationship of classes and students.
        db?.execSQL("CREATE TABLE IF NOT EXISTS ENROLLMENTS(" +
                "enroll_id INT PRIMARY KEY," +
                "student_id INT NOT NULL," +
                "class_id INT NOT NULL)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun recordAssignment(assignment: Assignment) {
        TODO("function to add assignment record to db")
    }

    fun recordClass(pwClass: PWClass) {
        TODO("function to add class record to db")
    }

    fun recordStudent(student: Student) {
        TODO("function to add student record to db")
    }
}