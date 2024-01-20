import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const methodChannel = MethodChannel('com.platformchannel.demo/methodChannel');
// Is line mein ek static constant declare ho raha hai jiska naam methodChannel hai, 
// jo ki MethodChannel type ka hai.
  // MethodChannel: This is a class provided by the flutter/services.dart library. 
  // A MethodChannel is used for communication between Dart (Flutter) and native code, 
  // allowing method calls and data transfer in both directions.
  static const pressureChannel = EventChannel('com.platformchannel/pressure');
  // this line of code creates a static constant named pressureChannel that 
  // represents an EventChannel instance. 
  // EventChannel: This is a class provided by the flutter/services.dart library. 
  // An EventChannel is used for establishing a one-to-many communication channel 
  // from native code to Dart (Flutter), allowing the streaming of events or data 
  // from native code to Flutter.
  late StreamSubscription pressureSubscription;
  // StreamSubscription: This is a class provided by the dart:async library in Dart. 
  // It is used to represent a subscription to a stream, allowing you to listen to 
  // events emitted by the stream and manage the subscription (e.g., cancel it 
  // when it's no longer needed).
  double _pressureReading = 0;
  String _sensorAvailable = 'Unknown';

  Future<void> _checkAvailability() async{
    try{
     var available = await methodChannel.invokeMethod('isSensorAvailable'); 
     setState(() {
       _sensorAvailable = available.toString(); 
     });
    }
   on PlatformException catch(e){
    print(e);
   }
  }

  _startReading() {
    try{
         pressureSubscription = pressureChannel.receiveBroadcastStream().listen((event) { 
      setState(() {
        _pressureReading = event;
      });
    });
    }
     catch(e){
      print('$e yai error hai');
    }
  }

  _stopReading(){
    setState(() {
      _pressureReading = 0;
    });
    pressureSubscription.cancel();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: const Text('Platform Channel Demo'),
      ),
      body:  Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Sensor Available: $_sensorAvailable',
            ),
            ElevatedButton(
              onPressed: _checkAvailability,
              child: Text('Check Sensor Availability') ,
            ),
            const SizedBox(
              height: 50,
            ),
            Text('Sensor Reading: $_pressureReading'),
            ElevatedButton(
              onPressed: () => _startReading(),
            child: Text('Start Reading') ,
            ),
            SizedBox(
              height: 50,
            ),
            ElevatedButton(
              onPressed:()=> _stopReading(),
            child: Text('Stop Reading') ,
            ),
          ],
        ),
      ),
    );
  }
}
