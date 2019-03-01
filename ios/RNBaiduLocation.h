
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import <BMKLocationkit/BMKLocationComponent.h>
#import <CoreLocation/CLLocationManager.h>
@interface RNBaiduLocation : NSObject <BMKLocationManagerDelegate,RCTBridgeModule,BMKLocationAuthDelegate>
+(void)setKey:(NSString *)key;
@end
  
