import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:audioplayers/audio_cache.dart';

class Flashlight extends StatefulWidget {
  @override
  _FlashlightState createState() => _FlashlightState();
}

class _FlashlightState extends State<Flashlight> {
  bool isFlashlightOn = false;

  // set the name of the channel
  static const platform = const MethodChannel("com.futuregrit/flashlight");

  _updateFlashlight() async {
    try {
      if (await platform.invokeMethod("startFlashlight")) {
        /// Perform setState after native function finished or Flashlight is
        /// turned ON or OFF
        setState(() {
          final player = AudioCache();
          player.play('sound_flashlight_button1.wav');
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
}
