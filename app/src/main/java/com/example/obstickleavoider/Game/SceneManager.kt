package com.example.obstickleavoider.Game

import android.graphics.Canvas
import android.view.MotionEvent
import java.util.*

object SceneManager {
    val gmpscene = GameplayScene()
    var scenes: ArrayList<Scene> = ArrayList<Scene>()
    var ACTIVE_SCENE = 0

    init {
        scenes.add(gmpscene)
    }

    fun receiveTouch(event: MotionEvent?) {
        scenes[ACTIVE_SCENE].receiveTouch(event)
    }

    fun update() {
        scenes[ACTIVE_SCENE].update()
    }

    fun draw(canvas: Canvas?) {
            scenes[ACTIVE_SCENE].draw(canvas)
    }
}