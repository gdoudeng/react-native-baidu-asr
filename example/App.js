import * as React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator, TransitionPresets} from '@react-navigation/stack';
import BaiduAsrScreen from './src/BaiduAsrScreen';
import HomeScreen from './src/HomeScreen';
import BaiduWakeUpScreen from './src/BaiduWakeUpScreen';
import BaiduSynthesizerScreen from './src/BaiduSynthesizerScreen';

const Stack = createStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator
        screenOptions={{
          gestureEnabled: true,
          ...TransitionPresets.SlideFromRightIOS,
        }}>
        <Stack.Screen
          name="Home"
          component={HomeScreen}
          options={{
            headerShown: false,
          }}
        />
        <Stack.Screen
          name="BaiduAsr"
          component={BaiduAsrScreen}
          options={{
            title: '语音识别',
          }}
        />
        <Stack.Screen
          name="BaiduWakeUp"
          component={BaiduWakeUpScreen}
          options={{
            title: '语音唤醒',
          }}
        />
        <Stack.Screen
          name="BaiduSynthesizer"
          component={BaiduSynthesizerScreen}
          options={{
            title: '语音合成',
          }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
