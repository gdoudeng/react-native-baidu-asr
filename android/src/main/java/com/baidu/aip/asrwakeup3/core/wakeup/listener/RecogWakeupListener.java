package com.baidu.aip.asrwakeup3.core.wakeup.listener;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.aip.asrwakeup3.core.wakeup.WakeUpResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fujiayi on 2017/9/21.
 */

public class RecogWakeupListener extends SimpleWakeupListener {

    private static final String TAG = "RecogWakeupListener";

    private Handler handler;

    public RecogWakeupListener(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onSuccess(String word, WakeUpResult result) {
        super.onSuccess(word, result);
        sendMessage(word);
    }

    @Override
    public void onStop() {
        super.onStop();
        sendMessage("唤醒词识别结束");
    }

    @Override
    public void onError(int errorCode, String errorMessage, WakeUpResult result) {
        super.onError(errorCode, errorMessage, result);
        try {
            JSONObject message = new JSONObject();
            message.put("errorCode", errorCode);
            message.put("errorMessage", errorMessage);
            message.put("result", result.getOrigalJson());
            sendMessage(message.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        if (handler == null) {
            Log.i(TAG, message);
            return;
        }
        Message msg = Message.obtain();
        msg.what = status;
        msg.arg1 = status;
        msg.obj = message;
        handler.sendMessage(msg);
    }
}
