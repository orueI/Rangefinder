package com.example.rangefinder

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.hardware.Sensor
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AlertDialog
import android.view.View
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.*
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import com.google.gson.GsonBuilder
import java.io.*
import android.content.SharedPreferences
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.view.LayoutInflater
import android.widget.EditText
import java.math.RoundingMode
import java.text.DecimalFormat


class MainActivity : AppCompatActivity(), View.OnClickListener,
//    DialogInterface.OnClickListener,
    ActivityCallBack {

var sensor = false
    val FILE_NAME = "ListOfPoint"
    lateinit var mHandler: Handler
    var listAzimuth = ArrayList<Float>()
    var listPitch = ArrayList<Float>()
    var listRoll = ArrayList<Float>()
    var lengthFull: Double = 0.0
    var defX: Double = 0.0
    var defY: Double = 0.0
    var fault = 0
    val listPoint = ArrayList<Point>()
    //    lateinit var mSettings: SharedPreferences
    lateinit var controller: MenuInterface
    lateinit var sensorManager: SensorManager


//    private var dialogSetting: View

    override fun onClick(v: View?) {
        controller.onClickMenuBtnCreateNewPoint(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        btnSetting.setOnClickListener(this)
//        btnNewBranch.setOnClickListener(this)
        btnNewPoint.setOnClickListener(this)

        controller = Controller(this, this)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val defPressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        sensorManager.registerListener(
            workingSensorEventListener,
            defPressureSensor,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun setLength(length: Double, x0: Double, y0: Double) {
        lengthFull = length
        defX = x0
        defY = y0
    }

    fun regListener() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val defPressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        sensorManager.registerListener(
            workingSensorEventListener,
            defPressureSensor,
            SensorManager.SENSOR_DELAY_UI
        )

        mHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                sensorManager.unregisterListener(
                    workingSensorEventListener
                )
                controller.azimuthRead()
//                calculate()

            }
        }

//        mHandler.postDelayed(1000)
    }

    override fun registrationSensorListener(){
        sensor = true
//        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        val defPressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
//        sensorManager.registerListener(
//            workingSensorEventListener,
//            defPressureSensor,
//            SensorManager.SENSOR_DELAY_UI
//        )
//        val handler = Handler()
//        val timeUpdaterRunnable = Runnable{
//            sensorManager.unregisterListener(
//                workingSensorEventListener
//            )
//            controller.azimuthRead()

//        }
//        handler.postDelayed(timeUpdaterRunnable,1000)
    }

    val workingSensorEventListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {

            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            textAzimuthRunTime.text     = " Azimuth = ${df.format(event.values[0].toDouble())}"
            textPitchRunTime.text         = " Pitch = ${df.format(event.values[1].toDouble())}"
//            textRollRunTime.text           = " Roll = ${event.values[2].toString()}"
            if(sensor) {
                listAzimuth.add(event.values[0])
                listPitch.add(event.values[1])
                listRoll.add(event.values[2])
//             averageAzimuth    = event.values[0]
//             averagePitch      = event.values[1]
//             averageRoll       = event.values[2]
                Log.v("MyTag", "Azimuth ${event.values[0].toString()}")
                Log.v("MyTag", "Pitch   ${event.values[1].toString()}")
                Log.v("MyTag", "Roll    ${event.values[2].toString()}")
                controller.azimuthRead()
               sensor = false
            }
        }
    }

    override fun calculate() {

        var averageAzimuth = 0f
        var averagePitch = 0f
        var averageRoll = 0f

//                listAzimuth.map { averageAzimuth += it }
        for (i in listAzimuth) {
            averageAzimuth += i
        }
        for (i in listPitch) {
            averagePitch += i
        }
        for (i in listRoll) {
            averageRoll += i
        }
//                listPitch.map { averagePitch + it }
//                listRoll.map { averageRoll + it }

        averageAzimuth  = listAzimuth [0]     //.size-1
        averagePitch    = listPitch   [0]     //.size-1
        averageRoll     = listRoll    [0]     //.size-1

        listAzimuth    = ArrayList()
        listPitch      = ArrayList()
        listRoll       = ArrayList()

        val length = cos(Math.toRadians(averagePitch.toDouble())) * lengthFull
        val deltaX = cos(Math.toRadians(averageAzimuth.toDouble())) * length
        val deltaY = sin(Math.toRadians(averageAzimuth.toDouble())) * length
        if (defX != 0.0 && defY != 0.0) {
            val p = Point(
                defX,
                defY,
                defX + deltaX,
                defY + deltaY,
                lengthFull,
                averageAzimuth,
                averagePitch,
                averageRoll
            )
            listPoint.add(p)
        } else if (defX == 0.0 && defY == 0.0) {
            if (listPoint.size == 0) {
                val p = Point(
                    0.0,
                    0.0,
                    deltaX,
                    deltaY,
                    lengthFull,
                    averageAzimuth,
                    averagePitch,
                    averageRoll
                )
                listPoint.add(p)

            } else {
                val p = Point(
                    listPoint[listPoint.size - 1].x1,
                    listPoint[listPoint.size - 1].y1,
                    listPoint[listPoint.size - 1].x0 + deltaX,
                    listPoint[listPoint.size - 1].y0 + deltaY,
                    lengthFull,
                    averageAzimuth,
                    averagePitch,
                    averageRoll
                )
                listPoint.add(p)
            }
        }

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
//        df.format(d)

        textX0.text = " X0 = ${df.format(listPoint[listPoint.size - 1].x0)} "
        textY0.text = " Y0 = ${df.format(listPoint[listPoint.size - 1].y0)}"
        textX1.text = " X1 = ${df.format(listPoint[listPoint.size - 1].x1)} "
        textY1.text = " Y1 = ${df.format(listPoint[listPoint.size - 1].y1)}"
        textLength.text = " Length = ${length}"
        textLength.text = " Length input = ${df.format(listPoint[listPoint.size - 1].lengthFull)}"
        textAzimuth.text = " Azimuth = ${df.format(listPoint[listPoint.size - 1].azimuth)}"
        textPitch.text = " Pitch = ${df.format(listPoint[listPoint.size - 1].Pitch)}"

    }

    override fun onStop() {
        super.onStop()

        val builder = GsonBuilder()
        val gson = builder.create()

        generateNoteOnSD(FILE_NAME, gson.toJson(listPoint))
    }
}
