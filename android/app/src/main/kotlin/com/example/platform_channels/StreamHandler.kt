package com.example.platform_channels

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.flutter.plugin.common.EventChannel

//This code defines a class named `StreamHandler`, which implements both the
//`EventChannel.StreamHandler` and `SensorEventListener` interfaces.
//
//
//1. `class StreamHandler`: This line declares a class named `StreamHandler`.
//
//2. `private val sensorManager: SensorManager`: This is a private, read-only
//property of type `SensorManager`. It is initialized with the value passed to
//the constructor.
//
//3. `sensorType: Int`: This is a parameter for the constructor, representing
//the type of sensor that this `StreamHandler` will handle. The type is an
//integer.
//
//4. `private var interval: Int = SensorManager.SENSOR_DELAY_NORMAL`:
//This is a private, mutable property of type `Int` representing the update
//interval for the sensor data. It has a default value of
//`SensorManager.SENSOR_DELAY_NORMAL`.

//5. `EventChannel.StreamHandler`: This interface is implemented by `StreamHandler`
//, indicating that this class can handle event stream operations for an
//`EventChannel`. This interface requires the implementation of the `onListen`
//and `onCancel` methods.
//
//6. `SensorEventListener`: This interface is implemented by `StreamHandler`,
//indicating that this class can handle sensor-related events. This interface
//requires the implementation of methods like `onSensorChanged` and
//`onAccuracyChanged`.


class StreamHandler(private val sensorManager: SensorManager, sensorType: Int,
                    private var interval:Int = SensorManager.SENSOR_DELAY_NORMAL) :
        EventChannel.StreamHandler, SensorEventListener {

    //yaha humne nikala ki sensor konsa hai with the help of sensormanager
    private val sensor = sensorManager.getDefaultSensor(sensorType)
    private var eventSink: EventChannel.EventSink? = null

//    In Flutter, an EventChannel.EventSink is part of the Flutter platform
//    channels system, specifically used for handling the stream of events
//    from the native side (Android or iOS) to the Dart (Flutter) side of a
//    Flutter application.

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {

        if(sensor != null){
            eventSink = events
//            If the sensor is not null, this line assigns the provided EventSink
//            (events) to the class variable eventSink. The EventSink is used to
//            send events to the Dart side of the Flutter application.

            sensorManager.registerListener(this, sensor,interval)
//            If the sensor is not null, this line registers the class
//            (which is assumed to implement SensorEventListener) as a listener
//            for sensor events with the sensorManager. The interval parameter is
//            the update interval for sensor data.
        }
    }

    override fun onCancel(arguments: Any?) {

        sensorManager.unregisterListener(this)
        eventSink = null
    }

//    onSensorChanged method is intended to be called when the sensor data
//    changes. It attempts to extract the sensor values from the SensorEvent
//    and send them to the Dart side using the provided EventSink (eventSink).

    override fun onSensorChanged(event: SensorEvent?) {

        var sensorValues = event!!.values[0]
//        Yeh line values array ke andar ke 1st element ([0]) ko access karne
//        ka prayas karti hai within the SensorEvent.

        eventSink?.success(sensorValues)

//        Yeh line sensor values ko Dart side mein bhejne ka prayas karti hai,
//        EventSink jo eventSink naam se hai, ka istemal karte hue. ?. operator
//        safe call operator hai, jo check karta hai ki eventSink null nahi hai
//        ya nahi, phir success method ko call karta hai. Agar eventSink null hai,
//        toh is line ka koi asar nahi hota.

//        This line attempts to send the sensor values to the Dart side using
//        the EventSink named eventSink. The ?. operator is the safe call operator,
//        which checks if eventSink is not null before calling the success method.
//        If eventSink is null, this line has no effect.
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        
    }
}

//EventChannel: Flutter mai, EventChannel ka use unidirectional communication
//channel setup karne k lie kya jaata hai,taaki events stream kie
//jaa sake from native platform to Flutter

//EventSink: EventChannel.EventSink ek aisa interface hai jo EventChannel ke
//madhyam sai events ko bhejne ke liye use hota hai. Ye ek sequence of events ke liye
//ek sink (matlab 1 destination) ko represent karta hai. Dart code ek EventChannel
//set up karta hai events sunne ke liye, aur jab kuch interesting hota hai (jaise
//ki sensor reading, location update, etc.), to native code Dart side ko EventSink
//ke madhyam sai events bhejta hai.

