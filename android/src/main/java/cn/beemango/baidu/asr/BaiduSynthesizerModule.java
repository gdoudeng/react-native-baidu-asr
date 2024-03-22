package cn.beemango.baidu.asr;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.beemango.baidu.asr.control.InitConfig;
import cn.beemango.baidu.asr.control.MySyntherizer;
import cn.beemango.baidu.asr.control.NonBlockSyntherizer;
import cn.beemango.baidu.asr.listener.UiMessageListener;
import cn.beemango.baidu.asr.util.Auth;
import cn.beemango.baidu.asr.util.AutoCheck;
import cn.beemango.baidu.asr.util.IOfflineResourceConst;
import cn.beemango.baidu.asr.util.MainHandlerConstant;

/**
 * 提供百度语音合成 React Native 接口
 */
public class BaiduSynthesizerModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static final String TAG = "BaiduSynthesizerModule";

    private final ReactApplicationContext mReactApplicationContext;
    private DeviceEventManagerModule.RCTDeviceEventEmitter mEventEmitter;
    // 主控制类，所有合成控制方法从这个类开始
    private MySyntherizer synthesizer;

    // 百度语音鉴权的信息
    private String APP_ID;
    private String APP_KEY;
    private String SECRET;

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； TtsMode.OFFLINE 纯离线合成，需要纯离线SDK
    protected TtsMode ttsMode = IOfflineResourceConst.DEFAULT_SDK_TTS_MODE;

    public BaiduSynthesizerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactApplicationContext = reactContext;
        reactContext.addLifecycleEventListener(this);

        try {
            Auth.getInstance(mReactApplicationContext.getApplicationContext());
        } catch (Auth.AuthCheckException e) {
            e.printStackTrace();
            return;
        }

        APP_ID = Auth.getInstance(mReactApplicationContext.getApplicationContext()).getAppId();
        APP_KEY = Auth.getInstance(mReactApplicationContext.getApplicationContext()).getAppKey();
        SECRET = Auth.getInstance(mReactApplicationContext.getApplicationContext()).getSecretKey();
        // sn = Auth.getInstance(mReactApplicationContext.getApplicationContext()).getSn(); // 离线合成SDK必须有此参数；在线合成SDK没有此参数
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {
        release();
    }

    //释放资源
    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        release();
    }

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    @ReactMethod
    public void initialTts(final ReadableMap options) {
        if (synthesizer != null) {
            return;
        }
        LoggerProxy.printable(BuildConfig.DEBUG); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        Handler mainHandler = new SynthesizerHandler(this);
        SpeechSynthesizerListener listener = new UiMessageListener(mainHandler);
        InitConfig config = getInitConfig(listener, options);
        synthesizer = new NonBlockSyntherizer(mReactApplicationContext.getApplicationContext(), config, mainHandler); // 此处可以改为MySyntherizer 了解调用过程
    }

    @ReactMethod
    public void addListener(String eventName) {
        // Keep: Required for RN built in Event Emitter Calls.
    }
    @ReactMethod
    public void removeListeners(Integer count) {
        // Keep: Required for RN built in Event Emitter Calls.
    }

    private InitConfig getInitConfig(SpeechSynthesizerListener listener, final ReadableMap options) {
        Map<String, String> params = getParams(options);
        // 添加你自己的参数
        InitConfig initConfig;
        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        initConfig = new InitConfig(APP_ID, APP_KEY, SECRET, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        if (BuildConfig.DEBUG) {
            AutoCheck.getInstance(mReactApplicationContext.getApplicationContext()).check(initConfig, new MyHandler());
        }
        return initConfig;
    }

    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     * {@see https://ai.baidu.com/ai-doc/SPEECH/Pk8446an5}
     *
     * @return 合成参数Map
     */
    private Map<String, String> getParams(final ReadableMap options) {
        Map<String, String> params = new HashMap<>();
        if (options != null) {
            // 仅在线生效，在线的发音
            if (options.hasKey("PARAM_SPEAKER")) {
                params.put(SpeechSynthesizer.PARAM_SPEAKER, options.getString("PARAM_SPEAKER"));
            }
            // 在线合成的音量 。范围["0" - "15"], 不支持小数。 "0" 最轻，"15" 最响 默认 "5"
            if (options.hasKey("PARAM_VOLUME")) {
                params.put(SpeechSynthesizer.PARAM_VOLUME, options.getString("PARAM_VOLUME"));
            }
            // 在线合成的语速 。范围["0" - "15"], 不支持小数。 "0" 最慢，"15" 最快 默认 "5"
            if (options.hasKey("PARAM_SPEED")) {
                params.put(SpeechSynthesizer.PARAM_SPEED, options.getString("PARAM_SPEED"));
            }
            // 在线合成的语调 。范围["0" - "15"], 不支持小数。 "0" 最低沉， "15" 最尖 默认 "5"
            if (options.hasKey("PARAM_PITCH")) {
                params.put(SpeechSynthesizer.PARAM_PITCH, options.getString("PARAM_PITCH"));
            }
            // 不使用改参数即可。SDK与服务器音频传输格式，与 PARAMAUDIO_RATE参数一起使用。可选值为SpeechSynthesizer.AUDIO_ENCODE*， 其中SpeechSynthesizer.AUDIO_ENCODE_PCM为不压缩
            if (options.hasKey("PARAM_AUDIO_ENCODE")) {
                params.put(SpeechSynthesizer.PARAM_AUDIO_ENCODE, options.getString("PARAM_AUDIO_ENCODE"));
            }
            // 不使用改参数即可。SDK与服务器音频传输格式，与 PARAMAUDIO_ENCODE参数一起使用。可选值为SpeechSynthesizer.AUDIO_BITRATE*, 其中SpeechSynthesizer.AUDIO_BITRATE_PCM 为不压缩传输
            if (options.hasKey("PARAM_AUDIO_RATE")) {
                params.put(SpeechSynthesizer.PARAM_AUDIO_RATE, options.getString("PARAM_AUDIO_RATE"));
            }
        }
        return params;
    }

    /**
     * 合成并播放
     */
    @ReactMethod
    public void speak(String text, String utteranceId, final ReadableMap options, Callback callback) {
        if (!TextUtils.isEmpty(text) && synthesizer != null) {
            // 合成前可以修改参数：
            Map<String, String> params = getParams(options);
            synthesizer.setParams(params);
            // int result = synthesizer.speak(text);
            int result;
            if (utteranceId == null) {
                result = synthesizer.speak(text);
            } else {
                result = synthesizer.speak(text, utteranceId);
            }

            if (callback != null) {
                callback.invoke(result);
            }
        }
    }

    /**
     * 批量播放
     *
     * @param textArray 字符串数组
     * @param options   自定义播放参数
     * @param callback  播放回调
     */
    @ReactMethod
    public void batchSpeak(final ReadableArray textArray, final ReadableMap options, Callback callback) {
        if (textArray != null && textArray.size() != 0 && synthesizer != null) {
            List<Pair<String, String>> texts = new ArrayList<>();

            for (int i = 0; i < textArray.size(); i++) {
                texts.add(new Pair<>(textArray.getString(i), String.valueOf(i)));
            }
            Map<String, String> params = getParams(options);
            synthesizer.setParams(params);
            int result = synthesizer.batchSpeak(texts);
            if (callback != null) {
                callback.invoke(result);
            }
        }
    }

    /**
     * 暂停播放。仅调用speak后生效
     */
    @ReactMethod
    public void pause(Callback callback) {
        if (synthesizer != null) {
            int result = synthesizer.pause();
            if (callback != null) {
                callback.invoke(result);
            }
        }
    }

    /**
     * 继续播放。仅调用speak后生效，调用pause生效
     */
    @ReactMethod
    public void resume(Callback callback) {
        if (synthesizer != null) {
            int result = synthesizer.resume();
            if (callback != null) {
                callback.invoke(result);
            }
        }
    }

    /*
     * 停止合成引擎。即停止播放，合成，清空内部合成队列。
     */
    @ReactMethod
    public void stop(Callback callback) {
        if (synthesizer != null) {
            int result = synthesizer.stop();
            if (callback != null) {
                callback.invoke(result);
            }
        }
    }

    @ReactMethod
    public void release() {
        if (synthesizer != null) {
            synthesizer.release();
            // 这个垃圾东西貌似释放的不会很及时 立马再重新new一个会报错
            synthesizer = null;
        }
    }

    private void handleMsg(Message msg) {
        WritableMap params = Arguments.createMap();
        switch (msg.what) {
            case MainHandlerConstant.STATUS_NONE:
                params.putInt("code", MainHandlerConstant.STATUS_NONE);
                params.putString("msg", "初始状态");
                break;
            case MainHandlerConstant.INIT_SUCCESS:
                params.putInt("code", MainHandlerConstant.INIT_SUCCESS);
                params.putString("msg", "合成引擎初始化成功");
                break;
            case MainHandlerConstant.STATUS_SYNTHESIZE_START:
                params.putInt("code", MainHandlerConstant.STATUS_SYNTHESIZE_START);
                params.putString("msg", "开始合成");
                break;
            case MainHandlerConstant.STATUS_SYNTHESIZE_PROCESSING:
                params.putInt("code", MainHandlerConstant.STATUS_SYNTHESIZE_PROCESSING);
                params.putString("msg", "正在合成中");
                break;
            case MainHandlerConstant.STATUS_SYNTHESIZE_FINISH:
                params.putInt("code", MainHandlerConstant.STATUS_SYNTHESIZE_FINISH);
                params.putString("msg", "合成结束");
                break;
            case MainHandlerConstant.STATUS_SPEAK_START:
                params.putInt("code", MainHandlerConstant.STATUS_SPEAK_START);
                params.putString("msg", "开始播放");
                break;
            case MainHandlerConstant.STATUS_SPEAKING:
                params.putInt("code", MainHandlerConstant.STATUS_SPEAKING);
                params.putString("msg", "正在播放");
                break;
            case MainHandlerConstant.STATUS_SPEAK_FINISH:
                params.putInt("code", MainHandlerConstant.STATUS_SPEAK_FINISH);
                params.putString("msg", "播放结束");
                break;
            case MainHandlerConstant.STATUS_ERROR:
                params.putInt("code", MainHandlerConstant.STATUS_ERROR);
                params.putString("msg", "合成错误");
            default:
                break;
        }

        if (msg.obj != null) {
            params.putString("data", msg.obj.toString());
        } else {
            params.putNull("data");
        }

        if (msg.what == MainHandlerConstant.STATUS_ERROR) {
//            Log.w("错误回调 ", params.toString());
            onJSEvent("onSynthesizerError", params);
        } else {
//            Log.w("结果回调 ", params.toString());
            onJSEvent("onSynthesizerResult", params);
        }
    }

    /**
     * 发送事件给js层
     *
     * @param eventName 事件名
     * @param data      数据
     */
    private void onJSEvent(String eventName, Object data) {
        if (mEventEmitter == null) {
            mEventEmitter = mReactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
        }
        mEventEmitter.emit(eventName, data);
    }

    /**
     * 静态匿名内部类不会持有一个对外部类的隐式引用，因此Activity将不会被泄露
     */
    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 100) {
                AutoCheck autoCheck = (AutoCheck) msg.obj;
                synchronized (autoCheck) {
                    String message = autoCheck.obtainDebugMessage();
                    Log.w("AutoCheckMessage", message);
                }
            }
        }
    }

    static class SynthesizerHandler extends Handler {
        private final WeakReference<BaiduSynthesizerModule> baiduSynthesizerModuleWeakReference;

        public SynthesizerHandler(BaiduSynthesizerModule baiduSynthesizerModule) {
            this.baiduSynthesizerModuleWeakReference = new WeakReference<>(baiduSynthesizerModule);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            BaiduSynthesizerModule baiduSynthesizerModule = this.baiduSynthesizerModuleWeakReference.get();
            baiduSynthesizerModule.handleMsg(msg);
        }
    }
}
