//
//  SMSShare.m
//  RNShare
//
//  Created by Lung on 20/1/2017.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import "SMSShare.h"

@implementation SMSShare

- (void)shareSingle:(NSDictionary *)options
 fromViewController:(nonnull UIViewController *)fromViewController
   composerDelegate:(nonnull id<NSObject>)composerDelegate
    failureCallback:(RCTResponseErrorBlock)failureCallback {
    
    if ([options objectForKey:@"message"] && [options objectForKey:@"message"] != [NSNull null]) {
        
        NSString *subject = @"";
        NSString *message = [RCTConvert NSString:options[@"message"]];
        
        if ([options objectForKey:@"subject"] && [options objectForKey:@"subject"] != [NSNull null]) {
            subject = [RCTConvert NSString:options[@"subject"]];
        }
        
        if ([options objectForKey:@"url"] && [options objectForKey:@"url"] != [NSNull null]) {
            NSString *url = [RCTConvert NSString:options[@"url"]];
            message = [message stringByAppendingString: [@" " stringByAppendingString: options[@"url"]] ];
        }
        
        MFMessageComposeViewController *controller = [[MFMessageComposeViewController alloc] init];
        if([MFMessageComposeViewController canSendText])
        {
            controller.subject = subject;
            controller.body = message;
            controller.messageComposeDelegate = composerDelegate;
            [fromViewController presentViewController:controller animated:YES completion:nil];
            
        } else {
            NSString *errorMessage = @"SMS is not configured";
            NSDictionary *userInfo = @{NSLocalizedFailureReasonErrorKey: NSLocalizedString(errorMessage, nil)};
            NSError *error = [NSError errorWithDomain:@"com.rnshare" code:1 userInfo:userInfo];
            
            NSLog(errorMessage);
            failureCallback(error);
        }
    }
}

@end
