package com.pagewisegroup.pagewise

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import kotlin.collections.ArrayList

@RunWith(JUnit4::class)
class StudentControllerTest : TestCase() {
    lateinit var context: Context

    @Before
    //gets context
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    //calculates reading speeds with no class
    fun testCalculateReadingSpeedsNoClass() {
        val studentCont = StudentController(context,Student("name",0L))
        studentCont.student.readingSpeed = ArrayList()
        studentCont.student.classes = ArrayList()
        studentCont.calculateReadingSpeeds()
        assertThat(studentCont.student.readingSpeed).isEqualTo(ArrayList<Double>())
    }

    @Test
    //calculates reading speeds with class
    fun testCalculateReadingSpeedsClass() {
        val studentCont = StudentController(context,Student("name",0L))
        studentCont.student.readingSpeed = ArrayList()
        studentCont.student.classes = ArrayList()
        val assign = Assignment("test",Date(122,2,25),0,1000)
        assign.progress.addSession(ReadingSession(0,25,
            System.currentTimeMillis()-301000000, System.currentTimeMillis()-300000000
        ))
        val classes = PWClass("test", arrayListOf(assign),0L)
        studentCont.student.classes = arrayListOf(classes)
        studentCont.student.classes = arrayListOf(classes)
        studentCont.calculateReadingSpeeds()
        assertThat(studentCont.student.readingSpeed[0]).isEqualTo(1.5)
    }

    @Test
    //calculates reading speed for a class that doesn't exist
    fun testCalculateReadingSpeedNoClass() {
        val studentCont = StudentController(context,Student("name",0L))
        studentCont.student.readingSpeed = ArrayList()
        studentCont.calculateReadingSpeed("null")
        assertThat(studentCont.student.readingSpeed).isEqualTo(ArrayList<Double>())
    }

    @Test
    //calculates reading speed with class/assign/progress
    fun testCalculateReadingSpeedClass() {
        val studentCont = StudentController(context,Student("name",0L))
        studentCont.student.readingSpeed = ArrayList()
        studentCont.student.classes = ArrayList()
        val assign = Assignment("test",Date(122,2,25),0,1000)
        assign.progress.addSession(ReadingSession(0,25,
            System.currentTimeMillis()-301000000, System.currentTimeMillis()-300000000
        ))
        val classes = PWClass("test", arrayListOf(assign),0L)
        studentCont.student.classes = arrayListOf(classes)
        studentCont.student.classes = arrayListOf(classes)
        val result = studentCont.calculateReadingSpeed("test")
        assertThat(result).isEqualTo(0.0)
    }

    @Test
    //creates a schedule with no class
    fun testCreateScheduleEmpty() {
        val studentCont = StudentController(context,Student("name",0L))
        studentCont.student.classes = ArrayList()
        studentCont.createSchedule()
        val result = studentCont.student.schedule
        assertThat(result).isEqualTo(ArrayList<PWClass>())
    }

    @Test
    //creates a schedule with no progress
    fun testCreateScheduleAssignNoProgress() {
        val studentCont = StudentController(context,Student("testing",0L))
        studentCont.student.readingSpeed = ArrayList()
        studentCont.student.classes = ArrayList()
        studentCont.student.classes.add(PWClass("testing", arrayListOf(Assignment("testing",Date(30,1,0),0,1000)),0L))
        studentCont.createSchedule()
        val result = studentCont.student.schedule
        assertThat(result).isEqualTo(ArrayList<PWClass>())
    }

    @Test
    //creates a schedule with invalid assignment
    fun testCreateScheduleInvalidAssignAssignment() {
        val studentCont = StudentController(context,Student("name",0L))
        val assign = Assignment("test assign",Date(1,1,1),0,10000)
        assign.progress.addSession(ReadingSession(0,100, System.currentTimeMillis(),
            System.currentTimeMillis()-3600000
        ))
        val classes = PWClass("test class", arrayListOf(assign),0L)
        studentCont.student.classes = arrayListOf(classes)
        studentCont.createSchedule()
        val result = studentCont.student.schedule
        assertThat(result).isEqualTo(ArrayList<PWClass>())
    }

    @Test
    //creates a schedule with valid progress
    fun testCreateScheduleValidAssignAssignment() {
        val studentCont = StudentController(context,Student("name",0L))
        val assign = Assignment("test assign",Date(122,2,25),0,10000)
        assign.progress.addSession(ReadingSession(0,1000,
            System.currentTimeMillis()-301000000, System.currentTimeMillis()-300000000
        ))
        val classes = PWClass("test class", arrayListOf(assign),0L)
        studentCont.student.classes = arrayListOf(classes)
        studentCont.createSchedule()
        val result = studentCont.student.schedule[0].reading.size
        assertThat(result).isEqualTo(1)
    }
}