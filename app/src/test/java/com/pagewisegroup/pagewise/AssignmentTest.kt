package com.pagewisegroup.pagewise

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class AssignmentTest {

    @Test
    //checks comp estimate with no reading
    fun updateCompletionEstimateNoProg() {
        val assign = Assignment("name", Date(),0,10)
        assign.updateCompletionEstimate(1.0)
        assertThat(assign.timeToComplete).isEqualTo(10.0)
    }

    @Test
    //checks comp estimate with reading
    fun updateCompletionEstimateProg() {
        val assign = Assignment("name", Date(),0,10)
        val rs = ReadingSession(0, 5,
            System.currentTimeMillis() -301000000, System.currentTimeMillis() -300000000)
        assign.progress.addSession(rs)
        assign.updateCompletionEstimate(1.0)
        assertThat(assign.timeToComplete).isEqualTo(5.0)
    }

    @Test
    //checks comp estimate when reading speed is 0
    fun updateCompletionEstimateZero() {
        val assign = Assignment("name", Date(),0,10)
        assign.updateCompletionEstimate(0.0)
        assertThat(assign.timeToComplete).isEqualTo(0.0)
    }
    @Test
    //checks comp estimate when reading speed is negative
    fun updateCompletionEstimateNegative() {
        val assign = Assignment("name", Date(),0,10)
        assign.updateCompletionEstimate(-1.0)
        assertThat(assign.timeToComplete).isEqualTo(0.0)
    }
}