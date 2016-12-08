/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

 import React, { Component } from 'react';
 import {
   AppRegistry,
   StyleSheet,
   Text,
   TouchableOpacity,
   View,
   Clipboard,
   Platform,
   NativeModules
 } from 'react-native';

 class DemoProject extends Component {
   constructor(props) {
     super(props);
   }

   render() {

     let options = {
       title: "React Native",
       message: "Hola mundo",
       url: "http://facebook.github.io/react-native/",
       subject: "Share Link", //  for email
       excludedActivityTypes: [
         "com.apple.UIKit.activity.AssignToContact",
         "com.apple.UIKit.activity.Print",
         "com.apple.UIKit.activity.AddToReadingList",
         "com.apple.UIKit.activity.SaveToCameraRoll",
         "com.apple.UIKit.activity.CopyToPasteboard"
       ],
       includedActivityTypes: [{
           "type": "com.9gag.RNShare.activity.copylink",
           "title": "Copy Link"
         }, {
           "type": "com.9gag.RNShare.activity.openInSafari",
           "title": "Open In Safari"
         }
       ]
     }

     return (
       <View style={styles.container}>
         <TouchableOpacity onPress={()=>{
           NativeModules.RNShare.open(options,(e) => {
             return reject({ error: e });
           },(e) => {
            //  resolve({
            //    message: e
            //  });
           });
         }}>
           <View style={styles.instructions}>
             <Text>My Share</Text>
           </View>
         </TouchableOpacity>
       </View>
     );
   }
 }

 const styles = StyleSheet.create({
   container: {
     flex: 1,
     justifyContent: 'center',
     alignItems: 'center',
     backgroundColor: '#F5FCFF',
   },
   instructions: {
     marginTop: 20,
     marginBottom: 20,
   },
 });

 AppRegistry.registerComponent('DemoProject', () => DemoProject);
