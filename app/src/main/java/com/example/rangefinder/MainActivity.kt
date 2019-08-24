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


class MainActivity : AppCompatActivity(), View.OnClickListener, DialogInterface.OnClickListener {
    val FILE_NAME = "ListOfPoint"
    lateinit var ad: AlertDialog
    lateinit var mHandler: Handler
    val listAzimuth = LinkedList<Float>()
    val listPitch = LinkedList<Float>()
    val listRoll = LinkedList<Float>()
    var lengthFull: Int = 0
    var defX: Int = 0
    var defY: Int = 0
    var fault = 0
    val listPoint = ArrayList<Point>()
    lateinit var mSettings: SharedPreferences
    lateinit var promptsView: View
    lateinit var editLengthLocal: EditText
    lateinit var editXLocal: EditText
    lateinit var editYLocal: EditText


//    private var dialogSetting: View

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnNewPoint->{
                val li = LayoutInflater.from(this)
                promptsView = li.inflate(R.layout.dialog_new_point, null)

                val adBilder = AlertDialog.Builder(this)
                adBilder
                    .setCancelable(false)
                    .setPositiveButton("Ok", this)
                adBilder.setTitle("Новая точка")
                adBilder.setView(promptsView)
                editLengthLocal = promptsView.findViewById(R.id.editLength) as EditText
                editXLocal = promptsView.findViewById(R.id.editX) as EditText
                editYLocal = promptsView.findViewById(R.id.editY) as EditText

                ad = adBilder.create()

                when (v?.id) {
//            R.id.btnSetting -> showADialog("btnSetting")
                    R.id.btnNewPoint -> showADialog("btnNewPoint")
//            R.id.btnNewBranch -> showADialog("btnNewBranch")
                }


            }
//            R.id.btnSave->{
//                val builder = GsonBuilder()
//                val gson = builder.create()
//
//                generateNoteOnSD(FILE_NAME, gson.toJson(listPoint))
//            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        btnSetting.setOnClickListener(this)
//        btnNewBranch.setOnClickListener(this)
        btnNewPoint.setOnClickListener(this)


    }

    fun showADialog(s: String) {
        when (s) {
//            "btnSetting" -> {
//                adBilder.setTitle("Настройки")
//                adBilder.setView(R.layout.alert_dialog)
//            }
            "btnNewPoint" -> {
                registrationSensorListener()
                ad.show()
            }
//            "btnNewBranch" -> {
//                adBilder.setTitle("Новая ветка")
//                adBilder.setView(R.layout.dialog_new_point)
//
//                registrationSensorListener()
//            }
        }
    }

    private fun registrationSensorListener() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
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
            }
        }

        mHandler.sendEmptyMessage(1000)

    }


    override fun onClick(dialog: DialogInterface?, which: Int) {

//            var s = "0"
//            var sX = "0"
//            var sY = "0"
//            if (editLengthLocal!= null){
        lengthFull = editLengthLocal.text.toString().toInt()
        defX = editXLocal.text.toString().toInt()
        defY = editYLocal.text.toString().toInt()
//        defX = editXLocal.text.
//            }
//             = s.toInt()

        //            if (editFault!= null)
//            fault = editFault.textAlignment

        dialog?.cancel()
        calculate()
    }

    val workingSensorEventListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {
            listAzimuth.add(event.values[0])
            listPitch.add(event.values[1])
            listRoll.add(event.values[2])
            Log.v("MyTag", "Azimuth ${event.values[0].toString()}")
            Log.v("MyTag", "Pitch ${event.values[1].toString()}")
            Log.v("MyTag", "Roll ${event.values[2].toString()}")
        }
    }


    fun calculate() {

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

        averageAzimuth /= listAzimuth.size
        averagePitch /= listPitch.size
        averageRoll /= listRoll.size

        val length = cos(Math.toRadians(averagePitch.toDouble())) * lengthFull
        val deltaX = cos(Math.toRadians(averageAzimuth.toDouble())) * length
        val deltaY = sin(Math.toRadians(averageAzimuth.toDouble())) * length
if (defX!=0&&defY!=0){
    val p = Point(
        defX + deltaX,
        defY + deltaY,
        lengthFull,
        averageAzimuth,
        averagePitch,
        averageRoll
    )
    listPoint.add(p)
}else if (defX==0&&defY==0){
    if (listPoint.size == 0) {
        val p = Point(deltaX, deltaY, lengthFull, averageAzimuth, averagePitch, averageRoll)
        listPoint.add(p)

    } else {
        val p = Point(
            listPoint[listPoint.size - 1].x + deltaX,
            listPoint[listPoint.size - 1].y + deltaY,
            lengthFull,
            averageAzimuth,
            averagePitch,
            averageRoll
        )
        listPoint.add(p)
    }
}

    }

    override fun onDestroy() {
        super.onDestroy()
        val builder = GsonBuilder()
        val gson = builder.create()

        generateNoteOnSD(FILE_NAME, gson.toJson(listPoint))
    }
}
