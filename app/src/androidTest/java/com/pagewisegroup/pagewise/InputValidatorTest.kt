package com.pagewisegroup.pagewise

import android.content.Context
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.pagewisegroup.pagewise.util.DateDisplayView
import com.pagewisegroup.pagewise.util.InputValidator
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import kotlin.collections.ArrayList

/**
 * Unit test for input validator class
 * Needs context to replicate edit text inputs
 */
@RunWith(JUnit4::class)
class InputValidatorTest : TestCase() {
    lateinit var context: Context

    @Before
    //gets context
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    //invalid input for getEditTextHandler
    fun getEditTextHandlerInvalid() {
        val text = EditText(context)
        val result = InputValidator().getEditTextHandler(text, "err")
        assertThat(result).isEqualTo(null)
    }

    @Test
    //valid input for getEditTextHandler
    fun getEditTextHandlerValid() {
        val text = EditText(context)
        text.setText("text")
        val result = InputValidator().getEditTextHandler(text, "err")
        assertThat(result).isEqualTo("text")
    }

    @Test
    //invalid input for getDateHandler
    fun getDateHandlerInvalid() {
        val date = DateDisplayView(context)
        val result = InputValidator().getDateHandler(date)
        assertThat(result).isEqualTo(null)
    }

    @Test
    //valid input for getDateHandler
    fun getDateHandlerValid() {
        val date = DateDisplayView(context)
        date.picker.year = 2000
        date.picker.month = 1
        date.picker.day = 1
        val result = InputValidator().getDateHandler(date)
        assertThat(result).isEqualTo(Date(100,1,1))
    }

    @Test
    //pages in order
    fun pageErrorInvalid() {
        val result = InputValidator().pageErrorCheck(EditText(context),1,0)
        assertThat(result).isFalse()
    }

    @Test
    //pages in reverse order
    fun pageErrorValid() {
        val result = InputValidator().pageErrorCheck(EditText(context),0,1)
        assertThat(result).isTrue()
    }

    @Test
    //date is in the past for dateInPast
    fun dateInPastPast() {
        val result = InputValidator().dateInPast(DateDisplayView(context),Date(100,1,1),"err")
        assertThat(result).isTrue()
    }

    @Test
    //date is in the future for dateInPast
    fun dateInPastFuture() {
        val result = InputValidator().dateInPast(DateDisplayView(context),Date(1000,1,1),"err")
        assertThat(result).isFalse()
    }

    @Test
    //no classes exist
    fun getClassHandlerNoClass() {
        val text = AutoCompleteTextView(context)
        text.setText("test")
        val student = Student("student",0L)
        val result = InputValidator().getClassHandler(text,student)
        assertThat(result).isEqualTo(null)
    }

    @Test
    //class given does not match a class
    fun getClassHandlerWrongClass() {
        val text = AutoCompleteTextView(context)
        text.setText("test")
        val student = Student("student",0L)
        student.classes.add(PWClass("error", ArrayList(),0L))
        val result = InputValidator().getClassHandler(text,student)
        assertThat(result).isEqualTo(null)
    }

    @Test
    //class given matches existing class (valid input)
    fun getClassHandlerValid() {
        val text = AutoCompleteTextView(context)
        text.setText("test")
        val student = Student("student",0L)
        student.classes.add(PWClass("test", ArrayList(),0L))
        val result = InputValidator().getClassHandler(text,student)
        assertThat(result).isEqualTo("test")
    }
}