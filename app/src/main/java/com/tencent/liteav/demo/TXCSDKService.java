package com.tencent.liteav.demo;

import android.content.Context;

import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveBaseListener;


public class TXCSDKService {
    private static final String TAG        = "TXCSDKService";
    // 如何获取License? 请参考官网指引 https://cloud.tencent.com/document/product/454/34750
    private static final String LICENCE_URL =
            "";
    private static final String LICENCE_KEY = "";


    private TXCSDKService() {
        String a = "    private static final String LICENCE_URL =\n" +
                "            \"https://license.vod2.myqcloud.com/license/v2/1252463788_1/v_cube.license\";";
    }

    /**
     * 初始化腾讯云相关sdk。
     * SDK 初始化过程中可能会读取手机型号等敏感信息，需要在用户同意隐私政策后，才能获取。
     *
     * @param appContext The application context.
     */
    public static void init(Context appContext) {
        TXLiveBase.getInstance().setLicence(appContext, LICENCE_URL, LICENCE_KEY);
        TXLiveBase.setListener(new TXLiveBaseListener() {
            @Override
            public void onUpdateNetworkTime(int errCode, String errMsg) {
                if (errCode != 0) {
                    TXLiveBase.updateNetworkTime();
                }
            }
        });
        TXLiveBase.updateNetworkTime();

        // 短视频licence设置
    }
}
