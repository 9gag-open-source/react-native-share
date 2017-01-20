//
//  FacebookShare.m
//  RNShare
//
//  Created by Diseño Uno BBCL on 23-07-16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "WhatsAppShare.h"
#import "NSString+Encoded.h"

@implementation WhatsAppShare

- (void)shareSingle:(NSDictionary *)options
    failureCallback:(RCTResponseErrorBlock)failureCallback
    successCallback:(RCTResponseSenderBlock)successCallback {

    NSLog(@"Try open view");

    if ([options objectForKey:@"message"] && [options objectForKey:@"message"] != [NSNull null]) {
        NSString *text = [RCTConvert NSString:options[@"message"]];
        NSString *fullText = [[text stringByAppendingString:@" "] stringByAppendingString:options[@"url"]];
        NSString *encodedText = [fullText urlencode];
        NSURL *whatsappURL = [NSURL URLWithString:[NSString stringWithFormat:@"whatsapp://send?text=%@", encodedText]];

        if ([[UIApplication sharedApplication] canOpenURL: whatsappURL]) {
            [[UIApplication sharedApplication] openURL: whatsappURL];
            successCallback(@[]);
        } else {
          // Cannot open whatsapp
          NSString *stringURL = @"http://itunes.apple.com/en/app/whatsapp-messenger/id310633997";
          NSURL *url = [NSURL URLWithString:stringURL];
          [[UIApplication sharedApplication] openURL:url];

          NSString *errorMessage = @"Not installed";
          NSDictionary *userInfo = @{NSLocalizedFailureReasonErrorKey: NSLocalizedString(errorMessage, nil)};
          NSError *error = [NSError errorWithDomain:@"com.rnshare" code:1 userInfo:userInfo];

          NSLog(errorMessage);
          failureCallback(error);
        }
    }

}


@end
