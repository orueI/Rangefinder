package com.example.rangefinder

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_progress_cat.*

class FragmentProgressCat : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_progress_cat, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        Glide.with(this).load(R.drawable.progrescat)
            .into(imageProgressCat)
    }
}