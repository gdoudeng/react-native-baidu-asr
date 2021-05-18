import { EmitterSubscription, NativeEventEmitter, NativeModules } from "react-native";
import {
  EventName,
  InitOptions,
  IBaseData,
  StatusCode,
  WakeUpOptions,
  WakeUpResultError
} from "./types";

const BaiduWakeUpModule = NativeModules.BaiduWakeUpModule;
const eventEmitter = new NativeEventEmitter(BaiduWakeUpModule);

/**
 * 提供百度语音唤醒 React Native 接口
 */
export default class BaiduWakeUp {
  /**
   * 初始化百度语音引擎
   * @param options 鉴权信息
   */
  static init(options: InitOptions) {
    BaiduWakeUpModule.init(options);
  }

  /**+
   * 开始进行语音识别
   * 可传入参数说明见百度语音文档 {@see https://ai.baidu.com/ai-doc/SPEECH/bkh07sd0m#asr_start-%E8%BE%93%E5%85%A5%E4%BA%8B%E4%BB%B6%E5%8F%82%E6%95%B0}
   * @param options WAKEUP_START 输入事件参数
   */
  static start(options: WakeUpOptions) {
    BaiduWakeUpModule.start(options);
  }

  /**
   * 停止唤醒
   */
  static stop() {
    BaiduWakeUpModule.stop();
  }

  /**
   * 释放资源
   * 下次需要再次使用的话必须再调用{@link init}方法初始化引擎
   */
  static release() {
    BaiduWakeUpModule.release();
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
      if (data.code && data.code === StatusCode.STATUS_ERROR && typeof data.data === "string") {
        try {
          data.data = JSON.parse(data.data);
        } catch (e) {
          console.log(e)
        }
      }
      cb(data);
    });
  }

  /**
   * 唤醒结果回调
   * @param callback
   */
  static addResultListener(callback: (data: IBaseData<string | undefined>) => void): EmitterSubscription {
    return this.addListener(EventName.onWakeUpResult, callback);
  }

  /**
   * 唤醒错误回调
   * @param callback
   */
  static addErrorListener(callback: (data: IBaseData<WakeUpResultError>) => void): EmitterSubscription {
    return this.addListener(EventName.onWakeUpError, callback);
  }

}
