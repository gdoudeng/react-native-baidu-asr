package cn.beemango.baidu.asr.util;

import com.baidu.tts.client.TtsMode;

public interface IOfflineResourceConst {

    TtsMode DEFAULT_SDK_TTS_MODE = TtsMode.ONLINE;

    /** 下面参数 纯离线SDK版本才有 */

    String VOICE_FEMALE = "F";

    String VOICE_MALE = "M";

    String VOICE_DUYY = "Y";

    String VOICE_DUXY = "X";

    String TEXT_MODEL = null;

    String VOICE_MALE_MODEL = null;

    String VOICE_FEMALE_MODEL = null;

    String VOICE_DUXY_MODEL = null;

    String VOICE_DUYY_MODEL = null;

    String PARAM_SN_NAME = null;
}
