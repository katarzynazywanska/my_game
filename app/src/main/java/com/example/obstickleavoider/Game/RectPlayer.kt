package com.example.obstickleavoider.Game

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.example.obstickleavoider.R

class RectPlayer(rect : Rect, rgb: Int): GameObject {

    private var rectangle: Rect? = rect
    private var color = rgb

    fun getRectangle() : Rect? {
        return  rectangle
    }

    override fun draw(canvas: Canvas) {
        val paint : Paint = Paint()
        paint.color = color

        val myImage: Drawable? = ResourcesCompat.getDrawable(Constants.CURRENT_CONTEXT.resources, R.drawable.spaceship_img, null)
        myImage?.setBounds(rectangle!!)

        myImage?.draw(canvas)
       // rectangle?.let { canvas.drawRect(it, paint) }



    }

    override fun update() {
        TODO("Not yet implemented")
    }

    fun update(point : Point) {
        //left, top, right, bottom


        rectangle!![point.x - rectangle!!.width() / 2,
                point.y - rectangle!!.height() / 2,
                point.x + rectangle!!.width() / 2] =
            point.y + rectangle!!.height() / 2
    }
}