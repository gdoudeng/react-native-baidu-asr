package com.baidu.aip.asrwakeup3.core.wakeup.listener;


import com.baidu.aip.asrwakeup3.core.recog.IStatus;
import com.baidu.aip.asrwakeup3.core.util.MyLogger;
import com.baidu.aip.asrwakeup3.core.wakeup.WakeUpResult;

/**
 * Created by fujiayi on 2017/6/21.
 */

public class SimpleWakeupListener implements IWakeupListener, IStatus {

    private static final String TAG = "SimpleWakeupListener";

    /**
     * 唤醒的引擎当前的状态
     */
    protected int status = STATUS_WAITING_READY;

    @Override
    public void onSuccess(String word, WakeUpResult result) {
        status = STATUS_WAKEUP_SUCCESS;
        MyLogger.info(TAG, "唤醒成功，唤醒词：" + word);
    }

    @Override
    public void onStop() {
        status = STATUS_WAKEUP_EXIT;
        MyLogger.info(TAG, "唤醒词识别结束：");
    }

    @Override
    public void onError(int errorCode, String errorMessage, WakeUpResult result) {
        status = STATUS_ERROR;
        MyLogger.info(TAG, "唤醒错误：" + errorCode + ";错误消息：" + errorMessage + "; 原始返回" + result.getOrigalJson());
    }

    @Override
    public void onASrAudio(byte[] data, int offset, int length) {
        MyLogger.error(TAG, "audio data： " + data.length);
    }

}
