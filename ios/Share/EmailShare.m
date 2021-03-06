//
//  EmailShare.m
//  RNShare
//
//  Created by Diseño Uno BBCL on 23-07-16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "EmailShare.h"

@implementation EmailShare
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
        
        if ([MFMailComposeViewController canSendMail]){
            MFMailComposeViewController *mc = [[MFMailComposeViewController alloc] init];
            mc.mailComposeDelegate = composerDelegate;
            [mc setSubject:subject];
            [mc setMessageBody:message isHTML:NO];
            
            [fromViewController presentViewController:mc animated:YES completion:Nil];
            
        } else {
            NSString *errorMessage = @"Email is not configured";
            NSDictionary *userInfo = @{NSLocalizedFailureReasonErrorKey: NSLocalizedString(errorMessage, nil)};
            NSError *error = [NSError errorWithDomain:@"com.rnshare" code:1 userInfo:userInfo];
            
            NSLog(errorMessage);
            failureCallback(error);
        }
        
//        NSString * urlWhats = [NSString stringWithFormat:@"mailto:?subject=%@&body=%@", subject, message ];
//        NSURL * whatsappURL = [NSURL URLWithString:[urlWhats stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
//
//        if ([[UIApplication sharedApplication] canOpenURL: whatsappURL]) {
//            [[UIApplication sharedApplication] openURL: whatsappURL];
//            successCallback(@[]);
//        } else {
//            // Cannot open email
//            NSLog(@"Cannot open email");
//        }
    }
}

@end
