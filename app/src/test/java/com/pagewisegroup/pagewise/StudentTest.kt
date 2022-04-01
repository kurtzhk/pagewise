package com.pagewisegroup.pagewise

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import kotlin.collections.ArrayList

/**
 * Unit testing for student class
 */
@RunWith(JUnit4::class)
class StudentTest {

    @Test
    //index for class that doesn't exist
    fun getClassIndexNoClass() {
        val result = Student("",0L).getClassIndex("test")
        assertThat(result).isEqualTo(-1)
    }

    @Test
    //index for class that exist
    fun getClassIndexClassExists() {
        val student = Student("",0L)
        student.getClasses().add(PWClass("test", ArrayList(),0L))
        val result = student.getClassIndex("test")
        assertThat(result).isEqualTo(1)
    }


    @Test
    //assign index for assign that doesn't exist
    fun getAssignIndexNoAssign() {
        val student = Student("",0L)
        val testClass = PWClass("test", ArrayList(), 0L)
        student.getClasses().add(testClass)
        val result = student.getAssignIndex("test",testClass)
        assertThat(result).isEqualTo(-1)
    }

    @Test
    //assign index for assign that exists
    fun getAssignIndexAssignExists() {
        val student = Student("",0L)
        val testClass = PWClass("test", arrayListOf(Assignment("test",Date(0,0,0),0,1)), 0L)
        student.getClasses().add(testClass)
        val result = student.getAssignIndex("test",testClass)
        assertThat(result).isEqualTo(1)
    }

    @Test
    //assign index for class that does not exist
    fun assignExistsNoClass() {
        val student = Student("",0L)
        val result = student.assignExists("test","test")
        assertThat(result).isEqualTo(false)
    }

    @Test
    //checks if assign exists with no assign
    fun assignExistsNoAssign() {
        val student = Student("",0L)
        val result = student.assignExists("test","test")
        assertThat(result).isEqualTo(false)
    }

    @Test
    //checks if assign exists when it does
    fun assignExistsAssignExists() {
        val student = Student("",0L)
        student.getClasses().add(PWClass("test", arrayListOf(Assignment("test",Date(0,0,0),0,1)),0L))
        val result = student.assignExists("test","test")
        assertThat(result).isEqualTo(true)
    }

    @Test
    //zero days of reading
    fun getReadingHistoryZero() {
        val student = Student("",0L)
        val result = student.getReadingHistory(0)
        assertThat(result).isEqualTo(IntArray(0))
    }

    @Test
    //negative days of history
    fun getReadingHistoryNegative() {
        val student = Student("",0L)
        val result = student.getReadingHistory(-1)
        assertThat(result).isEqualTo(IntArray(0))
    }

    @Test
    //impossible future reading session
    fun getReadingHistoryFutureRS() {
        val student = Student("",0L)
        val assign = Assignment("test",Date(0,0,0),0,100)
        val rs = ReadingSession(0, 5, System.currentTimeMillis()*3, System.currentTimeMillis()*2)
        assign.getProgress().addSession(rs)
        student.getClasses().add(PWClass("test", arrayListOf(assign),0L))
        val result = student.getReadingHistory(5)
        assertThat(result).isEqualTo(IntArray(0))
    }

    @Test
    //prints past reading
    fun getReadingHistoryFutureValid() {
        val student = Student("",0L)
        val assign = Assignment("test", Date(),0,100)
        val rs = ReadingSession(0, 5, System.currentTimeMillis()-301000000, System.currentTimeMillis()-300000000)
        assign.getProgress().addSession(rs)
        student.getClasses().add(PWClass("test", arrayListOf(assign),0L))
        val result = student.getReadingHistory(5)
        val expected = IntArray(5)
        expected[3] = 5
        assertThat(result).isEqualTo(expected)
    }
}