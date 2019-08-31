package com.example.rangefinder

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    var length = 0.0
    var deltaX = 0.0
    var deltaY = 0.0

    @Before
    fun before() {
        val calculator = Calculater()
        val (length, deltaX, deltaY) = calculator.calculateNewCoordinates(300f, 315f, 10.0)
        this.length = length
        this.deltaX = deltaX
        this.deltaY = deltaY
    }

    @Test
    fun checkLengthQuadrant1() {
        assertEquals(5.toDouble(), length, 0.0001)
    }

    @Test
    fun checkDeltaXQuadrant1() {
        assertEquals(3.535533906, deltaX, 0.001)
    }

    @Test
    fun checkDeltaYQuadrant1() {
        assertEquals(3.535533906, deltaY, 0.001)
    }

//    @Test
//    fun checkLengthQuadrant2() {
//        assertEquals(5.toDouble(), length, 0.0001)
//    }
//
//    @Test
//    fun checkDeltaXQuadrant2() {
//        assertEquals(3.535533906, deltaX, 0.00001)
//    }
//
//    @Test
//    fun checkDeltaYQuadrant2() {
//        assertEquals(3.535533906, deltaY, 0.00001)
//    }
}
