package com.example.obstickleavoider.Game

import android.graphics.Canvas
import android.view.MotionEvent

interface Scene {
    fun update()
    fun draw(canvas : Canvas?)
    fun terminate()
    fun receiveTouch(event: MotionEvent?)
    fun getObstacleManager(): ObstacleManager?
}