package com.example.obstickleavoider.Game

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
import android.hardware.display.DisplayManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import com.example.obstickleavoider.ProfileActivity
import com.example.obstickleavoider.R

class GameMain : AppCompatActivity() {

    var gmp: GamePanel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        Constants.SCREEN_WIDTH = dm.widthPixels
        Constants.SCREEN_HEIGHT = dm.heightPixels
        gmp= GamePanel(this)
        setContentView(gmp)

    }

    override fun onBackPressed() {
        Thread(){
            runOnUiThread {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)

            }
        }.start()

        Thread(){
            runOnUiThread(){
                finish()
            }
        }
    }
}