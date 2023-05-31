/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React from 'react';

import {
  View,
  Text,
  Button,
  Alert,
  StyleSheet,
  NativeModules,
  Platform,
} from 'react-native';

const {ScreenUnlockModule} = NativeModules;
const PlatformOs = Platform.OS;
const Version = Platform.Version;
const reactNativeVersion = Platform.constants.reactNativeVersion;

const unlockApp = () => {
  ScreenUnlockModule.unlockApp((isUnlocked) => {
    console.log('Is App Unlocked:', isUnlocked);
    if (isUnlocked) {
      Alert.alert('App Unlocked');
      // Continue with your app logic...
    } else {
      Alert.alert(
        'Screen Lock Required',
        'Please set up a screen lock (PIN, pattern, or password) for your device.',
      );
    }
  });
};

const App = () => {
  console.log('PlatformOs' + PlatformOs);
  console.log('Version' + Version);

  console.log('iosBuild' + reactNativeVersion);
  return <Button title="Unlock Device" onPress={unlockApp} />;
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignContent: 'center',
    justifyContent: 'center',
  },
  text: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
  },
});

export default App;
