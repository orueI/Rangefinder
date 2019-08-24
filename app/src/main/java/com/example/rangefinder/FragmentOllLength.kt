package com.example.rangefinder

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_all_langth.*

class FragmentOllLength: Fragment(),View.OnClickListener {
    override fun onClick(v: View?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater?.inflate(R.layout.fragment_all_langth, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        btnAllLength.setOnClickListener(this)
    }
}