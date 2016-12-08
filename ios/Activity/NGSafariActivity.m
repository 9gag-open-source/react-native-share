//
//  NGSafariActivity.m
//  RNShare
//
//  Created by Lung on 7/12/2016.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "NGSafariActivity.h"
#import "RNShare.h"

@interface NGSafariActivity ()

@property (nonatomic, strong) NSURL *url;

@end

@implementation NGSafariActivity

- (NSString *)activityType {
    return NGOpenInSafariActivityType;
}

- (NSString *)activityTitle {
    return self.title;
}

- (UIImage *)activityImage {
    return self.image;
}

- (BOOL)canPerformWithActivityItems:(NSArray *)activityItems {
    for (id activityItem in activityItems) {
        if ([activityItem isKindOfClass:[NSURL class]] && [[UIApplication sharedApplication] canOpenURL:activityItem]) {
            return YES;
        }
    }
    return NO;
}

- (void)prepareWithActivityItems:(NSArray *)activityItems {
    for (id activityItem in activityItems) {
        if ([activityItem isKindOfClass:[NSURL class]]) {
            self.url = activityItem;
        }
    }
}

- (void)performActivity {
    BOOL completed = [[UIApplication sharedApplication] openURL:self.url];
    [self activityDidFinish:completed];
}

@end
