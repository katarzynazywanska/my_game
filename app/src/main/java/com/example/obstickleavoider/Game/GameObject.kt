package com.example.obstickleavoider.Game

import android.graphics.Canvas

interface GameObject {
    fun draw(canvas : Canvas)
    fun update()

}