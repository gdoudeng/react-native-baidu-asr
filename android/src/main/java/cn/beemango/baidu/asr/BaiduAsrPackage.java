// BaiduAsrPackage.java

package cn.beemango.baidu.asr;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaiduAsrPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new BaiduAsrModule(reactContext));
        modules.add(new BaiduAsrConstantModule(reactContext));
        modules.add(new BaiduWakeUpModule(reactContext));
        modules.add(new BaiduSynthesizerModule(reactContext));
        modules.add(new BaiduSynthesizerConstantModule(reactContext));

        return modules;
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
