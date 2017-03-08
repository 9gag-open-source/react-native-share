@import UIKit;

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#else
#import "RCTBridgeModule.h"
#endif

UIKIT_EXTERN NSString * const NGCopyLinkActivityType;
UIKIT_EXTERN NSString * const NGOpenInSafariActivityType;

@interface RNShare : NSObject <RCTBridgeModule>

@end
