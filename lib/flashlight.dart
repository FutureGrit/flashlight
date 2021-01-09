import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:audioplayers/audio_cache.dart';
import 'package:audioplayers/audioplayers.dart';

class Flashlight extends StatefulWidget {
  @override
  _FlashlightState createState() => _FlashlightState();
}

class _FlashlightState extends State<Flashlight> {
  AudioCache audioPlayerCache;
  bool isFlashlightOn = false;

  // set the name of the channel
  static const platform = const MethodChannel("com.futuregrit/flashlight");

  @override
  void initState() {
    super.initState();
    audioPlayerCache =
        AudioCache(fixedPlayer: AudioPlayer(mode: PlayerMode.LOW_LATENCY));
    audioPlayerCache.load('sound_flashlight_button.wav');
  }

  _updateFlashlight() async {
    try {
      if (await platform.invokeMethod("startFlashlight")) {
        /// Perform setState after native function finished or Flashlight is
        /// turned ON or OFF
        audioPlayerCache.play('sound_flashlight_button.wav', volume: 1.0);
        setState(() {
          isFlashlightOn = !isFlashlightOn;
        });
      }
    } on PlatformException catch (e) {
      print(e.message);
    }
  }

  @override
  Widget build(BuildContext context) {
    return FractionallySizedBox(
      widthFactor: 0.48,
      child: FlatButton(
        // Removing default button padding
        padding: EdgeInsets.all(0),
        splashColor: Colors.transparent,

        onPressed: () {
          _updateFlashlight();
        },
        child: isFlashlightOn
            ? Image.asset('images/flashlightOn.png')
            : Image.asset('images/flashlightOff.png'),
      ),
    );
  }

  @override
  void dispose() {
    super.dispose();
    audioPlayerCache.clearCache();
  }
}
