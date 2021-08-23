package com.example.obstickleavoider.Game

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.constraintlayout.solver.widgets.Rectangle

class Obstacle(rectHeight : Int, color: Int, startX : Int, startY : Int, playerGap: Int) : GameObject{
    //left top right bottom
    private var rectangle: Rect? =  Rect(0, startY, startX, startY + rectHeight)
    private var rectangle2: Rect? = Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight)
    private var color = color

    fun getRectangle() : Rect? {
        return rectangle
    }

    fun incrementY (y : Float) {
        rectangle!!.top += y.toInt()
        rectangle!!.bottom += y.toInt()
        rectangle2!!.top += y.toInt()
        rectangle2!!.bottom += y.toInt()
    }

    fun playerCollide(player : RectPlayer) : Boolean{
        return rectangle?.let { player.getRectangle()?.let { it1 -> Rect.intersects(it, it1) } } == true || rectangle2?.let {
            player.getRectangle()?.let { it1 ->
                Rect.intersects(
                    it, it1
                )
            }
        } == true
    }

    override fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.color = color
        rectangle?.let { canvas.drawRect(it, paint) }
        rectangle2?.let { canvas.drawRect(it, paint) }
    }

    override fun update() {
    }

}