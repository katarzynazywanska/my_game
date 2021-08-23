package com.example.obstickleavoider.Game

import android.graphics.*
import android.view.MotionEvent
import android.widget.Toast
import com.example.obstickleavoider.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GameplayScene : Scene {

    private val r = Rect(0,0,0,0)
    private var player = RectPlayer(Rect(200, 200, 400, 400), Color.rgb(255, 0, 0))
    private var playerPoint: Point =  Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4)
    var obsManager = ObstacleManager(400,500,75, Color.rgb(30,17,54))
    private  var reference: DatabaseReference


    private var movingPlayer = false
    private var gameOver = false


    private var gameOverTime: Long = 0

    private var  orientationData = OrientationData()

    private var frameTime = System.currentTimeMillis()
    var oldPoints : Int = Int.MAX_VALUE

    init {
        reference = FirebaseDatabase.getInstance().getReference("Users")
        orientationData.register()
        player.update(playerPoint)
    }

    fun reset() {
        playerPoint = Point(Constants.SCREEN_WIDTH / 2, 3 * Constants.SCREEN_HEIGHT / 4)
        player!!.update(playerPoint!!)
        obsManager = ObstacleManager(400,500,75, Color.rgb(30,17,54))
        movingPlayer = false

    }

    override fun update() {
        if (!gameOver) {
            if (frameTime < Constants.INIT_TIME) {
                frameTime = Constants.INIT_TIME
            }
            val elapsedTime = (System.currentTimeMillis() - frameTime).toInt()
            frameTime = System.currentTimeMillis()
            if (orientationData.getStartOrientation() != null) {
                val pitch: Float =
                    orientationData.getOrientation()[1] - orientationData.getStartOrientation()!![1]
                val roll: Float =
                    orientationData.getOrientation()[2] - orientationData.getStartOrientation()!![2]
                val xSpeed = 2 * roll * Constants.SCREEN_WIDTH / 1000f
                val ySpeed = pitch * Constants.SCREEN_HEIGHT / 1000f

                if (Math.abs(xSpeed * elapsedTime) > 5){
                    playerPoint.x +=  (xSpeed * elapsedTime).toInt()
                }
                if (Math.abs(ySpeed * elapsedTime) > 5){
                    playerPoint.y -=  (ySpeed * elapsedTime).toInt()
                }

            }
            if (playerPoint.x < 0) playerPoint.x =
                0 else if (playerPoint.x > Constants.SCREEN_WIDTH) playerPoint.x =
                Constants.SCREEN_WIDTH
            if (playerPoint.y < 0) playerPoint.y =
                0 else if (playerPoint.y > Constants.SCREEN_HEIGHT) playerPoint.y =
                Constants.SCREEN_HEIGHT
            player.update(playerPoint)
            obsManager!!.update()
            if (obsManager!!.playerCollide(player)) {
                gameOver = true
                gameOverTime = System.currentTimeMillis()


                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val userProfile: User.UserObject? = dataSnapshot.getValue(User.javaClass)
                        if (userProfile != null) {
                            oldPoints = userProfile.points!!
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        show_info("Something wrong happened")
                    }
                }
                 FirebaseAuth.getInstance().currentUser?.let {
                    reference.child(it.uid)
                        .addListenerForSingleValueEvent(postListener)
                }

                if( oldPoints < obsManager.getScore()) {
                    FirebaseAuth.getInstance().currentUser?.let {
                        FirebaseDatabase.getInstance().getReference("Users")
                            .child(it.uid).child("points")
                            .setValue(obsManager.getScore())
                    }
                }
            }
        }
    }

    override fun draw(canvas : Canvas?) {
        if (canvas != null) {
            canvas.drawColor(Color.rgb(107, 76, 168))
            player.draw(canvas)
            obsManager.draw(canvas)

        }
        if (gameOver) {
            val paint = Paint()
            paint.textSize = 100f
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.color = Color.MAGENTA
            if (canvas != null) {
                drawCenterText(canvas, paint, "Game over")
            }
        }
    }

    override fun terminate() {
        SceneManager.ACTIVE_SCENE = 0
    }

    override fun receiveTouch(event: MotionEvent?) {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!gameOver && player!!.getRectangle()!!.contains(
                            event.x.toInt(),
                            event.y.toInt()
                        )
                    ) {
                        movingPlayer = true
                    }
                    if (gameOver && System.currentTimeMillis() - gameOverTime > 2000) {
                        reset()
                        gameOver = false
                        orientationData.newGame()
                    }
                }
                MotionEvent.ACTION_MOVE -> if (movingPlayer && !gameOver) {
                    playerPoint!![event.x.toInt()] = event.y.toInt()
                }
                MotionEvent.ACTION_UP -> movingPlayer = false
            }
        }
    }

    override fun getObstacleManager(): ObstacleManager? {
        return obsManager
    }



    private fun drawCenterText(canvas: Canvas, paint: Paint, text: String) {
        paint.textAlign = Paint.Align.LEFT
        canvas.getClipBounds(r)
        val cHeight = r.height()
        val cWidth = r.width()
        paint.getTextBounds(text, 0, text.length, r)
        val x = cWidth / 2f - r.width() / 2f - r.left
        val y = cHeight / 2f + r.height() / 2f - r.bottom
        canvas.drawText(text, x, y, paint)
    }

    fun show_info(info:String){
        Toast.makeText(Constants.CURRENT_CONTEXT,info , Toast.LENGTH_LONG).show()
    }
}