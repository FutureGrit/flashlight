import 'package:audioplayers/audio_cache.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(
    MaterialApp(
      home: Scaffold(
        backgroundColor: Color(0xff212121),
        body: SafeArea(
          child: Flashlight(),
        ),
      ),
    ),
  );
}

class Flashlight extends StatefulWidget {
  @override
  _FlashlightState createState() => _FlashlightState();
}

class _FlashlightState extends State<Flashlight> {
  // TODO 1: Implement actual working functionality for turn on/off flashlight
  // TODO 2: Implement RIVE animation for flashlight button status
  // TODO 3: Sync sound with animation
  // TODO 4: Update app icon

  bool isFlashlightOn = false;

  void updateFlashlight() {
    final player = AudioCache();
    player.play('sound_flashlight_button1.wav');

    setState(() {
      isFlashlightOn = !isFlashlightOn;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Align(
          alignment: Alignment.center,
          child: FractionallySizedBox(
            widthFactor: 0.48,
            child: FlatButton(
              // Removing default button padding
              padding: EdgeInsets.all(0),
              splashColor: Colors.transparent,

              onPressed: () {
                updateFlashlight();
              },
              child: isFlashlightOn
                  ? Image.asset('images/flashlightOn.png')
                  : Image.asset('images/flashlightOff.png'),
            ),
          ),
        ),
        Align(
          alignment: Alignment.bottomCenter,
          child: Padding(
            padding: const EdgeInsets.all(12.0),
            child: Text(
              '#FutureGrit',
              style: TextStyle(
                fontSize: 16,
                color: Colors.grey[800],
              ),
            ),
          ),
        ),
      ],
    );
  }
}
