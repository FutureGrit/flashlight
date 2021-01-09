import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:flashlight/flashlight.dart';

class Home extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Align(
          alignment: Alignment.center,
          child: FutureBuilder<bool>(
            future: MethodChannel("com.futuregrit/flashlight")
                .invokeMethod('checkFlashAvailability'),
            builder: (context, snapshot) {
              if (snapshot.connectionState == ConnectionState.done) {
                return snapshot.data ? Flashlight() : flashNotAvailable();
              } else {
                return Container(
                  child: Text('Checking flash availability in your device...'),
                );
              }
            },
          ),
        ),
        Align(
          alignment: Alignment.bottomCenter,
          child: Padding(
            padding: const EdgeInsets.all(12.0),
            child: Text(
              '@FutureGrit',
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

  Widget flashNotAvailable() {
    return Center(
      child: Container(
        color: Colors.yellow,
        width: double.infinity,
        height: double.infinity,
        child: Text('Flash is not available in your device.'),
      ),
    );
  }
}
