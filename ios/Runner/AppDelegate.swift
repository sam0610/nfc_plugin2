import UIKit
import Flutter

@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
  override func application(
    _ application: UIApplication,
    didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?
  ) -> Bool {
    
    let controller : FlutterViewController = window?.rootViewController as! FlutterViewController;
    let batteryChannel = FlutterMethodChannel.init(name: "sam0610.nixon.io/nfc",
                                                   binaryMessenger: controller);
    batteryChannel.setMethodCallHandler({
        (call: FlutterMethodCall, result: FlutterResult) -> Void in
        if ("write" == call.method) {
            self.receiveBatteryLevel(result: result);
        } else if("read" == call.method) {
            result(FlutterMethodNotImplemented);
        }else {
            result(FlutterMethodNotImplemented);
        }
    });
    
    GeneratedPluginRegistrant.register(with: self)
    return super.application(application, didFinishLaunchingWithOptions: launchOptions)
  }
    
    

    private func receiveBatteryLevel(result: FlutterResult) {
        let message = "ios sam ";
        
        result(message);
        
    }

}

