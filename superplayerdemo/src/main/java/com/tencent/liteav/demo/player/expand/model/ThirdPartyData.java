package com.tencent.liteav.demo.player.expand.model;

import com.tencent.liteav.demo.vodcommon.entity.ConfigBean;
import com.tencent.liteav.demo.vodcommon.entity.VideoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 正式合入和发布前需要删除！！
 */
public class ThirdPartyData {

    private static final int APP_ID = 1500020494;
    private static final String[] FILE_IDS = new String[]{
            "243791581752800029", "243791581752723391",
            "243791581752408631", "243791581753066992",
            "243791581753172133", "243791581753024640",
            "243791581752981251", "243791581752903213",
            "243791581752978166", "243791581752722487",
            "243791581753152694", "243791581752888249",
            "243791581752977360", "243791581752702503",
            "243791581753447399", "243791581752725753",
            "243791581753069834", "243791581752704761",
            "243791581753191838", "243791581753171925",
    };

    public static List<VideoModel> videoList() {
        List<VideoModel> videoModels = new ArrayList<>();
        for (int i = 0; i < FILE_IDS.length; i++) {
            VideoModel videoModel = new VideoModel();
            videoModel.appid = APP_ID;
            videoModel.fileid = FILE_IDS[i];
            videoModels.add(videoModel);
        }
        return videoModels;
    }
}
