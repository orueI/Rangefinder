package com.example.rangefinder

interface ActivityCallBack {
    fun registrationSensorListener()
    fun setLength(length: Double, x0: Double, y0: Double)
    fun calculate()
}