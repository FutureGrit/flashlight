package com.futuregrit.flashlight

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.annotation.RequiresApi


class MainActivity: FlutterActivity() {
    private val API_LEVEL = Build.VERSION.SDK_INT

    private val CAMERA_PERMISSION = 200
    var flashLightStatus: Boolean = false

    private val CHANNEL = "flashlight_activity"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor, CHANNEL).setMethodCallHandler { call, _ ->
            if (call.method == "startFlashlight") {
                startFlashlight()
            }
        }
    }

    private fun startFlashlight() {
        /* Check for camera permission on device higher then API level 23 */
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permission != PackageManager.PERMISSION_GRANTED) {
                setupPermission()
            } else {
                turnOnFlashlightAboveVersionCodeM()
            }
        } else {
            turnOnFlashlightBelowVersionCodeM()
        }
    }

    private fun setupPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                } else {
                    turnOnFlashlightAboveVersionCodeM()
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun turnOnFlashlightAboveVersionCodeM() {
        Log.e("API>=23","*********** Flashlight for API >= M(SDK 23) *****************")
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]

        if (!flashLightStatus) {
            try{
                cameraManager.setTorchMode(cameraId, true)
                this.flashLightStatus = true
            } catch (e: CameraAccessException) {
                e.printStackTrace();
            }

        } else {
            try{
                cameraManager.setTorchMode(cameraId, false)
                this.flashLightStatus = false
            } catch (e: CameraAccessException) {
                e.printStackTrace();
            }
        }

    }

    /* This will be called if API Version Code is below M (SDK 23)*/
    private  fun turnOnFlashlightBelowVersionCodeM() {

    }

    // TODO: Check if flashlight is supported or not
    // TODO: Change Message for granting permission
    // TODO: Return true when successfully updated flashlight so UI can be updated accordingly
}
