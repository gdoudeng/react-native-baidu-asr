import { EmitterSubscription, NativeEventEmitter, NativeModules } from "react-native";
import {
  AsrOptions,
  EventName,
  InitOptions,
  IBaseData,
  RecognizerResultData,
  RecognizerResultError,
  VolumeData
} from "./types";

export * from './types';

const BaiduAsrModule = NativeModules.BaiduAsrModule;
const eventEmitter = new NativeEventEmitter(BaiduAsrModule);

/**
 * 提供百度语音识别 React Native 接口
 */
export default class BaiduAsr {
  /**
   * 初始化百度语音引擎
   * @param options 鉴权信息
   */
  static init(options: InitOptions) {
    BaiduAsrModule.init(options);
  }

  /**+
   * 开始进行语音识别
   * 可传入参数说明见百度语音文档 {@see https://ai.baidu.com/ai-doc/SPEECH/bkh07sd0m#asr_start-%E8%BE%93%E5%85%A5%E4%BA%8B%E4%BB%B6%E5%8F%82%E6%95%B0}
   * @param options ASR_START 输入事件参数
   */
  static start(options: AsrOptions) {
    BaiduAsrModule.start(options);
  }

  /**
   * 暂停录音
   * SDK不会再识别停止后的录音。
   */
  static stop() {
    BaiduAsrModule.stop();
  }

  /**
   * 取消录音
   * SDK会取消本次识别，回到原始状态。
   */
  static cancel() {
    BaiduAsrModule.cancel();
  }

  /**
   * 释放资源
   * 下次需要再次使用的话必须再调用{@link init}方法初始化引擎
   */
  static release() {
    BaiduAsrModule.release();
  }

  /**
   * 状态回调
   * @param eventName
   * @param cb
   * @private
   */
  private static addListener(eventName: EventName, cb: (data: IBaseData) => void): EmitterSubscription {
    return eventEmitter.addListener(eventName, (data: IBaseData<string | undefined>) => {
      // java传过来的是字符串
      if (data.data && typeof data.data === "string") {
        data.data = JSON.parse(data.data);
      }
      cb(data);
    });
  }

  /**
   * 识别结果回调
   * @param callback
   */
  static addResultListener(callback: (data: IBaseData<RecognizerResultData | undefined>) => void): EmitterSubscription {
    return this.addListener(EventName.onRecognizerResult, callback);
  }

  /**
   * 识别错误回调
   * @param callback
   */
  static addErrorListener(callback: (data: IBaseData<RecognizerResultError>) => void): EmitterSubscription {
    return this.addListener(EventName.onRecognizerError, callback);
  }

  /**
   * 音量大小回调
   * @param listener
   */
  static addAsrVolumeListener(listener: (volume: VolumeData) => void): EmitterSubscription {
    return eventEmitter.addListener(EventName.onAsrVolume, listener);
  }
}
