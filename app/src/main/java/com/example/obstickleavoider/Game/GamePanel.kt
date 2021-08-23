package com.example.obstickleavoider.Game

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import com.example.obstickleavoider.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class GamePanel(context: Context):SurfaceView(context), SurfaceHolder.Callback {
    private var thread: MainThread
  //  private var r: Rect = Rect(0,0,0,0)
 //   private lateinit var player: RectPlayer
 //   private lateinit var playerPoint: Point
  //  var obstacleManager: ObstacleManager = ObstacleManager(400, 350, 75, Color.rgb(30, 17, 54))

  //  private var movingPlayer: Boolean = false
  //  private var gameOver: Boolean = false
  //  private var gameOverTime: Long = 0
    var manager: SceneManager

    init {
        holder.addCallback(this)
        Constants.CURRENT_CONTEXT = context
        thread = MainThread(holder, this)
        manager = SceneManager

        isFocusable = true
    }


    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread = MainThread(getHolder(), this)
        Constants.INIT_TIME = System.currentTimeMillis()
        thread.setRunning(true)
        thread.start()
    }


    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry: Boolean = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            retry = false
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        manager.receiveTouch(event)
        return true
    }





    fun update() {
        manager.update()
    }

    override fun draw(canvas: Canvas?) {
        if (canvas == null) {
            holder.removeCallback(this)
        } else {
            super.draw(canvas)
            manager.draw(canvas)
        }
    }

}
