package com.tencent.liteav.demo.player.demo.shortvideo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.tencent.liteav.demo.player.R;
import com.tencent.liteav.demo.player.demo.shortvideo.common.SVDemoConstants;
import com.tencent.liteav.demo.player.demo.shortvideo.layer.TUILayerBridge;
import com.tencent.liteav.demo.player.demo.shortvideo.common.DemoSVGlobalConfig;
import com.tencent.liteav.demo.player.demo.shortvideo.layer.custom.img.PicDisplayLayer;
import com.tencent.liteav.demo.player.demo.shortvideo.layer.live.TUILiveEntranceLayer;
import com.tencent.liteav.demo.player.demo.shortvideo.layer.live.TUILiveErrorLayer;
import com.tencent.liteav.demo.player.demo.shortvideo.layer.live.TUILiveLoadingLayer;
import com.tencent.liteav.demo.player.demo.shortvideo.layer.vod.TUICoverLayer;
import com.tencent.liteav.demo.player.demo.shortvideo.layer.vod.TUIErrorLayer;
import com.tencent.liteav.demo.player.demo.shortvideo.layer.vod.TUILoadingLayer;
import com.tencent.liteav.demo.player.demo.shortvideo.layer.vod.TUIVideoInfoLayer;
import com.tencent.liteav.demo.player.demo.shortvideo.ui.TUIToolsController;
import com.tencent.liteav.demo.player.expand.model.ShortVideoModel;
import com.tencent.qcloud.tuiplayer.core.api.TUIPlayerLiveStrategy;
import com.tencent.qcloud.tuiplayer.core.api.TUIPlayerVodStrategy;
import com.tencent.qcloud.tuiplayer.core.api.common.TUIConstants;
import com.tencent.qcloud.tuiplayer.core.api.model.TUIPlaySource;
import com.tencent.qcloud.tuiplayer.core.api.ui.view.custom.TUICustomLayerManager;
import com.tencent.qcloud.tuiplayer.core.api.ui.view.live.TUILiveLayerManager;
import com.tencent.qcloud.tuiplayer.core.api.ui.view.vod.TUIVodLayerManager;
import com.tencent.qcloud.tuiplayer.shortvideo.ui.view.TUIShortVideoListener;
import com.tencent.qcloud.tuiplayer.shortvideo.ui.view.TUIShortVideoView;

import java.util.List;

public class ShortVideoFragment extends Fragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, TUILayerBridge {
    private static final String TAG = "ShortVideoFragment";

    private ImageButton mBack;
    private TUIShortVideoView mShortVideoView;
    private SwipeRefreshLayout mShortViewRefresh;
    private TUIToolsController mToolsController;

    private boolean mNeedPause = true;

    private final TUIShortVideoListener mListener = new TUIShortVideoListener() {

        @Override
        public void onCreateVodLayer(TUIVodLayerManager layerManger, int viewType) {
            layerManger.addLayer(new TUIVideoInfoLayer(mShortVideoView, ShortVideoFragment.this));
            layerManger.addLayer(new TUICoverLayer());
            layerManger.addLayer(new TUILoadingLayer());
            layerManger.addLayer(new TUIErrorLayer());
        }

        @Override
        public void onCreateLiveLayer(TUILiveLayerManager layerManager, int viewType) {
            layerManager.addLayer(new TUILiveEntranceLayer(mShortVideoView, ShortVideoFragment.this));
            layerManager.addLayer(new TUILiveLoadingLayer());
            layerManager.addLayer(new TUILiveErrorLayer());
        }

        @Override
        public void onCreateCustomLayer(TUICustomLayerManager layerManager, int viewType) {
            if (viewType == SVDemoConstants.CustomSourceType.SINGLE_IMG_TYPE) {
                layerManager.addLayer(new PicDisplayLayer());
            }
        }

        @Override
        public void onPageChanged(int index, TUIPlaySource videoSource) {
            if (index >= mShortVideoView.getCurrentDataCount() - 1) {
                mShortViewRefresh.setRefreshing(true);
                ShortVideoModel.getInstance().loadMore(false);
            }
        }

        @Override
        public void onNetStatus(TUIPlaySource model, Bundle bundle) {
        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.player_fragment_tui_short_video_play, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(savedInstanceState);
    }

    private void initViews(@Nullable Bundle savedInstanceState) {
        mShortVideoView = requireActivity().findViewById(R.id.super_short_video_view_play_fragment);
        mShortViewRefresh = requireActivity().findViewById(R.id.spl_tui_refresh);
        FrameLayout settingContainer = requireActivity().findViewById(R.id.fl_setting_container);
        mBack = requireActivity().findViewById(R.id.ib_back_play);
        mBack.setOnClickListener(this);
        mShortVideoView.setListener(mListener);
        // set strategy of shortVideo
        TUIPlayerVodStrategy vodStrategy = new TUIPlayerVodStrategy.Builder()
                .setIsRetainPreVod(false)
                .setSuperResolutionMode(TUIConstants.TUISuperResolution.SUPER_RESOLUTION_ASR)
                .setRenderMode(TUIConstants.TUIRenderMode.ADJUST_RESOLUTION)
                .build();
        mShortVideoView.setVodStrategy(vodStrategy);

        TUIPlayerLiveStrategy liveStrategy = new TUIPlayerLiveStrategy.Builder()
                .setIsRetainPreLive(false)
                .setRenderMode(TUIConstants.TUIRenderMode.ADJUST_RESOLUTION)
                .build();
        mShortVideoView.setLiveStrategy(liveStrategy);
        mToolsController = new TUIToolsController(settingContainer, mShortVideoView, vodStrategy, liveStrategy,
                this);
        mShortViewRefresh.setOnRefreshListener(this);
        mShortViewRefresh.setRefreshing(true);
        DemoSVGlobalConfig.instance().initParams(0, false, false);

        ShortVideoModel.getInstance().loadMore(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TUIShortVideoLiveActivity.LIVE_CALL_BACK || resultCode == TUILandScapeActivity.LAND_SCAPE_CALL_BACK) {
            TUIPlaySource playSource = mShortVideoView.getDataManager().getCurrentModel();
            if (null != playSource) {
                playSource.setExtInfoAndNotify(SVDemoConstants.obtainEmptyEvent(SVDemoConstants.LayerEventCode.PLUG_RENDER_VIEW));
            }
        }
    }

    @Override
    public void onClick(View v) {
        final int vId = v.getId();
        if (vId == R.id.ib_back_play) {
            requireActivity().finish();
        } else {
            Log.i(TAG, "onClick in other case");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNeedPause) {
            mShortVideoView.pause();
        } else {
            mNeedPause = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mShortVideoView.resume();
    }

    public void onLoaded(List<TUIPlaySource> shortVideoBeanList, boolean isRefresh) {
        if (mShortVideoView != null) {
            mShortViewRefresh.setRefreshing(false);
            if (isRefresh) {
                int ret = mShortVideoView.setModels(shortVideoBeanList);
            } else {
                int ret = mShortVideoView.appendModels(shortVideoBeanList);
            }
        }
    }

    @Override
    public void onRefresh() {
        ShortVideoModel.getInstance().loadMore(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // clear glide memory
        mShortVideoView.release();
        Glide.get(requireActivity()).clearMemory();
    }


    @Override
    public void setNeedPauseOnce(boolean isNeed) {
        mNeedPause = isNeed;
    }

    @Override
    public void postExtInfoToCurLayer(Object obj) {
        TUIPlaySource playSource = mShortVideoView.getDataManager().getCurrentModel();
        if (null != playSource) {
            playSource.setExtInfoAndNotify(obj);
        }
    }
}
