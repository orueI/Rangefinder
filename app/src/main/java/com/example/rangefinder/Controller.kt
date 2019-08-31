package com.example.rangefinder

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.ArrayList
import kotlin.math.cos
import kotlin.math.sin

class Controller : DialogInterfase, MenuInterface {

    lateinit var dialog: DialogNewPoint
    lateinit var ai: ActivityCallBack

    constructor(context: Context, ai: ActivityCallBack) {
        this.ai = ai
        dialog = DialogNewPoint(context, this)
    }

    override fun onClickDialogBtnCreateNewPoint(length: Double, x0: Double, y0: Double) {
        ai.setLength(length, x0, y0)
        ai.calculate()
    }

    override fun onClickDialogBtnReadAzimuth() {
        ai.registrationSensorListener()
    }

    override fun onClickMenuBtnCreateNewPoint(context: Context) {
        dialog.showDialog(context)
        ai.registrationSensorListener()
        dialog.btnCreateNewPointClickable()

    }

    override fun azimuthRead() {
        dialog.btnCreateNewPointClickable()
    }




}