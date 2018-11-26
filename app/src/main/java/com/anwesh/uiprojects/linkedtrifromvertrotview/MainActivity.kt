package com.anwesh.uiprojects.linkedtrifromvertrotview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.trifromvertrotview.TriFromVertRotView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TriFromVertRotView.create(this)
    }
}
