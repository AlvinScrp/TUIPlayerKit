package com.tencent.liteav.demo.player.demo.shortvideo.layer.vod;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tencent.qcloud.tuiplayer.core.api.ui.view.TUIVodLayer;

public class TUIErrorLayer extends TUIVodLayer {

    @Override
    public View createView(ViewGroup parent) {
        return new View(parent.getContext());
    }

    @Override
    public void onError(int code, String message, Bundle extraInfo) {
        if (null != getView()) {
            Context context = getView().getContext();
            Toast.makeText(context, "playError, errorCode:" + code + ",message:" + message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String tag() {
        return "TUILiveErrorLayer";
    }
}
