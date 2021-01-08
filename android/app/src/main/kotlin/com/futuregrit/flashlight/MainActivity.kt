package com.futuregrit.flashlight

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
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
import java.lang.RuntimeException
import java.util.*



class MainActivity : FlutterActivity() {
    private val CAMERA_PERMISSION = 200
    var camera: Camera? = null
    var parameters: Camera.Parameters? = null
    //var hasFlash: Boolean by Delegates.notNull<Boolean>()

    private var flashLightStatus: Boolean = false
    private var isStatusUpdated: Boolean = false

    private val CHANNEL = "flashlight_activity"


    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "startFlashlight") {
                //result.success(startFlashlight())
                startFlashlight()
                result.success(isStatusUpdated)
            }
        }
    }

    private fun startFlashlight(): Boolean {
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
        return true
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
        Log.e("API>=23", "*********** Flashlight for API >= M(SDK 23) *****************")
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]

        try {
            this.flashLightStatus = !flashLightStatus
            cameraManager.setTorchMode(cameraId, flashLightStatus)

            isStatusUpdated = true
        } catch (e: CameraAccessException) {
            e.printStackTrace();
        }
    }

    /* This will be called if API Version Code is below M (SDK 23)*/
    private fun turnOnFlashlightBelowVersionCodeM() {
        Log.e("API<23", "*********** Flashlight for API Below 23 *****************")
        if (camera == null && parameters == null) {
            try {
                camera = Camera.open()
                parameters = camera?.parameters
            } catch (e: RuntimeException) {
                Toast.makeText(this, "Camera is being used", Toast.LENGTH_LONG).show()
                return
            }

        }

        if (!flashLightStatus) {
            Log.e("API<23", "*********** Flashlight for API Below 23 ON *****************")
            parameters?.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            camera?.parameters = parameters
            Objects.requireNonNull(camera)?.startPreview()
            flashLightStatus = true
        } else {
            Log.e("API<23", "*********** Flashlight for API Below 23 OFF *****************")
            parameters?.flashMode = Camera.Parameters.FLASH_MODE_OFF
            camera?.parameters = parameters
            Objects.requireNonNull(camera)?.stopPreview()
            flashLightStatus = false
        }
        isStatusUpdated = true
    }

    private fun checkFlashAvailability(): Boolean {
        return applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    // TODO: Check camera availability for API Above 23
    // TODO: Change Message for granting permission
}
