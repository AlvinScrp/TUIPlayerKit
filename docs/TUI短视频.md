## 组件简介

TUIPlayerShortVideo 组件是腾讯云推出的一款性能优异，支持视频极速首帧和流畅滑动，提供优质播放体验的短视频组件。
- 首帧秒开：首帧时间是短视频类应用核心指标之一，直接影响用户的观看体验。短视频组件通过预播放、预下载、播放器复用和精准流量控制等技术，实现极速首帧、滑动丝滑的优质播放体验，从而提升用户播放量和停留时长。

- 优秀的性能：通过播放器复用和加载策略的优化，在保证极佳流畅度的同时，始终让内存和 CPU 消耗保持在较低的水平。

- 快速集成：组件对复杂的播放操作进行了封装，提供默认的播放 UI，同时支持 FileId 和 Url 播放，可低成本快速集成到您的项目中。


## 效果对比

以下视频演示了，在同等环境下，未经过优化和经过优化之后的短视频使用的对比差异。
- 优化前，可以明显感觉到视频起播的卡顿感。

- 优化后，可以达到无感起播的体验，优化后起播平均时长达到10毫秒 - 30毫秒。

<table>
<tr>
<td rowspan="1" colSpan="1" >未优化短视频</td>

<td rowspan="1" colSpan="1" >优化后短视频</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" ></td>

<td rowspan="1" colSpan="1" ></td>
</tr>
</table>


## TUIPlayerKit 下载

TUIPlayerKit SDK 和 Demo 可[单击这里](https://mediacloud-76607.gzc.vod.tencent-cloud.com/TUIPlayerKit/download/latest/TUIPlayerKit_Android_latest.zip) 下载。

## 集成 TUIPlayerShortVideo 组件

### 环境准备

Android 系统最低版本要求：Android SDK  >= 19

添加短视频需要的依赖：
``` xml
// 如果您使用的是专业版本的SDK，则用：api 'com.tencent.liteav:LiteAVSDK_Professional:latest.release'
api 'com.tencent.liteav:LiteAVSDK_Player:latest.release'
implementation (name:'tuiplayercore-release_x.x.x', ext:'aar')
implementation (name:'tuiplayershortvideo-release_x.x.x', ext:'aar')
implementation 'androidx.appcompat:appcompat:1.0.0'
implementation 'androidx.viewpager2:viewpager2:1.0.0'
```

> **注意：**
> 

> `x.x.x` 为版本号，注意2个 aar 的版本号必须一致。
> 


SDK 需要的权限：
``` typescript
        
<uses-permission android:name="android.permission.INTERNET" />
```

设置混淆规则：

在 proguard-rules.pro 文件中，将相关类加入不混淆名单：
``` xml
-keep class com.tencent.** { *; }
```

### 申请播放器高级版 License

使用 TUIPlayer Kit 组件需要使用移动端播放器高级版 License，您可参见 [移动端播放器 License](https://write.woa.com/document/96912869201248256) 指引来获取。若您已获取对应 License，可前往 [腾讯云视立方控制台 > License 管理 > 移动端 License](https://console.cloud.tencent.com/vcube/mobile) 获取对应 LicenseURL 和 LicenseKey。如果没有申请移动端播放器高级版 License，将会出现视频播放失败、黑屏等现象。

### 设置 License

短视频组件需要设置 License 才能使用。
``` java
TUIPlayerConfig config = new TUIPlayerConfig.Builder()
        .enableLog(true)
        .licenseKey("Your license key")
        .licenseUrl("Your license url")
        .build();
TUIPlayerCore.init(context, config);
```

### 添加 UI 组件

在布局文件中，添加短视频 UI 组件。
``` xml
<com.tencent.qcloud.tuiplayer.shortvideo.ui.view.TUIShortVideoView
    android:id="@+id/my_video_view"
    android:layout_height="match_parent"
    android:layout_width="match_parent"/>
```

### 配置组件

对短视频组件进行配置，填充视频数据。
``` java
mSuperShortVideoView.setActivityLifecycle(getLifecycle());
// set strategy of shortVideo
mSuperShortVideoView.setStrategy(new TUIVideoStrategy.Builder().build());
mSuperShortVideoView.setListener(new TUIShortVideoListener() {
  @Override
  public void onPageChanged(int index, TUIVideoSource videoSource) {
      if (index >= mSuperShortVideoView.getCurrentDataCount() - 1) {
        // append next page data
        mSuperShortVideoView.appendModels(data);
      }
  }

  @Override
  public void onCreateItemLayer(TUILayerManger layerManger, int viewType) {
      // add your custom layers
      layerManger.addLayer(new TUICoverLayer());
  }
  
  @Override
  public void onNetStatus(TUIVideoSource model, Bundle bundle) {
  }
 });
 mSuperShortVideoView.setModels(shortVideoData);
```

## 自定义图层

### 简介

Android TUI 短视频，采用图层管理的方式，提供给每个短视频页面自定义 UI 能力。通过图层管理器，能够更好的处理视频 UI 与视频的事件交互。

图层的显示和隐藏，会直接对 View 进行添加和移除，不会造成界面过度渲染。

图层会根据添加的顺序，来决定界面的展示顺序，先添加的在最底层，后添加的在最上层。

### 创建自定义

#### 1. 创建自定义图层

创建自定义图层，需要继承 TUIBaseLayer，然后实现自己需要的图层。

以视频详情图层为例，首先需要实现 createView 和 tag 方法。createView 是图层 view 的创建方法，tag 是用来区分图层的字符串标签。
``` java
@Override
public View createView(ViewGroup parent) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.player_video_info_layer, parent, false);
    mSeekBar = view.findViewById(R.id.vsb_tui_video_progress);
    mTvProgress = view.findViewById(R.id.tv_tui_progress_time);
    mIvPause = view.findViewById(R.id.iv_tui_pause);
    mSeekBar.setListener(this);
    return view;
}

@Override
public String tag() {
    return "TUIVideoInfoLayer";
}
```

createView 中创建了一个 View 并返回。这里可以使用 LayoutInflater 从 XML 中加载布局，也可以使用代码直接创建布局。

#### 2. 展示布局

View 创建完成之后，需要在合适的时机去展示。TUIBaseLayer 提供了丰富的事件回调。详细信息可以参见 [图层回调](https://write.woa.com/#eacb47cb-9355-4b47-8ae0-08d48ba0d6fa)。

视频详情图层在获得数据的时候就可以展示布局了。所以在 onBindData 把图层展示出来。
``` java
@Override
public void onBindData(TUIVideoSource videoSource) {
    show();
}
```

#### 3. 操作单个 View

还可以在其他事件中对单个 View 进行操作，例如暂停按钮的显示和隐藏，以及播放进度的回调，参见如下代码。
``` java
@Override
public void onPlayBegin() {
    super.onPlayBegin();
    if (null != mIvPause) {
        mIvPause.setVisibility(View.GONE);
    }
}

@Override
public void onPlayPause() {
    super.onPlayPause();
    if (null != mIvPause) {
        mIvPause.setVisibility(View.VISIBLE);
    }
}

@Override
public void onPlayProgress(long current, long duration, long playable) {
    videoDuration = duration;
    if (null != mSeekBar) {
        // ensure a refresh at every percentage point
        int progressInt = (int) (((1.0F * current) / duration) * 100);
        if(lastProgressInt != progressInt) {
            setProgress(progressInt / 100F);
            lastProgressInt = progressInt;
        }
    }
}
```

#### 4. 控制播放器

除了接收来自播放器的事件之外，还可以对播放器进行控制。例如调用播放器进行进度的跳转。
``` java
@Override
public void onDragDone(VideoSeekBar seekBar) {
    TUIPlayerController controller = getPlayerController();
    if (null != controller && videoDuration > 0) {
        controller.seekTo((int) ((videoDuration * seekBar.getBarProgress()) / 1000));
    }
    if (null != mTvProgress) {
        mTvProgress.setVisibility(View.GONE);
    }
}
```

目前在 TUIBaseLayer 中，可以通过 getVideoView 获得当前 page 的 VideoView 对象，通过 getPlayerController 获得当前视频的播放控制器（只有当前 page 是当前短视频列表正在播放的视频的时候才会有），getPlayer 获得当前播放器对象。由于短视频的 item 组件采用了复用机制。这三个对象在获取的时候，都可能会获得空对象，需要进行判空。

推荐使用 getPlayerController 对视频对象进行操作。

#### 5. 图层脱离图层管理器时进行释放

在图层脱离图层管理器的时候，需要进行一些释放操作。防止一些外部的对象对图层造成持有，产生了内存泄漏。例如在 unBindLayerManager 中将 seekBar 的监听设置为空。
``` java
@Override
public void unBindLayerManager() {
    super.unBindLayerManager();
    if (null != mSeekBar) {
        mSeekBar.setListener(null);
    }
}
```

#### 6. 监听 controller

如果需要监听当前 page 是否是当前播放视频，可以对 controller 进行监听。参见以下封面图的显示和隐藏所示：
``` java
@Override
public void onControllerUnBind(TUIPlayerController controller) {
    super.onControllerUnBind(controller);
    // show cover when unbind
    show();
}
```

以上代码，是在解除 controller 绑定之后，不需要播放了，显示封面图。

#### 7. 通过 onRecFileVideoInfo 回调获取视频信息

FileId 播放没有视频信息，可以通过 onRecFileVideoInfo 回调去获取，参见如下代码：
``` java
@Override
public void onRecFileVideoInfo(TUIFileVideoInfo params) {
    if(isShowing()) {
        TUIBaseVideoView videoView = getVideoView();
        if (null != videoView && null != params) {
            String coverUrl = params.getCoverUrl();
            if (!TextUtils.isEmpty(coverUrl)) {
                ImageView imageView = getView();
                Glide.with(videoView).load(coverUrl)
                        .centerCrop()
                        .into(imageView);
                coverUrlFromServer = coverUrl;
            }
        }
    }
}
```

该方法会在只使用 fileID 播放的时候回调。会返回视频 URL 链接、封面图、时长、雪碧图等信息。

建议尽可能的通过 URL 传入短视频组件进行播放，并提前赋值好封面图 URL ，这样能够增加短视频加载性能。

#### 8. 通过 onRcvFirstIframe 方法判断视频首帧是否到来

使用方法参见如下代码：
``` java
@Override
public void onRcvFirstIframe() {
    hidden();
}
```

例如封面图等场景，需要在收到首帧事件后来隐藏封面图。

### 管理图层

当集成短视频组件 TUIShortVideoView 后，使用 TUIShortVideoView 设置监听会在合适的时机回调 item 的创建方法 onCreateItemLayer，来添加或者管理自定义图层。
``` java
mSuperShortVideoView.setListener(new TUIShortVideoListener() {

   // ......
   
    @Override
    public void onCreateItemLayer(TUILayerManger layerManger, int viewType) {
        layerManger.addLayer(new TUICoverLayer());
        layerManger.addLayer(new TUIVideoInfoLayer());
        layerManger.addLayer(new TUILoadingLayer());
        layerManger.addLayer(new TUIErrorLayer());
    }
});
```

onCreateItemLayer 有两个参数，layerManger 为图层管理器，可以添加、移除、查询图层。添加方式如上图所示。viewType 为当前 page 的视频类型，1代表点播，2代表直播。

如果不需要图层，可以将图层移除。
``` java
layerManger.removeLayer(layer);
```

如果需要获得图层的层级，做图层的交互操作，可以通过以下方法获取图层的层级：
``` java
layerManger.indexOfLayer(layer);
```

## 短视频接口使用说明

### 1. 设置生命周期监听

用于 TUIShortVideoView 的生命周期控制，内部自行根据 lifeCycle 状态，进行列表视频的暂停、播放和销毁。
``` java
mSuperShortVideoView.setActivityLifecycle(getLifecycle());
```

### 2. 设置短视频监听

用于监听 TUIShortVideoView 的事件，其中包括加载分页数据时机，创建 page 的时候回调（可以在该回调添加图层）。
``` java
mSuperShortVideoView.setListener(new TUIShortVideoListener() {
        @Override
        public void onPageChanged(int index, TUIVideoSource videoSource) {
          if (index >= mSuperShortVideoView.getCurrentDataCount() - 1) {
               loadMore(false);
          }
        }

        @Override
        public void onCreateItemLayer(TUILayerManger layerManger, int viewType) {
            layerManger.addLayer(new TUICoverLayer());
            layerManger.addLayer(new TUIVideoInfoLayer());
            layerManger.addLayer(new TUILoadingLayer());
            layerManger.addLayer(new TUIErrorLayer());
        }
        
        @Override
        public void onNetStatus(TUIVideoSource model, Bundle bundle) {
        }
    });
```

### 3. 设置视频播放策略

设置视频播放过程中的各种策略。

#### 策略 TUIVideoStrategy 参数

需要使用 Builder 构建。
<table>
<tr>
<td rowspan="1" colSpan="1" >**函数**</td>

<td rowspan="1" colSpan="1" >**描述**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreloadCount</td>

<td rowspan="1" colSpan="1" >设置预加载最大并发数量，默认3。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreLoadBufferSize</td>

<td rowspan="1" colSpan="1" >设置预加载缓存大小，默认1MB，单位 MB。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setIsPreVideoResume</td>

<td rowspan="1" colSpan="1" >返回上一个视频，是否继续之前的播放状态，默认 false。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreferredResolution</td>

<td rowspan="1" colSpan="1" >设置视频播放的偏好分辨率，默认720 x 1280。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setProgressInterval</td>

<td rowspan="1" colSpan="1" >播放进度回调间隔，默认500毫秒，单位毫秒。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setRenderMode</td>

<td rowspan="1" colSpan="1" >渲染平铺模式，默认0。liteavPlayer 中，0代表全屏屏幕，1代表按照视频实际比例渲染，可能会有黑边。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setExtInfo</td>

<td rowspan="1" colSpan="1" >设置额外信息。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setMediaType</td>

<td rowspan="1" colSpan="1" >当提前知道播放的媒资类型时，可以通过该接口设置媒资类型，减少播放器SDK内部播放类型探测，提升启播速度。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >enableAutoBitrate</td>

<td rowspan="1" colSpan="1" >设置是否启用码率自适应。</td>
</tr>
</table>


### 4. 填充数据

往 TUIShortVideoView 中填充数据：
``` java
mSuperShortVideoView.setModels(shortVideoBeanList);
```

追加数据：
``` java
mSuperShortVideoView.appendModels(shortVideoBeanList);
```

#### TUIVideoSource 类
<table>
<tr>
<td rowspan="1" colSpan="1" >**参数**</td>

<td rowspan="1" colSpan="1" >**类型**</td>

<td rowspan="1" colSpan="1" >**描述**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >videoURL</td>

<td rowspan="1" colSpan="1" >String</td>

<td rowspan="1" colSpan="1" >视频链接，建议填充该字段，会加快预加载速度。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >coverPictureUrl</td>

<td rowspan="1" colSpan="1" >String</td>

<td rowspan="1" colSpan="1" >视频封面，会回调到 layer，由客户自行处理。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >duration</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >视频时长，单位秒，会回调到 layer。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >appid</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >视频 appId</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >fileid</td>

<td rowspan="1" colSpan="1" >String</td>

<td rowspan="1" colSpan="1" >视频 fileId</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >pSign</td>

<td rowspan="1" colSpan="1" >String</td>

<td rowspan="1" colSpan="1" >视频加密 pSign</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >extInfo</td>

<td rowspan="1" colSpan="1" >Map</td>

<td rowspan="1" colSpan="1" >用于业务自行扩展额外参数。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setVideoConfig</td>

<td rowspan="1" colSpan="1" >TUIPlayerVideoConfig</td>

<td rowspan="1" colSpan="1" >视频独立配置</td>
</tr>
</table>


#### TUIPlayerVideoConfig 类
<table>
<tr>
<td rowspan="1" colSpan="1" >**参数**</td>

<td rowspan="1" colSpan="1" >**类型**</td>

<td rowspan="1" colSpan="1" >**描述**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreloadBufferSizeInMB</td>

<td rowspan="1" colSpan="1" >float</td>

<td rowspan="1" colSpan="1" >设置视频单独的预加载缓存大小，可选。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreferredResolution</td>

<td rowspan="1" colSpan="1" >long</td>

<td rowspan="1" colSpan="1" >设置视频单独的起播和预加载分辨率，可选。</td>
</tr>
</table>


### 5. 获取当前正在播放的视频资源

获取当前正在播放的视频资源，使用参见如下代码：
``` java
mSuperShortVideoView.getCurrentModel()
```

### 6. 暂停

暂停当前正在播放视频。
``` java
mSuperShortVideoView.pause()
```

### 7. 从指定位置播放

从指定位置开始播放，使用参见如下代码：
``` java
mSuperShortVideoView.startPlayIndex()
```

### 8. 销毁控件

销毁控件和资源。
``` java
mSuperShortVideoView.release()
```

### 9. 续播当前视频

续播当前视频，使用参见如下代码：
``` java
mSuperShortVideoView.resume()
```

### 10. 添加图层

TUI 短视频可以通过添加图层来实现短视频播放界面上的自定义 UI。当 onCreateItemLayer 回调时，就可以通过方法携带的 LayerManager 进行图层的添加和管理。可以通过继承 TUIBaseLayer 来自定义自己需要的图层。图层会浮在视频 VideoView 的上方。

图层的显示和隐藏，都是通过对 View 的添加和移除来操作的，没有同时显示大量图层的情况，不会产生过度渲染的问题。

#### onCreateItemLayer 参数
<table>
<tr>
<td rowspan="1" colSpan="1" >**参数**</td>

<td rowspan="1" colSpan="1" >**类型**</td>

<td rowspan="1" colSpan="1" >**描述**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >layerManger</td>

<td rowspan="1" colSpan="1" >TUILayerManger</td>

<td rowspan="1" colSpan="1" >图层管理</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >viewType</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >当前视频播放类型<br>- 1：点播<br>- 2：直播</td>
</tr>
</table>

``` java
layerManger.addLayer(new TUICoverLayer());
layerManger.addLayer(new TUIVideoInfoLayer());
layerManger.addLayer(new TUILoadingLayer());
layerManger.addLayer(new TUIErrorLayer());
```

### 11. 实时切换分辨率

TUI 短视频可以实时切换当前视频分辨率以及全局视频分辨率，接口如下：
``` java
mSuperShortVideoView.switchResolution(720 * 1080, TUIConstants.TUIResolutionType.CURRENT);
```

switchType 除了可以传递 `TUIResolutionType` 以外，也可以直接指定需要切换的视频的 index 来切换视频分辨率。

switchType 含义如下：player
<table>
<tr>
<td rowspan="1" colSpan="1" >参数</td>

<td rowspan="1" colSpan="1" >描述</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >GLOBAL</td>

<td rowspan="1" colSpan="1" >设置全局分辨率</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >CURRENT</td>

<td rowspan="1" colSpan="1" >设置当前视频分辨率</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >其他大于等于0的值</td>

<td rowspan="1" colSpan="1" >设置指定分辨率</td>
</tr>
</table>


**目前分辨率的优先级，当前视频分辨率的设置优先级大于全局分辨率。**

### 12. 暂停和继续预加载

TUI 短视频可以实时暂停和继续预加载任务
``` java
// pause all preload
mSuperShortVideoView.pausePreload();
// start preload from current video index
mSuperShortVideoView.resumePreload();
```

调用继续预加载的时候，会从当前视频开始往后继续预加载



## TUIPlayerCore 接口使用说明

### 1. 配置 License

使用 TUI 组件，需要配置对应 premium 的 License，示例如下：
``` java
TUIPlayerConfig config = new TUIPlayerConfig.Builder()
        .enableLog(true)
        .licenseKey(LICENCE_KEY)
        .licenseUrl(LICENCE_URL)
        .build();
TUIPlayerCore.init(getApplicationContext(), config);
```

### 2. 创建 PlayerManager

创建 PlayerManager，视频数据处理者，其中包括播放器销毁创建、预播放、预加载、播放控制、绑定等操作。
``` java
mPlayerManager = new TUIPlayerManager(getContext(), listener);
```

#### playerManager 构造方法参数
<table>
<tr>
<td rowspan="1" colSpan="1" >**参数**</td>

<td rowspan="1" colSpan="1" >**类型**</td>

<td rowspan="1" colSpan="1" >**描述**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >context</td>

<td rowspan="1" colSpan="1" >Context</td>

<td rowspan="1" colSpan="1" >上下文</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >bridge</td>

<td rowspan="1" colSpan="1" >PlayerBridge</td>

<td rowspan="1" colSpan="1" >playerManager 的沟通桥梁</td>
</tr>
</table>


#### PlayerBridge 类
<table>
<tr>
<td rowspan="1" colSpan="1" >**函数**</td>

<td rowspan="1" colSpan="1" >**返回类型**</td>

<td rowspan="1" colSpan="1" >**描述**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getCurrentScrollState</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >当前视频列表滑动状态。<br>- 1：空闲状态<br>- 2：正在拖动<br>- 3：拖动后的惯性滑动状态</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getCurrentPlayingIndex</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >当前 page 的 index</td>
</tr>
</table>


### 3. 更新策略

更新 PlayerManager 的视频预加载、播放、预播放等的策略。
``` java
mPlayerManager.updateStrategy(strategy);
```

strategy 参数与短视频的一致。

### 4. 设置数据

设置 playerManager 中的数据，该接口会清除之前所有的数据。
``` java
mPlayerManager.setModels(dataSource);
```

### 5. 追加数据

向 PlayerManager 中追加数据，一般用于分页加载。
``` java
mPlayerManager.appendModels(models);
```

### 6. 预加载绑定 VideoView

在切换当前正在播放的 page 的时候，需要绑定当前的 VideoView，来加载视频，示例如下：
``` java
TUIBaseVideoView itemView = findVideoViewByPos(position);
if (null != itemView) {
    boolean isUpDirection = mLastPosition > position;
    TUIPlayerLog.v(TAG, "start bind and play view " + position + ", isUpDirection:" + isUpDirection);
    mPlayerManager.bindVideoView(itemView, isUpDirection);
    mPlayerManager.startCurrent();
}
```

### 7. 预加载预渲染 VideoView

当播放当前视频的时候，可以提前给下一个视频的 View 预渲染。来提升下一个视频的起播速度，示例如下：
``` java
private void handleNextViewRender(int currentPosition) {
    final int position = currentPosition + 1;
    TUIBaseVideoView nextView = findVideoViewByPos(position);
    if (nextView != null) {
        TUIPlayerLog.v(TAG, "start render next view " + position);
        mPlayerManager.preRenderOnView(nextView);
    }
}
```

### 8. 获得当前正在处理的视频

获得当前正在播放的视频的 model 对象。
``` java
mPlayerManager.getCurrentModel();
```

### 9. 暂停当前视频

暂停当前视频列表正在播放的视频。
``` java
mPlayerManager.pauseCurrent();
```

### 10. 播放状态

判断当前视频是否正在播放。
``` java
mPlayerManager.isCurrentPlaying();
```

### 11. 继续播放

继续播放当前正在播放的视频。
``` java
mPlayerManager.resumeCurrent();
```

### 12. 切换播放状态

根据当前播放状态，自行切换当前视频的播放和暂停。
``` java
mPlayController.togglePlay();
```

### 13. 销毁 Manager

释放 Manager 资源。如果短视频中设置了生命周期，该方法会自行在短视频的 VideoView 中被调用。
``` java
mPlayerManager.releasePlayers();
```

### 14. 添加图层

向图层管理器添加图层。
``` java
layerManger.addLayer(new TUICoverLayer());
```

### 15. 移除指定图层

将图层管理器中的图层移除。
``` java
layerManger.removeLayer(layer);
```

### 16. 移除所有图层

移除图层管理器中的所有图层。
``` java
layerManger.removeAllLayer();
```

### 17. 获得图层的当前层级

传入图层，获得当前图层的层级，依此可以判断图层显示过程中的先后覆盖顺序，以及触摸事件的先后传递顺序。
``` java
layerManger.indexOfLayer(layer);
```

### 18. 图层绑定 VideoView

将图层管理器与 VideoView 进行绑定。绑定后，该图层管理器就会收到视频播放过程的事件。
``` java
layerManger.bindVideoView(itemView);
```

### 19. 图层回调

继承 TUIBaseLayer 后，可以根据功能需要来接收视频播放的回调，目前回调函数如下：

#### TUIBaseLayer 类
<table>
<tr>
<td rowspan="1" colSpan="1" >**函数**</td>

<td rowspan="1" colSpan="1" >**返回类型**</td>

<td rowspan="1" colSpan="1" >**参数**</td>

<td rowspan="1" colSpan="1" >**描述**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >isShowing</td>

<td rowspan="1" colSpan="1" >boolean</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >当前图层是否正在显示。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >createView</td>

<td rowspan="1" colSpan="1" >View</td>

<td rowspan="1" colSpan="1" >parent：图层容器</td>

<td rowspan="1" colSpan="1" >抽象方法，需要自己实现，用于创建图层的 View。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >tag</td>

<td rowspan="1" colSpan="1" >String</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >图层的 tag，用于区分不同的图层。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >unBindLayerManager</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >图层与管理器发生解绑，一般发生在图层被移除的时候。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >show</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >显示当前图层。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >hidden</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >隐藏当前图层。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getView</td>

<td rowspan="1" colSpan="1" >T extends View</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >获得当前图层的 View。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getVideoView</td>

<td rowspan="1" colSpan="1" >TUIBaseVideoView</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >获得当前 VideoView，如果 layerManager 与 VideoView还未绑定会返回空。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getPlayerController</td>

<td rowspan="1" colSpan="1" >TUIPlayerController</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >获得当前播放 Controller，如果还未与 Controller 发生绑定，会返回空。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getPlayer</td>

<td rowspan="1" colSpan="1" >ITUIPlayer</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >获得当前播放器，如果还未与 Controller 发生绑定，会返回空。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getRenderMode</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >获得当前播放器画面渲染填充模式。</td>
</tr>
</table>


#### PlayerObserver 类
<table>
<tr>
<td rowspan="1" colSpan="1" >**函数**</td>

<td rowspan="1" colSpan="1" >**返回类型**</td>

<td rowspan="1" colSpan="1" >**参数**</td>

<td rowspan="1" colSpan="1" >**描述**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayPrepare</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >视频准备完毕</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayBegin</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >视频开始播放</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayPause</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >视频暂停</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayStop</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >视频停止</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayLoading</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >视频开始加载</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayLoadingEnd</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >视频加载结束</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayProgress</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >- current：当前视频播放进度，单位毫秒，long 类型。<br>- duration：当前视频总时长，单位毫秒，long 类型。<br>- playable：当前视频可播放时长，单位毫秒，long 类型</td>

<td rowspan="1" colSpan="1" >视频播放进度</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onSeek</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >position：跳转到的视频进度。单位秒，int 类型。</td>

<td rowspan="1" colSpan="1" >视频进度发生跳转</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onError</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >code：视频错误码 <br>message：错误描述</td>

<td rowspan="1" colSpan="1" >视频播放发生错误</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onRcvFirstIframe</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >收到首帧事件</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onRcvTrackInformation</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >infoList：视频轨道</td>

<td rowspan="1" colSpan="1" >收到视频轨道信息</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onRcvSubTitleTrackInformation</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >infoList：视频字幕信息</td>

<td rowspan="1" colSpan="1" >收到视频字幕信息</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onRecFileVideoInfo</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >params：视频文件信息</td>

<td rowspan="1" colSpan="1" >收到视频文件信息，一般只使用 fileId 播放才会触发该回调。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onResolutionChanged</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >width：视频宽度<br>height：视频高度</td>

<td rowspan="1" colSpan="1" >当前视频分辨率发生变化</td>
</tr>
</table>


#### TUIVideoViewListener 类
<table>
<tr>
<td rowspan="1" colSpan="1" >**函数**</td>

<td rowspan="1" colSpan="1" >**返回类型**</td>

<td rowspan="1" colSpan="1" >**参数**</td>

<td rowspan="1" colSpan="1" >**描述**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onControllerBind</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >controller：当前视频播放控制器</td>

<td rowspan="1" colSpan="1" >当前 VideoView 与播放控制器发生绑定，即当前视频变为列表中正在播放的视频。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onControllerUnBind</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >controller：当前视频播放控制器</td>

<td rowspan="1" colSpan="1" >VideoView 与播放控制器发生解绑，一般代表该 page 滑出界面。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onBindData</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >videoSource：视频数据</td>

<td rowspan="1" colSpan="1" >当前 VideoView 绑定了视频数据。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onViewRecycled</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >videoView: 当前播放器 view 容器</td>

<td rowspan="1" colSpan="1" >当前 videoView 被回收。</td>
</tr>
</table>


### 20. 播放器函数

当前播放器的接口函数。

#### ITUIPlayer 类
<table>
<tr>
<td rowspan="1" colSpan="1" >**函数**</td>

<td rowspan="1" colSpan="1" >**返回类型**</td>

<td rowspan="1" colSpan="1" >**参数**</td>

<td rowspan="1" colSpan="1" >**描述**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >prePlay</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >model：视频数据，类型 TUIVideoSource</td>

<td rowspan="1" colSpan="1" >对视频进行预播放，内部会有判重机制。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >resumePlay</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >继续播放当前视频。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >seekTo</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >time：需要跳转的事件点，单位秒，int 类型。</td>

<td rowspan="1" colSpan="1" >跳转到指定播放位置。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >isPlaying</td>

<td rowspan="1" colSpan="1" >boolean</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >当前视频是否正在播放。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >startPlay</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >model：视频数据，类型 TUIVideoSource。</td>

<td rowspan="1" colSpan="1" >播放视频。目前的点播播放器内部会有判断： <br>-  如果传入视频与内部已经有的视频相同： <br>  - a：如果内部已经调用了播放方法，但是还没开始播放，配置为自动播放。<br>  - b：如果视频已经准备完毕，或者处于暂停状态。就调用 resumePlay 。<br>  - c：其他情况，重新播放视频。<br>- 如果传入视频不同，则直接开始播放视频。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >pause</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >暂停播放</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >stop</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >停止播放</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getCurrentPlayTime</td>

<td rowspan="1" colSpan="1" >float</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >获得当前播放时间，单位：秒。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setDisplayView</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >videoView：视频渲染 View，类型TXCloudVideoView。</td>

<td rowspan="1" colSpan="1" >设置播放器要渲染的 View。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setSurface</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >surface：画布</td>

<td rowspan="1" colSpan="1" >设置视频要渲染的 surface。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >addPlayerObserver</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >observer：播放器监听，类型 PlayerObserver。</td>

<td rowspan="1" colSpan="1" >设置播放器监听。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >removePlayerObserver</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >observer：播放器监听，类型 PlayerObserver。</td>

<td rowspan="1" colSpan="1" >移除播放器监听。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreloadFileInfo</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >info：视频信息，类型 TUIFileVideoInfo。</td>

<td rowspan="1" colSpan="1" >设置预加载返回的视频信息，一般 fileId 视频才会调用该方法。预加载把视频设置到播放器后，播放器通过事件，通知给监听者。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setConfig</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >config：视频配置，类型 TXVodPlayConfig。</td>

<td rowspan="1" colSpan="1" >设置视频配置。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setRenderMode</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >renderMode：渲染模式，0：铺满</td>

<td rowspan="1" colSpan="1" >设置平铺模式。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setStartTime</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >startTime：开始播放时间，单位：秒。float 类型</td>

<td rowspan="1" colSpan="1" >设置开始播放时间，在播放前调用有效，只会生效一次。循环播放后会从头开始。</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setLoop</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >isLoop：是否循环</td>

<td rowspan="1" colSpan="1" >设置视频是否循环播放</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setBitrateIndex</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >index：码率角标</td>

<td rowspan="1" colSpan="1" >设置当前码率</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setNetStatusHolder</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >holder：网络状态回调，类型 NetStatusHolder</td>

<td rowspan="1" colSpan="1" >设置当前网络状态打印监听 holder</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >switchResolution</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >resolution：分辨率，即宽 × 高</td>

<td rowspan="1" colSpan="1" >设置改播放器的分辨率，如果有对应分辨率就会进行切换，仅对本次播放生效</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getSupportResolution</td>

<td rowspan="1" colSpan="1" >List<TUIPlayerBitrateItem></td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >获取当前正在播放视频的分辨率列表</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getBitrateIndex</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >获取当前正在播放视频的码率角标</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setResolutionSelector</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >resolutionSelector：分辨率选择器</td>

<td rowspan="1" colSpan="1" >设置当前播放器的分辨率选择器</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setAudioNormalization</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >value: `TXVodConstants.AUDIO_NORMALIZATION_OFF` 关, `TXVodConstants.AUDIO_NORMALIZATION_LOW` 低，`TXVodConstants.AUDIO_NORMALIZATION_STANDARD` 标准，`TXVodConstants.AUDIO_NORMALIZATION_HIGH` 高</td>

<td rowspan="1" colSpan="1" >设置音量均衡，响度范围：-70～0(LUFS)。注意：只对播放器高级版生效</td>
</tr>
</table>


#### TUIPlayerBitrateItem 类
<table>
<tr>
<td rowspan="1" colSpan="1" >**函数**</td>

<td rowspan="1" colSpan="1" >**返回类型**</td>

<td rowspan="1" colSpan="1" >描述</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getIndex</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >当前分辨率的码率角标</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getWidth</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >当前分辨率的视频宽度</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getHeight</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >当前分辨率的视频高度</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getBitrate</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >当前分辨率的视频码率</td>
</tr>
</table>


