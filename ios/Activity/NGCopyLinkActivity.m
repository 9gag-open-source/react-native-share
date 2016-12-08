//
//  NGCopyLinkActivity.m
//  RNShare
//
//  Created by Lung on 7/12/2016.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "NGCopyLinkActivity.h"
#import "RNShare.h"

@interface NGCopyLinkActivity ()

@property (nonatomic, strong) NSURL *url;

@end


@implementation NGCopyLinkActivity

- (NSString *)activityType {
    return NGCopyLinkActivityType;
}

- (NSString *)activityTitle {
    return self.title;
}

- (UIImage *)activityImage {
    return self.image;
}

- (BOOL)canPerformWithActivityItems:(NSArray *)activityItems {
    for (id activityItem in activityItems) {
        if ([activityItem isKindOfClass:[NSURL class]]) {
            return YES;
        }
    }
    return NO;
}

- (void)prepareWithActivityItems:(NSArray *)activityItems {
    for (id activityItems in activityItems) {
        if ([activityItems isKindOfClass:[NSURL class]]) {
            self.url = (NSURL *)activityItems;
        }
    }
}

- (void)performActivity {
    if (self.url) {
        UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
        pasteboard.string = self.url.absoluteString;
    }
    [self activityDidFinish:YES];
}

@end
