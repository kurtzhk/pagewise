package com.pagewisegroup.pagewise

import android.content.Context
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before

@RunWith(JUnit4::class)
class StudentControllerTest : TestCase() {
    lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    fun testAddClass() {
        val studentCont = StudentController(context,Student("name",0L))
        studentCont.addClass("class")
        val result = studentCont.student.classes
        assertEquals("wrong size", 3, result.size)
    }

    fun testAddAssignment() {}

    fun testAddReadingSession() {}

    fun testCreateTempAssignments() {}

    fun testCreateTempProgress() {}

    fun testCreateAssignmentUniqueString() {}

    fun testCalculateReadingSpeeds() {}

    fun testCalculateReadingSpeed() {}

    fun testCreateSchedule() {}

    fun testGetStudent() {

    }
}