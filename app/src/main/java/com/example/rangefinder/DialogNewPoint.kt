package com.example.rangefinder

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText

class DialogNewPoint {

    private lateinit var callBack: DialogInterfase
    private lateinit var editLengthLocal: EditText
    private lateinit var editXLocal: EditText
    private lateinit var editYLocal: EditText
//    private lateinit var btnReadAzimuth: Button
    private lateinit var btnCreateNewPoint: Button
    private lateinit var ad: AlertDialog
    private lateinit var adBilder: AlertDialog.Builder

    constructor(context: Context, callBack: DialogInterfase) {
        createDialog(context)
        this.callBack = callBack
    }

    private fun createDialog(context: Context) {
        val li = LayoutInflater.from(context)
        val promptsView = li.inflate(R.layout.dialog_new_point, null)

        adBilder = AlertDialog.Builder(context)
        adBilder
            .setCancelable(false)
//            .setPositiveButton("Ok", this)

        adBilder.setTitle("Новая точка")
        adBilder.setView(promptsView)
        initializationView(promptsView)

        setListener()

        ad = adBilder.create()

        btnCreateNewPoint.notClickable()
    }

    private fun setListener() {
//        btnReadAzimuth.setOnClickListener {
//            callBack.onClickDialogBtnReadAzimuth()
//        }
        btnCreateNewPoint.setOnClickListener {
            ad.cancel()
            sendLength()
        }
    }

    private fun sendLength() {

        callBack.onClickDialogBtnCreateNewPoint(
            editLengthLocal.text.toString().toDouble(),
            editXLocal.text.toString().toDouble(),
            editYLocal.text.toString().toDouble()
        )
    }

    private fun initializationView(promptsView: View) {
        editLengthLocal = promptsView.findViewById(R.id.editLength) as EditText
        editXLocal = promptsView.findViewById(R.id.editX) as EditText
        editYLocal = promptsView.findViewById(R.id.editY) as EditText
//        btnReadAzimuth = promptsView.findViewById(R.id.btnDialogReadAzimuth) as Button
        btnCreateNewPoint = promptsView.findViewById(R.id.btnDialogCreateNewPoint) as Button
    }

    fun showDialog(context: Context) {
        createDialog(context)

        ad.show()
    }

    fun btnCreateNewPointClickable() {
//        btnCreateNewPoint.clickable()
//        btnCreateNewPoint.setTextColor(btnReadAzimuth.textColors)
    }

}

@SuppressLint("ResourceAsColor")
private fun Button.notClickable() {
//    this.setTextColor(R.color.colorNotClickable)
//    this.isClickable = false
}

private fun Button.clickable() {
//    this.setTextColor()
//    this.isClickable = true


}
