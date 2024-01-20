//
//  PressureStreamHandler.swift
//  Runner
//
//  Created by Puneet Raj on 17/01/24.
//

import Foundation
import CoreMotion

class PressureStreamHandler : NSObject, FlutterStreamHandler{
//  Class PressureStreamHandler: This declares a class named PressureStreamHandler. This class is intended to handle streaming events related to pressure data.PressureStreamHandler, is designed to handle streaming events, possibly related to pressure data, in the context of a Flutter application. By inheriting from NSObject and conforming to FlutterStreamHandler, it becomes suitable for use as a bridge between Flutter and native code, allowing communication and handling of events related to a pressure stream.
//    NSObject: This is the base class for most Objective-C classes and is commonly used as a superclass for Swift classes that need to interact with Objective-C APIs. In this case, it indicates that PressureStreamHandler is an Objective-C-compatible class.
//
//    FlutterStreamHandler: This is a protocol provided by the Flutter framework. Conforming to this protocol means that the class will implement methods required for handling streams of events between Flutter (Dart code) and native code (Swift or Objective-C).
    let altimeter = CMAltimeter()
    private let queue = OperationQueue()
    
    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
//   func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError?: This method is required by the FlutterStreamHandler protocol. It is called when a Flutter widget starts listening to the stream. It takes two parameters:
//        arguments: Any optional arguments passed from the Flutter side. In this case, it's not used (_ is Swift's way of ignoring unused parameters).
//        events: A closure (FlutterEventSink) that is used to send events back to the Flutter side.
        if CMAltimeter.isRelativeAltitudeAvailable(){
//  if CMAltimeter.isRelativeAltitudeAvailable() {: This checks if the relative altitude sensor is available on the device before attempting to start updates.
            altimeter.startRelativeAltitudeUpdates(to: queue){
                (data, error) in 
                if data != nil {
                    //get pressure
                    let pressurePascals = data?.pressure
//                    This extracts the pressure information from the altitude data.
                    events(pressurePascals!.doubleValue * 10.0)
//                    events(pressurePascals?.doubleValue ?? 0.0 * 10.0): This sends the pressure data to the Flutter side using the events closure. The pressure is multiplied by 10.0 (you might want to verify if this is the correct scaling factor for your use case).
                    
                }
            }
//  altimeter.startRelativeAltitudeUpdates(to: queue) { (data, error) in ... }: This starts the relative altitude updates using the altimeter and delivers the updates to the specified operation queue (queue). The closure inside this method is executed when altitude data is available.
        }
           return nil
        <#code#>
    }
    
    func onCancel(withArguments arguments: Any?) -> FlutterError? {
            altimeter.stopRelativeAltitudeUpdates()
            return nil
    }
}
