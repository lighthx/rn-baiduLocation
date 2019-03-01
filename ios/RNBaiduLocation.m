
#import "RNBaiduLocation.h"
BMKLocationManager* _locationManager;
@implementation RNBaiduLocation

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
+(void)setKey:(NSString *)key{
        [[BMKLocationAuth sharedInstance] checkPermisionWithKey:key authDelegate:self];
}
RCT_EXPORT_MODULE()
RCT_REMAP_METHOD(init,
                 findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject){
    _locationManager = [[BMKLocationManager alloc] init];
    _locationManager.delegate = self;
    _locationManager.coordinateType = BMKLocationCoordinateTypeBMK09LL;
    _locationManager.distanceFilter = kCLDistanceFilterNone;
    _locationManager.desiredAccuracy = kCLLocationAccuracyBest;
    _locationManager.activityType = CLActivityTypeAutomotiveNavigation;
    _locationManager.pausesLocationUpdatesAutomatically = YES;
    _locationManager.allowsBackgroundLocationUpdates=NO;
    _locationManager.locationTimeout = 10;
    _locationManager.reGeocodeTimeout = 10;
    resolve(@"1");
}

RCT_REMAP_METHOD( getLocation,
                 findEventsWithResolve:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject ){

    [_locationManager requestLocationWithReGeocode:YES withNetworkState:YES completionBlock:^(BMKLocation * _Nullable location, BMKLocationNetworkState state, NSError * _Nullable error) {
        NSLog(@"%f",location.location.coordinate.latitude); ;
        NSLog(@"%f",location.location.coordinate.longitude); ;
        NSLog(@"%@",location.rgcData.country); ;
        if(error){
            reject(error.localizedDescription,error.localizedDescription,nil);
        }else{
            resolve(@{
                      @"latitude":@(location.location.coordinate.latitude),
                      @"longitude":@(location.location.coordinate.longitude),
                      @"country":location.rgcData.country?location.rgcData.country:@"",
                      @"city":location.rgcData.city?location.rgcData.city:@"",
                      @"province":location.rgcData.province?location.rgcData.province:@"",
                      @"district":location.rgcData.district?location.rgcData.district:@"",
                      @"street":location.rgcData.street?location.rgcData.street:@"",
                      @"streetNumber":location.rgcData.streetNumber?location.rgcData.streetNumber:@"",
                      @"cityCode":location.rgcData.adCode?location.rgcData.adCode:@"",
                      });
        }
        //获取经纬度和该定位点对应的位置信息
    }];

}

RCT_REMAP_METHOD(check,
                 resolve:(RCTPromiseResolveBlock)resolve
                 rejecte:(RCTPromiseRejectBlock)reject){
   BOOL gpsPermissionIsOpen= [CLLocationManager authorizationStatus] !=kCLAuthorizationStatusDenied;
    BOOL gpsIsOpen=[CLLocationManager locationServicesEnabled];
    resolve(@{@"gpsIsOpen":@(gpsIsOpen),
              @"gpsPermissionIsOpen":@(gpsPermissionIsOpen)
              });
}

RCT_REMAP_METHOD(toAppSetting,resolve:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject){
    NSURL *url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
    if ([[UIApplication sharedApplication] canOpenURL:url]) {
        [[UIApplication sharedApplication] openURL:url];
    }
}

- (void)onCheckPermissionState:(BMKLocationAuthErrorCode)iError{
    NSLog(@"%d",iError);
}

@end
  
