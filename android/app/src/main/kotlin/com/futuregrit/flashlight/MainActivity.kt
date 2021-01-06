package com.futuregrit.flashlight

import android.os.Bundle
import android.util.Log
import io.flutter.app.FlutterActivity
//import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugins.GeneratedPluginRegistrant


class MainActivity: FlutterActivity() {
    private val CHANNEL = "flashlight_activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GeneratedPluginRegistrant.registerWith(FlutterEngine(applicationContext))

        MethodChannel(flutterView, CHANNEL).setMethodCallHandler { call, _ ->
            if (call.method == "startFlashlight") {
                startFlashlight()
            }
        }

    }
    private fun startFlashlight() {
        Log.e("ActivityCalled","*********** Hello From Android startFlashlight************");
    }
}
