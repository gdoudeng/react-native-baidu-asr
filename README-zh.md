# react-native-baidu-asr

<p align="center">
  <a href="https://www.npmjs.com/package/react-native-baidu-asr">
    <img src="https://img.shields.io/npm/v/react-native-baidu-asr" alt="react-native-baidu-asr">
  </a>
  <a href="https://www.npmjs.com/package/react-native-baidu-asr">
    <img src="https://img.shields.io/npm/dm/react-native-baidu-asr" alt="react-native-baidu-asr">
  </a>
  <a href="https://github.com/gdoudeng/react-native-baidu-asr/issues">
    <img src="https://img.shields.io/github/issues/gdoudeng/react-native-baidu-asr" alt="issues">
  </a>
  <a href="https://opensource.org/licenses/MIT">
    <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License: MIT">
  </a>
  <a href="#badge">
    <img alt="semantic-release" src="https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg">
  </a>
</p>

`react-native-baidu-asr` 是一个 React Native 下的百度语音库，可以进行语音识别以及语音唤醒。

[English](./README.md) | 简体中文

## 预览

<p align="left">
  <img width=360 title="预览" src="./sreenshot/asr.gif" alt="预览">
  <img width=360 title="预览" src="./sreenshot/wakeup.gif" alt="预览">
</p>

## 支持平台

- React Native >= 0.47.0
- Android

当前并未实现iOS平台，我有空一定补上，还有语音合成会补上。

## 安装

- 对于RN >= 0.60

1. `yarn add react-native-baidu-asr`

- 对于RN < 0.60

1. `yarn add react-native-baidu-asr`

2. `react-native link react-native-baidu-asr`

## 使用

- 详见[example](https://github.com/gdoudeng/react-native-baidu-asr/tree/master/example)

首先是你先要去 [百度语音控制台](https://console.bce.baidu.com/ai/?_=1620713753811&fromai=1#/ai/speech/overview/index)
创建一个应用，拿到鉴权信息：AppID，API Key，Secret Key。

- 语音识别

```typescript
import {
  BaiduAsr,
  StatusCode,
  IBaseData,
  RecognizerResultError,
  RecognizerResultData,
  VolumeData
} from 'react-native-baidu-asr';

// 初始化百度语音引擎
BaiduAsr.init({
  APP_ID: '你的鉴权信息AppID',
  APP_KEY: '你的鉴权信息API Key',
  SECRET: '你的鉴权信息Secret Key',
});

// 处理识别结果
this.resultListener = BaiduAsr.addResultListener(this.onRecognizerResult);
// 处理错误结果
this.errorListener = BaiduAsr.addErrorListener(this.onRecognizerError);
// 处理音量大小
this.volumeListener = BaiduAsr.addAsrVolumeListener(this.onAsrVolume);

// 开始语音识别
// 更多输入参数请参考百度语音文档
// https://ai.baidu.com/ai-doc/SPEECH/bkh07sd0m#asr_start-%E8%BE%93%E5%85%A5%E4%BA%8B%E4%BB%B6%E5%8F%82%E6%95%B0
BaiduAsr.start({
  // 长语音
  VAD_ENDPOINT_TIMEOUT: 0,
  BDS_ASR_ENABLE_LONG_SPEECH: true,
  // 禁用标点符号
  DISABLE_PUNCTUATION: true,
});
```

- 语音唤醒

```typescript
import { BaiduWakeUp } from 'react-native-baidu-asr';

// 初始化百度语音引擎
BaiduWakeUp.init({
  APP_ID: '你的鉴权信息AppID',
  APP_KEY: '你的鉴权信息API Key',
  SECRET: '你的鉴权信息Secret Key',
});

// 唤醒结果
this.resultListener = BaiduWakeUp.addResultListener(this.onWakeUpResult);
// 处理错误结果
this.errorListener = BaiduWakeUp.addErrorListener(this.onWakeUpError);

// 开始语音唤醒
// 更多输入参数请参考百度语音文档
// https://ai.baidu.com/ai-doc/SPEECH/bkh07sd0m#wakeup_start-%E8%BE%93%E5%85%A5%E4%BA%8B%E4%BB%B6%E5%8F%82%E6%95%B0
BaiduWakeUp.start({
  //表示WakeUp.bin文件定义在assets目录下
  WP_WORDS_FILE: 'assets:///WakeUp.bin',
});
```

## API

### 语音识别

#### Methods

- `BaiduAsr.init(options: InitOptions)`

初始化百度语音引擎

- `BaiduAsr.start(options: AsrOptions)`

开始语音识别

- `BaiduAsr.stop()`

暂停录音，SDK不会再识别停止后的录音。

- `BaiduAsr.cancel()`

取消录音，SDK会取消本次识别，回到原始状态。

- `BaiduAsr.release()`

释放资源，下次需要再次使用的话必须再调用`init`方法初始化引擎。

#### Events

识别结果回调数据有一个统一格式的，类似与api接口返回一样，有code，msg，data。

`IBaseData`数据类型如下：

```typescript
interface IBaseData<T = any> {
  /**
   * 状态码
   */
  code: StatusCode,
  /**
   * 消息
   */
  msg: string,
  /**
   * 数据
   */
  data: T
}
```

- `addResultListener(callback: (data: IBaseData<RecognizerResultData | undefined>) => void): EmitterSubscription`  
  语音识别结果回调，在语音识别时会不断触发该事件，`data` 为 `IBaseData<RecognizerResultData | undefined>` 类型，其值：

    - `code`：状态码
    - `msg`：消息
    - `data`：识别数据

其中`data`数据类型如下：

```typescript
interface RecognizerResultData {
  best_result: string,
  // 如无意外 取第一个值就是识别结果
  results_recognition: Array<string>,
  result_type: ResultType,
  origin_result: {
    corpus_no: number,
    err_no: number,
    raf: number,
    result: {
      word: Array<string>
    },
    sn: string
  },
  error: number,
  desc: string
}
```

- `addErrorListener(callback: (data: IBaseData<RecognizerResultError>) => void): EmitterSubscription`  
  语音识别出现错误，错误信息与百度语音文档保持一致，其值：

    - `code`：状态码
    - `msg`：消息
    - `data`：错误数据

其中`data`数据类型如下：

```typescript
interface RecognizerResultError {
  errorCode: number // 错误码对照百度语音文档 https://ai.baidu.com/ai-doc/SPEECH/qk38lxh1q
  subErrorCode: number
  descMessage: string
}
```

- `addAsrVolumeListener(listener: (volume: VolumeData) => void): EmitterSubscription`  
  语音识别的音量大小，当识别的语音改变音量时会触发该事件，`volume` 为 `VolumeData` 类型，其值：

    - `volumePercent`: 当前音量百分比
    - `volume`: 当前音量大小

### 语音唤醒

首先是先导出 [唤醒词](https://ai.baidu.com/tech/speech/wake#tech-demo) ，预定义唤醒词和自定义唤醒词，均需通过唤醒词评估工具进行导出使用。

#### Methods

- `BaiduWakeUp.init(options: InitOptions)`

初始化百度语音引擎

- `BaiduWakeUp.start(options: WakeUpOptions)`

开始语音唤醒

- `BaiduWakeUp.stop()`

结束语音唤醒。

- `BaiduWakeUp.release()`

释放资源，下次需要再次使用的话必须再调用`init`方法初始化引擎。

#### Events

唤醒结果回调数据有一个统一格式的，类似与api接口返回一样，有code，msg，data。

`IBaseData`数据类型如下：

```typescript
interface IBaseData<T = any> {
  /**
   * 状态码
   */
  code: StatusCode,
  /**
   * 消息
   */
  msg: string,
  /**
   * 数据
   */
  data: T
}
```

- `addResultListener(callback: (data: IBaseData<string | undefined>) => void): EmitterSubscription`  
  语音唤醒结果回调，`data` 为 `IBaseData<string | undefined>` 类型，其值：

    - `code`：状态码
    - `msg`：消息
    - `data`：唤醒词

- `addErrorListener(callback: (data: IBaseData<WakeUpResultError>) => void): EmitterSubscription`  
  语音唤醒出现错误，错误信息与百度语音文档保持一致，其值：

    - `code`：状态码
    - `msg`：消息
    - `data`：错误数据

其中`data`数据类型如下：

```typescript
interface WakeUpResultError {
  // 错误码 可以对照百度语音文档查找错误码 https://ai.baidu.com/ai-doc/SPEECH/qk38lxh1q#%E5%94%A4%E9%86%92%E9%94%99%E8%AF%AF%E7%A0%81
  errorCode: number,
  // 错误消息
  errorMessage: string,
  // 百度语音返回的原初错误数据
  result: string
}
```

## 减少apk体积

如果直接接入的话，你会发现apk包体积会瞬间暴增了10几兆，这是因为现在有 [5个架构](https://ai.baidu.com/ai-doc/SPEECH/dk38lxg4d#ndk-so%E5%BA%93%E6%9E%B6%E6%9E%84)
目录：`armeabi`，`armeabi-v7a`，`arm64-v8a`，`x86`，`x86_64`

而且语音识别与语音唤醒都要有（语音合成待完成），所以每个架构都的so文件都要保留，但是没必要每个架构都有的其实，现在国内应用市场是仍然需要上传32位架构的apk，但是Google市场早就要求上传64位了，国内小米，oppo，vivo也说开始联手强制开发者以后必须且只能上传64位结构的apk。[安卓APP升级64位架构的相关通知](https://open.oppomobile.com/service/message/detail?id=229507)
。

所以目前我建议是分开架构打包，32位和64位分开打包，这样可以减少包体积，但是注意如果你接入了其他sdk，那么也要保证有对应架构的so文件。

具体操作可以参考example的[build.gradle](https://github.com/gdoudeng/react-native-baidu-asr/blob/master/example/android/app/build.gradle)

## Contribute

期待提出有关建议，欢迎做出贡献，感谢star。

[Github](https://github.com/gdoudeng/react-native-baidu-asr)

## License

[MIT License](https://github.com/gdoudeng/react-native-baidu-asr/blob/master/LICENSE)
