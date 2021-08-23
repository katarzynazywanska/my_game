package com.example.obstickleavoider.Game

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class OrientationData: SensorEventListener {
    private var manager: SensorManager?  = Constants.CURRENT_CONTEXT!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = manager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var magnometer: Sensor? = manager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var accelOutput: FloatArray? = null
    private var magOutput: FloatArray? = null

    private val orientation = FloatArray(3)
    fun getOrientation(): FloatArray = orientation

    private var startOrientation: FloatArray? = null

    fun getStartOrientation(): FloatArray? = startOrientation

    fun newGame() {
        startOrientation = null
    }

    fun register() {
        manager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        manager?.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME)
    }

    fun pause(){
        manager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
                accelOutput = event.values
            }
            else if( event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD ){
                magOutput = event.values
            }
            if (accelOutput != null && magOutput != null) {
                var R = FloatArray(9)
                var I= FloatArray(9)
                var succsess : Boolean = SensorManager.getRotationMatrix(R,I, accelOutput,magOutput)
                if(succsess) {
                    SensorManager.getOrientation(R, orientation)
                    if(startOrientation == null) {
                        startOrientation = FloatArray(orientation.size)
                        System.arraycopy(orientation, 0, startOrientation,0, orientation.size)
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}