package com.example.obstickleavoider.Game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface


class ObstacleManager(playerGap: Int, obstacleGap : Int, obstacleHeight : Int, color : Int) {
    //higher index  = lower on screen = higher y value
    private var obstacles : ArrayList<Obstacle>? = null
    private var playerGap : Int = playerGap
    private var obstacleGap : Int = obstacleGap
    private var obstacleHeight : Int = obstacleHeight
    private var color : Int = color
    private  var score : Int =0
    private var startTime: Long = 0
    private var initTime = System.currentTimeMillis()

    //private var startTime = System.currentTimeMillis()

    init {
        obstacles = ArrayList<Obstacle>()
        startTime = initTime
        populateObstacles()

    }

    fun  getScore () : Int = score


    fun playerCollide(player: RectPlayer?): Boolean {
        for (ob in obstacles!!) {
            if (player?.let { ob.playerCollide(it) } == true) {
                return true
            }
        }
        return false
    }

    fun populateObstacles() {
        var currY : Int = -5*Constants.SCREEN_HEIGHT/4
        while(currY < 0) {
            var xStart : Int = (Math.random()*(Constants.SCREEN_WIDTH - playerGap)).toInt()
            obstacles!!.add(Obstacle(obstacleHeight, color, xStart, currY, playerGap))
            currY += obstacleHeight + obstacleGap
        }
    }


    fun update() {
        if (startTime < Constants.INIT_TIME) {
            startTime = Constants.INIT_TIME
        }

        var elapsedTime : Int = (System.currentTimeMillis() - startTime).toInt()
        startTime = System.currentTimeMillis()

        val speed: Float = Math.sqrt(1 + (startTime - initTime) / 2000.0).toFloat() * Constants.SCREEN_HEIGHT / 10000.0f
        //val speed = Constants.SCREEN_HEIGHT/10000.0f

        for (ob in obstacles!!) {
            ob.incrementY(speed * elapsedTime)
        }
        if(obstacles!!.get(obstacles!!.size - 1 ).getRectangle()!!.top >= Constants.SCREEN_HEIGHT){
            val xStart : Int = (Math.random()*(Constants.SCREEN_WIDTH - playerGap)).toInt()
            obstacles!!.add(0, Obstacle(obstacleHeight, color, xStart, obstacles!!.get(0).getRectangle()!!.top - obstacleHeight - obstacleGap, playerGap))
            obstacles!!.removeAt(obstacles!!.size - 1)
            score ++
        }
    }

    fun draw( canvas : Canvas) {
        for (ob in obstacles!!) {
            ob.draw(canvas)
        }
        val paint = Paint()
        paint.setColor(Color.rgb(224, 216, 239))
        paint.textSize = 100.0F
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("" + score, 50F, 80F, paint)
    }
}