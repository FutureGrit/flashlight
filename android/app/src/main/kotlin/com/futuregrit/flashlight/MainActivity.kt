package com.futuregrit.flashlight

import android.util.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant


class MainActivity: FlutterActivity() {
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
        Log.e("ActivityCalled","*********** Hello From Android startFlashlight************");
    }
}
