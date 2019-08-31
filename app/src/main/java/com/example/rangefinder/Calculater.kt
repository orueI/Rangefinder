package com.example.rangefinder

import java.util.ArrayList
import kotlin.math.cos
import kotlin.math.sin

class Calculater {
    fun calculateAzimuth(
        averageAzimuth: Float,
        averagePitch: Float,
        averageRoll: Float,
        listAzimuth:ArrayList<Float>,
        listPitch:ArrayList<Float>,
        listRoll:ArrayList<Float>
    ): Triple<Float, Float, Float> {
        var averageAzimuth1 = averageAzimuth
        var averagePitch1 = averagePitch
        var averageRoll1 = averageRoll
        averageAzimuth1 = listAzimuth[0]     //.size-1
        averagePitch1 = listPitch[0]     //.size-1
        averageRoll1 = listRoll[0]     //.size-1

//        listAzimuth = ArrayList()
//        listPitch = ArrayList()
//        listRoll = ArrayList()
        return Triple(averageAzimuth1, averagePitch1, averageRoll1)
    }

    fun calculateNewCoordinates(
        averagePitch: Float,
        averageAzimuth: Float,
        lengthFull:Double
    ): Triple<Double, Double, Double> {
        val length = cos(Math.toRadians(averagePitch.toDouble())) * lengthFull
        val deltaX = cos(Math.toRadians(averageAzimuth.toDouble())) * length
        val deltaY = sin(Math.toRadians(averageAzimuth.toDouble())) * length
        return Triple(length, deltaX, deltaY)
    }
}