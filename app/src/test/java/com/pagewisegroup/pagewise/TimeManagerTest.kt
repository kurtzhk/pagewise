package com.pagewisegroup.pagewise

import com.google.common.truth.Truth.assertThat
import com.pagewisegroup.pagewise.schedule.PlannedDay
import com.pagewisegroup.pagewise.util.TimeManager
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Unit tests for time manager
 */
@RunWith(JUnit4::class)
class TimeManagerTest {

    @Test
    //checks if index for empty plannedDay
    fun findDayIndexEmpty() {
        val result = TimeManager().findDayIndex(ArrayList(),Date())
        assertThat(result).isEqualTo(-1)
    }

    @Test
    //checks for index outside of plannedDays
    fun findDayIndexInvalid() {
        val plannedDay = PlannedDay(Date(), ArrayList())
        val result = TimeManager().findDayIndex(arrayListOf(plannedDay),Date(1,1,1))
        assertThat(result).isEqualTo(-1)
    }

    @Test
    //checks for index outside of plannedDays
    fun findDayIndexValid() {
        val plannedDay1 = PlannedDay(Date(1,1,1), ArrayList())
        val plannedDay2 = PlannedDay(Date(1,1,2), ArrayList())
        val plannedDay3 = PlannedDay(Date(1,1,3), ArrayList())
        val result = TimeManager().findDayIndex(arrayListOf(plannedDay1,plannedDay2,plannedDay3),Date(1,1,2))
        assertThat(result).isEqualTo(1)
    }

    @Test
    //diff between two dates in seconds
    fun getDateDiffSeconds() {
        val result = TimeManager().getDateDiff(Date(1,1,1), Date(1,1,2),TimeUnit.SECONDS)
        assertThat(result).isEqualTo(86400)
    }
    @Test
    //diff between two dates in negative diff
    fun getDateDiffSecondsReversed() {
        val result = TimeManager().getDateDiff(Date(1,1,2), Date(1,1,1),TimeUnit.SECONDS)
        assertThat(result).isEqualTo(-1)
    }
    @Test
    //diff between two dates in seconds
    fun getDateDiffHours() {
        val result = TimeManager().getDateDiff(Date(1,1,1), Date(1,1,2),TimeUnit.HOURS)
        assertThat(result).isEqualTo(24)
    }
    @Test
    //diff between two dates in seconds
    fun getDateDiffMilli() {
        val result = TimeManager().getDateDiff(Date(1,1,1), Date(1,1,2),TimeUnit.MILLISECONDS)
        assertThat(result).isEqualTo(86400000)
    }


    @Test
    //day is incremented by 1
    fun incrementDay() {
        val result = TimeManager().incrementDay(Date(1,1,1))
        assertThat(result).isEqualTo(Date(1,1,2))
    }

    @Test
    //day is incremented to next month
    fun incrementDayOverMonth() {
        val result = TimeManager().incrementDay(Date(1,1,28))
        assertThat(result).isEqualTo(Date(1,2,1))
    }
}