package com.baidu.aip.asrwakeup3.core.recog.listener;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.aip.asrwakeup3.core.recog.RecogResult;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fujiayi on 2017/6/16.
 */

public class MessageStatusRecogListener extends StatusRecogListener {
    private Handler handler;

    private long speechEndTime = 0;

    private boolean needTime = false;

    private static final String TAG = "MesStatusRecogListener";

    public MessageStatusRecogListener(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onAsrReady() {
        super.onAsrReady();
        speechEndTime = 0;
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_WAKEUP_READY, null);
    }

    @Override
    public void onAsrBegin() {
        super.onAsrBegin();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN, null);
    }

    @Override
    public void onAsrEnd() {
        super.onAsrEnd();
        speechEndTime = System.currentTimeMillis();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_END, null);
    }

    @Override
    public void onAsrPartialResult(String[] results, RecogResult recogResult) {
        super.onAsrPartialResult(results, recogResult);
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, recogResult.getOrigalJson());
    }

    @Override
    public void onAsrFinalResult(String[] results, RecogResult recogResult) {
        super.onAsrFinalResult(results, recogResult);
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, recogResult.getOrigalJson());
        if (speechEndTime > 0) {
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - speechEndTime;
            Log.d(TAG, "说话结束到识别结束耗时【" + diffTime + "ms】");
        }
        speechEndTime = 0;
    }

    @Override
    public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage, RecogResult recogResult) {
        super.onAsrFinishError(errorCode, subErrorCode, descMessage, recogResult);
        // String message = "【asr.finish事件】识别错误, 错误码：" + errorCode + " ," + subErrorCode + " ; " + descMessage;
        try {
            JSONObject message = new JSONObject();
            message.put("errorCode", errorCode);
            message.put("subErrorCode", subErrorCode);
            message.put("descMessage", descMessage);
            sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_ERROR, message.toString());
            if (speechEndTime > 0) {
                long diffTime = System.currentTimeMillis() - speechEndTime;
                Log.d(TAG, "说话结束到识别结束耗时【" + diffTime + "ms】");
            }
            speechEndTime = 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAsrOnlineNluResult(String nluResult) {
        super.onAsrOnlineNluResult(nluResult);
        if (!nluResult.isEmpty()) {
            sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, nluResult);
        }
    }

    @Override
    public void onAsrFinish(RecogResult recogResult) {
        super.onAsrFinish(recogResult);
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_FINISH, recogResult.getOrigalJson());
    }

    /**
     * 长语音识别结束
     */
    @Override
    public void onAsrLongFinish() {
        super.onAsrLongFinish();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_LONG_SPEECH, null);
    }


    /**
     * 使用离线命令词时，有该回调说明离线语法资源加载成功
     */
    @Override
    public void onOfflineLoaded() {
        super.onOfflineLoaded();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_LOADED, "离线资源加载成功。没有此回调可能离线语法功能不能使用。");
    }

    /**
     * 使用离线命令词时，有该回调说明离线语法资源加载成功
     */
    @Override
    public void onOfflineUnLoaded() {
        super.onOfflineUnLoaded();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_UNLOADED, "离线资源卸载成功。");
    }

    @Override
    public void onAsrExit() {
        super.onAsrExit();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_EXIT, null);
    }

    @Override
    public void onAsrVolume(int volumePercent, int volume) {
        super.onAsrVolume(volumePercent, volume);
        sendVolumeMessage(volumePercent, volume);
    }

    private void sendStatusMessage(String eventName, String message) {
        // message = "[" + eventName + "]" + message;
        sendMessage(message, status);
    }

    private void sendMessage(String message) {
        sendMessage(message, WHAT_MESSAGE_STATUS);
    }

    private void sendMessage(String message, int what) {
        sendMessage(message, what, false);
    }

    private void sendMessage(String message, int what, boolean highlight) {
        if (needTime && what != STATUS_FINISHED) {
            message += " ;time= " + System.currentTimeMillis();
        }
        if (handler == null) {
            Log.i(TAG, message);
            return;
        }
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = status;
        if (highlight) {
            msg.arg2 = 1;
        }
        msg.obj = message;
        handler.sendMessage(msg);
    }

    private void sendVolumeMessage(int volumePercent, int volume) {
        Message msg = Message.obtain();
        msg.what = STATUS_ASR_VOLUME;
        msg.arg1 = volumePercent;
        msg.arg2 = volume;
        handler.sendMessage(msg);
    }
}
