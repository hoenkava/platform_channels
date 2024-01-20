package com.example.platform_channels
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
//    Is line mein ek nayi class ka declaration hai jiska naam
//    `MainActivity` hai. Colon (`:`) yeh darshata hai ki yeh
//    class kisi aur class se inherit hoti hai, jiska naam yahan
//    `FlutterActivity` hai.
//    FlutterActivity ek pehle se define ki gayi class hai jo ki Flutter
//    framework dwara specifically Android development ke liye provide ki
//    gayi hai. Yeh `android.app.Activity` ko extend karti hai aur Flutter
//    aur Android platform ke beech zaroori integration provide karti hai.

    private val METHOD_CHANNEL_NAME = "com.platformchannel.demo/methodChannel"
//    yai line ek constant define karti hai METHOD_CHANNEL_NAME naam kaa aur
//    ise "com.platformchannel.demo/methodChannel" string type ka value assign
//    kiya gaya hai. Ye string ek method channel ka naam hai, jo Flutter Dart
//    code aur Android native code ke beech communication ke liye use ki jaaegi.
    private var methodChannel: MethodChannel? =null
    private val PRESSURE_CHANNEL_NAME = "com.platformchannel/pressure"

    private lateinit var sensorManager: SensorManager
    private var pressureChannel: EventChannel? =null
    private var pressureStreamHandler : StreamHandler? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
// yaha pe configureFlutterEngine jo FlutterActivity class sai belong karta hai
// use override kia jaa raha hai
// Jab Flutter engine Flutter application ke liye
// configure yaa tyaar ho raha hota hai, tab ye function call hota hai.


        super.configureFlutterEngine(flutterEngine)
// Fir aati hai Ye line, joki superclass FlutterActivity method k andar waale configureFlutterEngine ko
// invoke karti hai, jisse yai confirm hotaa hai ki parent k pas jo
//  bhi base configuration hai Flutter Engine k lie
//  wo flutter engine k lie apply ho jaae.

        //setup Channels
        setupChannels(this, flutterEngine.dartExecutor.binaryMessenger)
//        this: this: Yeh current class ka instance (context) ko represent karta hai. Is code ke context mein,
//        flutterEngine.dartExecutor.binaryMessenger: Yai jo expression hai yai
//        BinaryMessenger provide karta hai joki FlutterEngine sai associated hhai

//        It is used for communication between Flutter and native code.



    }

    override fun onDestroy() {
//        fun onDestroy(): Ye line onDestroy method ko declare kar rahi hai.
//        Android mein, onDestroy method activity lifecycle ka hissa hai
//        aur isko tab call kiya jata hai jab activity destroy karni hoti
//        hai.  for example, jab user activity se bahar jaa raha
//        ho ya jab system resources waapis reclaim karna chahta ho.
        tearDownChannels()
        super.onDestroy()
    }

    private fun setupChannels(context: Context, messenger: BinaryMessenger){
//        The Context typically provides information about the application's
//        environment, and BinaryMessenger facilitates communication between
//        Flutter and native code.
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        The getSystemService() method is a part of the Context class and
//        is used to retrieve a system-level service. In this case, we are
//        retrieving the SENSOR_SERVICE system service. The as SensorManager
//        part is used to cast the returned object to the SensorManager class.
//        Context.SENSOR_SERVICE: A constant value representing the sensor service.
//        1 tarike sai isi naam sai bulaya hai us service ko

        methodChannel = MethodChannel(messenger, METHOD_CHANNEL_NAME)
//        MethodChannel(messenger, METHOD_CHANNEL_NAME): This part creates
//        a new instance of the MethodChannel class.

//        messenger: Yeh ek BinaryMessenger instance hai, jo aam taur par
//        Flutter aur native code ke beech communication ko facilitate karne
//        ke liye zimmedar hota hai. Yeh messenger communication channels
//        sthapit karne ke liye avashyak hai.
//
//        METHOD_CHANNEL_NAME: Yeh ek string constant hai jo communication
//        channel ka naam darshata hai. Is  naam ki help sai hum specific channel
//        ko identify karnege taaki hum us channel sai communicate  kar paae.

        methodChannel!!.setMethodCallHandler{
//            setMethodCallHandler: This method is part of the MethodChannel
//            class and is used to set a callback handler for incoming method
//            calls from Flutter.
            call, result -> if(call.method == "isSensorAvailable") {
                result.success(sensorManager!!.getSensorList(Sensor.TYPE_PRESSURE).isNotEmpty())

// If the method is "isSensorAvailable," it checks whether the device has a
//            pressure sensor. If the sensor is available, it sends a success
//            message back to Flutter using result.success. Otherwise, it
//            sends a failure message.
            }
//            The block { call, result -> ... }: Yai lambda expression hai jo
//             call handler method k behaivour ko define karta hai. It takes two
//            parameters:
//            call: Represents the method call from Flutter. It contains
//            information about the method being called, including its name
//            (call.method).
//            result: Represents the result object that is used to send the
//            result of the method call back to Flutter.

            else {
             result.notImplemented()
            }

        }
        pressureChannel = EventChannel(messenger, PRESSURE_CHANNEL_NAME)
        println("$pressureChannel pressureChannel");
        pressureStreamHandler = StreamHandler(sensorManager!!, Sensor.TYPE_PRESSURE)
        println("$pressureStreamHandler pressureStreamHandler");
        pressureChannel!!.setStreamHandler(pressureStreamHandler)


//        1. **`pressureChannel = EventChannel(messenger, PRESSURE_CHANNEL_NAME)`**:
//        This line creates an instance of `EventChannel` named `pressureChannel`.
//        It takes two parameters:
//        - `messenger`: This is a `BinaryMessenger` instance, presumably obtained
//        from the `FlutterEngine`. It is necessary for establishing communication
//        channels between Flutter and native code.
//        - `PRESSURE_CHANNEL_NAME`: This is a string constant representing the name
//        of the communication channel. It is used to identify the specific channel
//        for streaming pressure sensor-related events.
//
//        2. **`pressureStreamHandler = StreamHandler(sensorManager!!,
//        Sensor.TYPE_PRESSURE)`**: This line creates an instance of the
//        `StreamHandler` class and assigns it to the variable
//        `pressureStreamHandler`. It takes three parameters:
//        - `sensorManager!!`: This is the `SensorManager` instance,
//        forcefully unwrapped using `!!` assuming it is not null.
//        `SensorManager` is required for handling sensor events.
//        - `Sensor.TYPE_PRESSURE`: This is an integer constant representing the
//        type of sensor, specifically the pressure sensor in this case.
//
//        3. **`pressureChannel!!.setStreamHandler(pressureStreamHandler)`**:
//        This line sets the `StreamHandler` (`pressureStreamHandler`) as the
//        handler for the `pressureChannel` to manage the stream of events.
//        It uses the `setStreamHandler` method provided by the `EventChannel`
//        class.
//
//        In summary, this code establishes an event channel (`pressureChannel`)
//        for streaming pressure sensor-related events from native Android code
//        to Flutter Dart code. It uses a `StreamHandler` (`pressureStreamHandler`)
//        to handle the events, and the communication is set up through the
//        `EventChannel` API.

        }
    private fun tearDownChannels(){
        methodChannel!!.setMethodCallHandler(null)
        pressureChannel!!.setStreamHandler(null)
        //shut down methodchannel
    }


}



