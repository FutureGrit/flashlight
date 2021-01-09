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
    return Container(
      margin: EdgeInsets.all(40.0),
      padding: EdgeInsets.all(20.0),
      decoration: BoxDecoration(
        color: Color(0xffFD9808),
        borderRadius: BorderRadius.circular(16),
      ),
      child: Text(
        'Flash is not available in your device.',
        style: TextStyle(fontSize: 24, fontWeight: FontWeight.w600),
        textAlign: TextAlign.center,
      ),
    );
  }
}
