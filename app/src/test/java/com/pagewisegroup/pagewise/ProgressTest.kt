package com.pagewisegroup.pagewise

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class ProgressTest {
    @Test
    //tests adding sessions
    fun addSession() {
        val prog = Progress(Assignment("test", Date(), 0, 10))
        val rs = ReadingSession(0, 5,
            System.currentTimeMillis() -301000000, System.currentTimeMillis() -300000000)
        prog.addSession(rs)
        assertThat(prog.getSessions().size).isEqualTo(1)
    }
    @Test
    //tests adding session that already exists
    fun addSessionDuplicate() {
        val prog = Progress(Assignment("test", Date(), 0, 10))
        val rs = ReadingSession(0, 5,
            System.currentTimeMillis() -301000000, System.currentTimeMillis() -300000000)
        prog.addSession(rs)
        prog.addSession(rs)
        assertThat(prog.getSessions().size).isEqualTo(1)
    }

    @Test
    //session already exists
    fun sessionExists() {
        val prog = Progress(Assignment("test", Date(), 0, 10))
        val rs = ReadingSession(0, 5,
            System.currentTimeMillis() -301000000, System.currentTimeMillis() -300000000)
        prog.addSession(rs)
        val result = prog.sessionExists(rs)
        assertThat(result).isTrue()
    }
    @Test
    //session doesn't exist
    fun sessionNoExists() {
        val prog = Progress(Assignment("test", Date(), 0, 10))
        val rs = ReadingSession(0, 5,
            System.currentTimeMillis() -301000000, System.currentTimeMillis() -300000000)
        val result = prog.sessionExists(rs)
        assertThat(result).isFalse()
    }

    @Test
    //no current page/page 0
    fun getCurrentPageNoRs() {
        val result = Progress(Assignment("test", Date(), 1, 10)).getCurrentPage()
        assertThat(result).isEqualTo(1)
    }

    @Test
    //current page is over endpage of assignment
    fun getCurrentPageOver() {
        val prog = Progress(Assignment("test", Date(), 0, 10))
        val rs = ReadingSession(0, 20,
            System.currentTimeMillis() -301000000, System.currentTimeMillis() -300000000)
        prog.addSession(rs)
        val result = prog.getCurrentPage()
        assertThat(result).isEqualTo(10)
    }

    @Test
    //current page is valid
    fun getCurrentPage() {
        val prog = Progress(Assignment("test", Date(), 0, 10))
        val rs = ReadingSession(0, 5,
            System.currentTimeMillis() -301000000, System.currentTimeMillis() -300000000)
        prog.addSession(rs)
        val result = prog.getCurrentPage()
        assertThat(result).isEqualTo(5)
    }
    @Test
    //gets portion complete with no reading session
    fun getPortionCompleteNoRs() {
        val result = Progress(Assignment("test", Date(), 0, 10)).getPortionComplete()
        assertThat(result).isEqualTo(0.toFloat())
    }

    @Test
    //gets portion complete with a reading session
    fun getPortionCompleteRs() {
        val prog = Progress(Assignment("test", Date(), 0, 10))
        val rs = ReadingSession(0, 5,
            System.currentTimeMillis() -301000000, System.currentTimeMillis() -300000000)
        prog.addSession(rs)
        val result = prog.getPortionComplete()
        assertThat(result).isEqualTo(.5F)
    }
}