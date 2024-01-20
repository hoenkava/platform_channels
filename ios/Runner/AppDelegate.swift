import UIKit
import Flutter
import CoreMotion
//These statements import the necessary modules for iOS development (UIKit) and Flutter integration.

@UIApplicationMain

//Ye attribute yeh batane ke liye istemal hota hai ki yeh Swift file application ka entry point hai aur ki AppDelegate class application delegate hai. @UIApplicationMain attribute alag se main.swift file ki zarurat ko khatam karta hai jo ki explicit taur par UIApplicationMain function ko contain karta hai. Isse ye hota hai ki application delegate class (is case mein, AppDelegate) ko application ka entry point ke roop mein istemal kiya ja sake.
@objc class AppDelegate: FlutterAppDelegate {
//    Ye line AppDelegate class ko define karti hai, jo ek bahut important component hota hai iOS application mein. Ye events aur life cycle methods ko handle karne ke liye zimmedar hota hai, jaise ki app launch, termination, background mein jaana, aur background se wapas aana.
//  Ye class FlutterAppDelegate se inherit hoti hai, jo indicate kart hai iski ability ki yai flutter-relared functionality ko handle kar sakta hai aur saath mai iOS app life cycle events ko bhi.   indicating its ability to handle Flutter-related functionality alongside standard iOS app life cycle events.
    
  override func application(
    
//    The override keyword indicates that this method is overriding a function from the superclass. In this case, it's overriding the application(_:didFinishLaunchingWithOptions:) method from the superclass (FlutterAppDelegate).
    
    _ application: UIApplication,
    
//    This parameter represents the instance of the UIApplication class, which is the central class of the iOS app and provides access to the application's state and functionality.
    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    
//    This method is called when the app finishes launching and provides options for handling the launch. The launchOptions parameter is a dictionary containing information about the launch, such as the reason for the app launch.
  ) -> Bool {
//      This part of the method signature indicates that the method returns a Boolean value. In the context of didFinishLaunchingWithOptions, returning true typically indicates that the app launched successfully, while false may indicate a failure
      
      
      let METHOD_CHANNEL_NAME = "com.platformchannel.demo/methodChannel"
//      let: This keyword is used to declare constants in Swift, meaning that the value of METHOD_CHANNEL_NAME cannot be changed once it's assigned.
      let PRESSURE_CHANNEL_NAME = "com.platformchannel/pressure"
      let pressureStreamHandler = PressureStreamHandler()
      let controller : FlutterViewController = window?.rootViewController as! FlutterViewController
//  controller: This is the name given to the constant, representing an instance of FlutterViewController. FlutterViewController is a class provided by the Flutter framework that serves as a UIViewController for embedding Flutter widgets in an iOS application. window?.rootViewController: This expression is accessing the rootViewController property of the application's main window. The rootViewController is typically the initial view controller that is displayed when the app launches. as! FlutterViewController: This is a type cast using the as! operator, indicating that the rootViewController is expected to be of type FlutterViewController. If the cast fails at runtime, it will result in a runtime error.
      
      let methodChannel = FlutterMethodChannel(name: METHOD_CHANNEL_NAME, binaryMessenger: controller.binaryMessenger)
//  FlutterMethodChannel: This is a class provided by the Flutter framework for creating and managing communication channels between Flutter and native code using method calls. name: METHOD_CHANNEL_NAME: This part specifies the name of the method channel. METHOD_CHANNEL_NAME is a constant that likely contains a unique identifier for the method channel. binaryMessenger: controller.binaryMessenger: This part specifies the binary messenger associated with the method channel. In Flutter, a binary messenger is a mechanism for sending binary messages (method calls, in this case) between Dart (Flutter) and native code. controller.binaryMessenger is using the binary messenger associated with the FlutterViewController instance created earlier.
      
      methodChannel.setMethodCallHandler({(call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in
//      setMethodCallHandler: This method is used to set up a handler for incoming method calls on the method channel.
//          {(call: FlutterMethodCall, result: @escaping FlutterResult) -> Void in: This is a closure (anonymous function) that serves as the handler for incoming method calls. It takes two parameters:
//          call: An object of type FlutterMethodCall that contains information about the method call, such as the method name.
//          result: A closure of type FlutterResult that is used to send the result of the method call back to the Flutter side.
          switch call.method{
//              switch call.method: This switch statement is used to handle different method names that may be called from the Flutter side.
          case "isSensorAvailable":
              result(CMAltimeter.isRelativeAltitudeAvailable())
//     This line calls the isRelativeAltitudeAvailable method of CMAltimeter, which is a part of the Core Motion framework. It checks if the relative altitude sensor is available on the device and returns the result to Flutter using the result closure.
          default :
              result(FlutterMethodNotImplemented)
          }
      })
      
      let pressureChannel = FlutterEventChannel(name: PRESSURE_CHANNEL_NAME, binaryMessenger: controller.binaryMessenger)
      pressureChannel.setStreamHandler(pressureStreamHandler)
      
    GeneratedPluginRegistrant.register(with: self)
//      This line registers Flutter plugins with the Flutter engine
      
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
//      Ant mein, yeh method superclass (`FlutterAppDelegate`) ki implementation ka result return karta hai jise `application(_:didFinishLaunchingWithOptions:)` ke liye define kiya gaya hai. Isse yeh hota hai ki method ka default behavior barkarar rahe, aur koi bhi zaroori initialization jo Flutter se judi hai, woh bhi ho sake.
//      Saransh mein, yeh code iOS application delegate ko Flutter integration ke saath set up karta hai, isse ensure karte hue ki Flutter plugins register hote hain aur app native iOS code aur Flutter code ko dono chalaane ke liye tayaar hota hai.
  }
}
