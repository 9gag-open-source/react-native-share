//
//  SMSShare.h
//  RNShare
//
//  Created by Lung on 20/1/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RCTConvert.h"
#import "RCTBridge.h"
#import "RCTUIManager.h"
#import "RCTLog.h"
#import "RCTUtils.h"
#import <MessageUI/MessageUI.h>

@interface SMSShare : NSObject

- (void)shareSingle:(NSDictionary *)options
 fromViewController:(UIViewController *)fromViewController
   composerDelegate:(id<NSObject>)composerDelegate
    failureCallback:(RCTResponseErrorBlock)failureCallback;

@end
