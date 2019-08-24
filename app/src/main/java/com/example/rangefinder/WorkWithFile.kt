package com.example.rangefinder

import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

fun generateNoteOnSD( sFileName: String, sBody: String) {
    try {
        val root = File(Environment.getExternalStorageDirectory(), "Notes")
        if (!root.exists()) {
            root.mkdirs()
        }
        val sdf = SimpleDateFormat("yyyy.MM.dd_HH:mm")
        val currentDateandTime = sdf.format(Date())


        val fileName = "${sFileName}_${currentDateandTime}.txt"
        val gpxfile = File(root, fileName)
        val writer = FileWriter(gpxfile)
        writer.append(sBody)
        writer.flush()
        writer.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
