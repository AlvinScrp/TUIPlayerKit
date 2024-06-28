## Component Introduction

TUIPlayerShortVideo is a high-performance short video component launched by Tencent Cloud, which supports fast first frame and smooth sliding, providing users with a high-quality playback experience.
- Instant first frame: The first frame time is one of the core indicators of short video applications, which directly affects the user's viewing experience. The short video component achieves a high-quality playback experience with fast first frame and smooth sliding through technologies such as pre-play, pre-download, player reuse, and precise traffic control, thereby increasing user playback volume and stay time.

- Excellent performance: Through the optimization of player reuse and loading planning, the memory and CPU consumption are always kept at a low level while ensuring excellent smoothness.

- Quick integration: The component encapsulates complex playback operations, provides default playback UI, and supports FileId and URL playback. It can be quickly integrated into your project at low cost.

## Comparison of Effects

The following video demonstrates the difference between short videos before and after optimization under the same environment.
- Before optimization, users can clearly feel the stuttering of the video when it starts playing.

- After optimization, users can experience seamless playback with an average start-up time of 10ms-30ms.

<table>
<tr>
<td rowspan="1" colSpan="1" >Short Video Before Optimization</td><td rowspan="1" colSpan="1" >Short Video After Optimization</td>
</tr><tr>
<td rowspan="1" colSpan="1" > <a href="https://1251520898.vod2.myqcloud.com/dc1d8ff3vodtranscq1251520898/e17235cb3270835009944665195/drm/v.f100270.m3u8?sign=178fb528eae0d51c2926282baee186a1&t=64fb7426&us=4016804">Before Optimization Video</a></td>
<td rowspan="1" colSpan="1" ><a href="https://1251520898.vod2.myqcloud.com/dc1d8ff3vodtranscq1251520898/e85d6e263270835009944951390/drm/v.f100220.m3u8?sign=6500af8b0e44fbb028580e051c3a682d&t=64fb7426&us=974264">After Optimization Video</a></td>
</tr>
</table>


## Download TUIPlayerKit

TUIPlayerKit SDK and Demo can be downloaded by [clicking here](https://mediacloud-76607.gzc.vod.tencent-cloud.com/TUIPlayerKit/download/latest/TUIPlayerKit_Android_latest.zip).

## Integrate TUIPlayerShortVideo Component

### Environment Preparation

Minimum Android system version requirement: Android SDK >= 19

Add dependencies required for short video:
``` xml
// If you are using the professional version of the SDK, use: api 'com.tencent.liteav:LiteAVSDK_Professional:latest.release'
api 'com.tencent.liteav:LiteAVSDK_Player:latest.release'
implementation (name:'tuiplayercore-release_x.x.x', ext:'aar')
implementation (name:'tuiplayershortvideo-release_x.x.x', ext:'aar')
implementation 'androidx.appcompat:appcompat:1.0.0'
implementation 'androidx.viewpager2:viewpager2:1.0.0'
```

> **Note:**
> 

> `x.x.x` is the version number, and the version numbers of the two aar files must be consistent.
> 


Permissions required by the SDK:
``` typescript
        
<uses-permission android:name="android.permission.INTERNET" />
```

Setting ProGuard Rules:

In the proguard-rules.pro file, add the relevant classes to the non-obfuscation list:
``` xml
-keep class com.tencent.** { *; }
```

### Apply for Advanced Player License

To use the TUIPlayer Kit component, you need to use the Advanced Player License for mobile devices. You can refer to the [Mobile Player License](https://write.woa.com/document/96912869201248256) guide to obtain it. If you have obtained the corresponding License, you can go to [Tencent Cloud Vcube Console > License Management > Mobile License](https://console.cloud.tencent.com/vcube/mobile) to get the corresponding LicenseURL and LicenseKey. If you have not applied for the Advanced Player License for mobile devices, video playback failures, black screens and other phenomena may occur.

### Set License

The short video component needs to set the License to use it.
``` java
TUIPlayerConfig config = new TUIPlayerConfig.Builder()
        .enableLog(true)
        .licenseKey("Your license key")
        .licenseUrl("Your license url")
        .build();
TUIPlayerCore.init(context, config);
```

### Add UI Components

In the layout file, add the short video UI component.
``` xml
<com.tencent.qcloud.tuiplayer.shortvideo.ui.view.TUIShortVideoView
    android:id="@+id/my_video_view"
    android:layout_height="match_parent"
    android:layout_width="match_parent"/>
```

### Configure the Component

Configure the short video component and fill in the video data.
``` java
mSuperShortVideoView.setActivityLifecycle(getLifecycle());
// set strategy of shortVideo
mSuperShortVideoView.setStrategy(new TUIVideoStrategy.Builder().build());
mSuperShortVideoView.setListener(new TUIShortVideoListener() {
  @Override
  public void onReachedLast() {
      // append next page data
      mSuperShortVideoView.appendModels(data);
  }

  @Override
  public void onCreateItemLayer(TUILayerManger layerManger, int viewType) {
      // add your custom layers
      layerManger.addLayer(new TUICoverLayer());
  }
 });
 mSuperShortVideoView.setModels(shortVideoData);
```

## Custom Layers

### Introduction

In Android TUI short video, a layer management method is used to provide custom UI capabilities to each short video page. Through the layer manager, the interaction between video UI and video events can be better handled.

The display and hiding of the layers will directly add and remove the View, without causing excessive rendering of the interface.

The layers will determine the display order of the interface according to the order of addition, with the first added layer at the bottom and the last added layer at the top.

### Create Custom Layers

#### 1. Create Custom Layers

To create a custom layer, you need to inherit TUIBaseLayer and then implement your own layer.

Taking the video details layer as an example, you need to implement the createView and tag methods. createView is the method for creating the layer view, and tag is a string tag used to distinguish the layer.
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

createView creates a View and returns it. Here you can use LayoutInflater to load the layout from XML, or you can create the layout directly with code.

#### 2. Display the Layout

After the View is created, it needs to be displayed at the appropriate time. TUIBaseLayer provides rich event callbacks. For more information, please refer to [Layer Callbacks](https://write.woa.com/#eacb47cb-9355-4b47-8ae0-08d48ba0d6fa).

The video details layer can display the layout when it gets the data. Therefore, the layer is displayed in onBindData.
``` java
@Override
public void onBindData(TUIVideoSource videoSource) {
    show();
}
```

#### 3. Operate a Single View

You can also operate a single View in other events, such as showing and hiding the pause button, and the playback progress callback, as shown in the following code.
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

#### 4. Control the Player

In addition to receiving events from the player, you can also control the player. For example, call the player to jump to a specific progress.
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

Currently in TUIBaseLayer, you can get the VideoView object of the current page through getVideoView, get the playback controller of the current video through getPlayerController (only when the current page is the video being played in the current short video list), and get the current player object through getPlayer. Due to the reuse mechanism of the short video item component, these three objects may get empty objects when obtained and need to be checked for null.

It is recommended to use getPlayerController to operate on the video object.

#### 5. Release When the Layer is Detached from the Layer Manager

When the layer is detached from the layer manager, some release operations need to be performed to prevent external objects from holding the layer and causing memory leaks. For example, set the listener of the seekBar to null in unBindLayerManager.
``` java
@Override
public void unBindLayerManager() {
    super.unBindLayerManager();
    if (null != mSeekBar) {
        mSeekBar.setListener(null);
    }
}
```

#### 6. Listen to the Controller

If you need to listen to whether the current page is the current playing video, you can listen to the controller. See the display and hiding of the cover image in the following code:
``` java
@Override
public void onControllerUnBind(TUIPlayerController controller) {
    super.onControllerUnBind(controller);
    // show cover when unbind
    show();
}
```

The above code shows the cover image when the controller is unbound and does not need to play.

#### 7. Get Video Information through onRecFileVideoInfo Callback

There is no video information for FileId playback, which can be obtained through the onRecFileVideoInfo callback, as shown in the following code:
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

This method will be called when only fileID playback is used. It returns video URL links, cover images, duration, sprite images, and other information.

It is recommended to play short videos by passing URL into the short video component as much as possible, and assign the cover image URL in advance. This can increase the performance of short video loading.

#### 8. Determine Whether the First Frame of the Video Has Arrived through onRcvFirstIframe

Use it as shown in the following code:
``` java
@Override
public void onRcvFirstIframe() {
    hidden();
}
```

For example, in the cover image scene, you need to hide the cover image after receiving the first frame event.

### Manage Layers

After integrating the short video component TUIShortVideoView, using TUIShortVideoView to set listeners will call the onCreateItemLayer item creation method at the appropriate time to add or manage custom layers.
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

onCreateItemLayer has two parameters, layerManger is the layer manager, which can add, remove, and query layers. The adding method is shown in the above figure. viewType represents the video type of the current page, 1 represents VOD, and 2 represents live streaming.

If you don't need a layer, you can remove it.
``` java
layerManger.removeLayer(layer);
```

If you need to get the layer level to perform layer interaction operations, you can use the following method to get the layer level:
``` java
layerManger.indexOfLayer(layer);
```

## Short Video API Usage

### 1. Set Lifecycle Listener

Used to control the lifecycle of TUIShortVideoView, internally pause, play, and destroy the list video according to the lifeCycle status.
``` java
mSuperShortVideoView.setActivityLifecycle(getLifecycle());
```

### 2. Set Short Video Listener

Used to listen to events of TUIShortVideoView, including the timing of loading pagination data, and the callback when creating a page (you can add layers in this callback).
``` java
mSuperShortVideoView.setListener(new TUIShortVideoListener() {
        @Override
        public void onReachedLast() {
                loadMore(false);
        }

        @Override
        public void onCreateItemLayer(TUILayerManger layerManger, int viewType) {
            layerManger.addLayer(new TUICoverLayer());
            layerManger.addLayer(new TUIVideoInfoLayer());
            layerManger.addLayer(new TUILoadingLayer());
            layerManger.addLayer(new TUIErrorLayer());
        }
    });
```

### 3. Set Video Playback Strategy

Set various strategies during video playback.

#### Strategy TUIVideoStrategy parameters

Need to be constructed using Builder.
<table>
<tr>
<td rowspan="1" colSpan="1" >**Function**</td>

<td rowspan="1" colSpan="1" >**Description**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreloadCount</td>

<td rowspan="1" colSpan="1" >Set the maximum number of concurrent preloads, default is 3</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreLoadBufferSize</td>

<td rowspan="1" colSpan="1" >Set the size of the preload cache, the default is 1MB, in MB</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setIsPreVideoResume</td>

<td rowspan="1" colSpan="1" >Whether to continue the previous playback status when returning to the previous video, the default is false</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreferredResolution</td>

<td rowspan="1" colSpan="1" >Set the preferred resolution for video playback, the default is 720 x 1280</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setProgressInterval</td>

<td rowspan="1" colSpan="1" >Set the playback progress callback interval, the default is 500 milliseconds, in milliseconds</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setRenderMode</td>

<td rowspan="1" colSpan="1" >Set the rendering tiling mode, the default is 0. In liteavPlayer, 0 means full screen, and 1 means rendering according to the actual proportion of the video, which may have black edges.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setExtInfo</td>

<td rowspan="1" colSpan="1" >Set additional information</td>
</tr>

<tr> 
<td rowspan="1" colSpan="1" >setMediaType</td> 

<td rowspan="1" colSpan="1" >When the media type of the playback is known in advance, this interface can be used to set the media type, reducing the internal playback type detection of the player SDK and improving the start-up speed.</td> 
</tr>

<tr> 
<td rowspan="1" colSpan="1" >enableAutoBitrate</td>

<td rowspan="1" colSpan="1" >Sets whether to enable bitrate adaptation or not.</td> 
</tr>
</table>


### 4. Fill Data

Fill data into TUIShortVideoView:
``` java
mSuperShortVideoView.setModels(shortVideoBeanList);
```

Append data:
``` java
mSuperShortVideoView.appendModels(shortVideoBeanList);
```

#### TUIVideoSource class
<table>
<tr>
<td rowspan="1" colSpan="1" >**Parameter**</td>

<td rowspan="1" colSpan="1" >**Type**</td>

<td rowspan="1" colSpan="1" >**Description**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >videoURL</td>

<td rowspan="1" colSpan="1" >String</td>

<td rowspan="1" colSpan="1" >Video link, it is recommended to fill in this field, which will speed up the preload speed</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >coverPictureUrl</td>

<td rowspan="1" colSpan="1" >String</td>

<td rowspan="1" colSpan="1" >Video cover, will be called back to the layer, and will be processed by the customer</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >duration</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >Video duration, in seconds, will be called back to the layer</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >appid</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >Video appId</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >fileid</td>

<td rowspan="1" colSpan="1" >String</td>

<td rowspan="1" colSpan="1" >Video fileId</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >pSign</td>

<td rowspan="1" colSpan="1" >String</td>

<td rowspan="1" colSpan="1" >Video encryption pSign</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >extInfo</td>

<td rowspan="1" colSpan="1" >Map</td>

<td rowspan="1" colSpan="1" >Used for business to expand additional parameters on their own.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setVideoConfig</td>

<td rowspan="1" colSpan="1" >TUIPlayerVideoConfig</td>

<td rowspan="1" colSpan="1" >Independent video configuration</td>
</tr>
</table>

#### TUIPlayerVideoConfig Class
<table>
<tr>
<td rowspan="1" colSpan="1" >**Parameter**</td>

<td rowspan="1" colSpan="1" >**Type**</td>

<td rowspan="1" colSpan="1" >**Description**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreloadBufferSizeInMB</td>

<td rowspan="1" colSpan="1" >float</td>

<td rowspan="1" colSpan="1" >Set the independent preload buffer size for the video, optional.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreferredResolution</td>

<td rowspan="1" colSpan="1" >long</td>

<td rowspan="1" colSpan="1" >Set the independent start playback and preload resolution for the video, optional.</td>
</tr>
</table>


### 5. Get the Currently Playing Video Resource

Get the currently playing video resource, use it as shown in the following code:
``` java
mSuperShortVideoView.getCurrentModel()
```

### 6. Pause

Pause the currently playing video.
``` java
mSuperShortVideoView.pause()
```

### 7. Play from a Specified Position

Play from a specified position, use it as shown in the following code:
``` java
mSuperShortVideoView.startPlayIndex()
```

### 8. Destroy the Control

Destroy the control and resources.
``` java
mSuperShortVideoView.release()
```

### 9. Resume the Current Video

Resume the current video, use it as shown in the following code:
``` java
mSuperShortVideoView.resume()
```

### 10. Add Layers

TUI Short Video can add custom UI on the short video playback interface by adding layers. When the onCreateItemLayer callback is called, layers can be added and managed through the LayerManager provided by the method. You can customize the required layers by inheriting TUIBaseLayer. The layer will float above the video VideoView.

The display and hiding of the layers are operated by adding and removing Views, and there is no problem of excessive rendering when a large number of layers are displayed at the same time.

#### onCreateItemLayer parameters
<table>
<tr>
<td rowspan="1" colSpan="1" >**Parameter**</td>

<td rowspan="1" colSpan="1" >**Type**</td>

<td rowspan="1" colSpan="1" >**Description**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >layerManger</td>

<td rowspan="1" colSpan="1" >TUILayerManger</td>

<td rowspan="1" colSpan="1" >Layer management</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >viewType</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >Current video playback type<br>- 1: VOD<br>- 2: Live</td>
</tr>
</table>

``` java
layerManger.addLayer(new TUICoverLayer());
layerManger.addLayer(new TUIVideoInfoLayer());
layerManger.addLayer(new TUILoadingLayer());
layerManger.addLayer(new TUIErrorLayer());
```

### 11. Real-time resolution switching

TUI short video can switch the current video resolution and global video resolution in real-time, with the following interface:
``` java
mSuperShortVideoView.switchResolution(720 * 1080, TUIConstants.TUIResolutionType.CURRENT);
```

In addition to passing `TUIResolutionType` for switchType, you can also directly specify the index of the video to be switched to change the video resolution.

The meanings of switchType are as follows:
<table>
<tr>
<td rowspan="1" colSpan="1" >Parameter</td>

<td rowspan="1" colSpan="1" >Description</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >GLOBAL</td>

<td rowspan="1" colSpan="1" >Set global resolution</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >CURRENT</td>

<td rowspan="1" colSpan="1" >Set current video resolution</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >Other values greater than or equal to 0</td>

<td rowspan="1" colSpan="1" >Set specified resolution</td>
</tr>
</table>

**Currently, the priority of resolution is that the setting priority of the current video resolution is higher than the global resolution.**

### 12. Pause and resume preloading

TUI short video can pause and resume preloading tasks in real-time
``` java
// pause all preload
mSuperShortVideoView.pausePreload();
// start preload from current video index
mSuperShortVideoView.resumePreload();
```

When calling to resume preloading, it will continue preloading from the current video onwards.


## TUIPlayerCore Interface Usage

### 1. Configure License

To use the TUI component, you need to configure the corresponding premium License, as shown below:
``` java
TUIPlayerConfig config = new TUIPlayerConfig.Builder()
        .enableLog(true)
        .licenseKey(LICENCE_KEY)
        .licenseUrl(LICENCE_URL)
        .build();
TUIPlayerCore.init(getApplicationContext(), config);
```

### 2. Create PlayerManager

Create PlayerManager, the video data processor, which includes player destruction and creation, pre-play, pre-load, play control, binding, and other operations.
``` java
mPlayerManager = new TUIPlayerManager(getContext(), listener);
```

#### playerManager Constructor Parameters
<table>
<tr>
<td rowspan="1" colSpan="1" >**Parameter**</td>

<td rowspan="1" colSpan="1" >**Type**</td>

<td rowspan="1" colSpan="1" >**Description**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >context</td>

<td rowspan="1" colSpan="1" >Context</td>

<td rowspan="1" colSpan="1" >Context</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >bridge</td>

<td rowspan="1" colSpan="1" >PlayerBridge</td>

<td rowspan="1" colSpan="1" >Communication bridge of playerManager</td>
</tr>
</table>


#### PlayerBridge Class
<table>
<tr>
<td rowspan="1" colSpan="1" >**Function**</td>

<td rowspan="1" colSpan="1" >**Return Type**</td>

<td rowspan="1" colSpan="1" >**Description**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getCurrentScrollState</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >Current video list scroll state.<br>- 1: idle state<br>- 2: dragging<br>- 3: inertia sliding state after dragging</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getCurrentPlayingIndex</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >Current page index</td>
</tr>
</table>


### 3. Update Strategy

Update the strategy for video preloading, playback, pre-play, etc. of PlayerManager.
``` java
mPlayerManager.updateStrategy(strategy);
```

The strategy parameter is consistent with that of the short video.

### 4. Set Data

Set the data in playerManager, which will clear all previous data.
``` java
mPlayerManager.setModels(dataSource);
```

### 5. Append Data

Append data to PlayerManager, usually used for pagination loading.
``` java
mPlayerManager.appendModels(models);
```

### 6. Preload and Bind VideoView

When switching the currently playing page, it is necessary to bind the current VideoView to load the video, as shown below:
``` java
TUIBaseVideoView itemView = findVideoViewByPos(position);
if (null != itemView) {
    boolean isUpDirection = mLastPosition > position;
    TUIPlayerLog.v(TAG, "start bind and play view " + position + ", isUpDirection:" + isUpDirection);
    mPlayerManager.bindVideoView(itemView, isUpDirection);
    mPlayerManager.startCurrent();
}
```

### 7. Preload and Prerender VideoView

When playing the current video, you can pre-render the View of the next video in advance to improve the start-up speed of the next video, as shown below:
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

### 8. Get the Currently Playing Video

Get the model object of the currently playing video.
``` java
mPlayerManager.getCurrentModel();
```

### 9. Pause the Current Video

Pause the video currently playing in the video list.
``` java
mPlayerManager.pauseCurrent();
```

### 10. Playback Status

Determine if the current video is playing.
``` java
mPlayerManager.isCurrentPlaying();
```

### 11. Resume Playback

Resume playing the currently playing video.
``` java
mPlayerManager.resumeCurrent();
```

### 12. Switch Playback Status

Toggle the playback and pause of the current video according to the current playback status.
``` java
mPlayController.togglePlay();
```

### 13. Destroy Manager

Release Manager resources. If the lifecycle is set in the short video, this method will be called in the VideoView of the short video.
``` java
mPlayerManager.releasePlayers();
```

### 14. Add Layer

Add a layer to the layer manager.
``` java
layerManger.addLayer(new TUICoverLayer());
```

### 15. Remove a Specific Layer

Remove the layer in the layer manager.
``` java
layerManger.removeLayer(layer);
```

### 16. Remove All Layers

Remove all layers in the layer manager.
``` java
layerManger.removeAllLayer();
```

### 17. Get the Current Layer Level

Pass in the layer to get the current layer level, which can be used to determine the order of overlapping during layer display and the order of touch event transmission.
``` java
layerManger.indexOfLayer(layer);
```

### 18. Bind VideoView to Layer

Bind the layer manager to the VideoView. After binding, the layer manager will receive events during video playback.
``` java
layerManger.bindVideoView(itemView);
```

### 19. Layer Callbacks

After inheriting TUIBaseLayer, you can receive video playback callbacks according to your functional needs. Currently, the callback functions are as follows:

#### TUIBaseLayer Class
<table>
<tr>
<td rowspan="1" colSpan="1" >**Function**</td>

<td rowspan="1" colSpan="1" >**Return Type**</td>

<td rowspan="1" colSpan="1" >**Parameters**</td>

<td rowspan="1" colSpan="1" >**Description**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >isShowing</td>

<td rowspan="1" colSpan="1" >boolean</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Indicates whether the current layer is being displayed</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >createView</td>

<td rowspan="1" colSpan="1" >View</td>

<td rowspan="1" colSpan="1" >parent: layer container</td>

<td rowspan="1" colSpan="1" >An abstract method that needs to be implemented by yourself to create the View of the layer.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >tag</td>

<td rowspan="1" colSpan="1" >String</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >The tag of the layer, used to distinguish different layers.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >unBindLayerManager</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >The layer is unbound from the manager, usually occurs when the layer is removed.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >show</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Displays the current layer.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >hidden</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Hides the current layer.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getView</td>

<td rowspan="1" colSpan="1" >T extends View</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Gets the View of the current layer.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getVideoView</td>

<td rowspan="1" colSpan="1" >TUIBaseVideoView</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Gets the current VideoView. If the layerManager and VideoView are not yet bound, it will return null.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getPlayerController</td>

<td rowspan="1" colSpan="1" >TUIPlayerController</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Gets the current playback controller. If it has not been bound to the controller yet, it will return null.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getPlayer</td>

<td rowspan="1" colSpan="1" >ITUIPlayer</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Gets the current player. If it has not been bound to the controller yet, it will return null.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getRenderMode</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Get the current player screen rendering fill mode.</td>
</tr>
</table>


#### PlayerObserver Class
<table>
<tr>
<td rowspan="1" colSpan="1" >**Function**</td>

<td rowspan="1" colSpan="1" >**Return Type**</td>

<td rowspan="1" colSpan="1" >**Parameters**</td>

<td rowspan="1" colSpan="1" >**Description**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayPrepare</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Video preparation is complete.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayBegin</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >The video starts playing.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayPause</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >The video is paused.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayStop</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >The video is stopped.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayLoading</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >The video starts loading.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayLoadingEnd</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >The video loading is complete.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onPlayProgress</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >- current: The current video playback progress, in milliseconds, of type long.<br>- duration: The current total duration of the video, in milliseconds, of type long.<br>- playable: The current playable duration of the video, in milliseconds, of type long.</td>

<td rowspan="1" colSpan="1" >Video playback progress.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onSeek</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >position: The video progress to jump to. Unit is second, of type int.</td>

<td rowspan="1" colSpan="1" >The video progress jumps.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onError</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >code: Video error code.<br>message: Error description.</td>

<td rowspan="1" colSpan="1" >Video playback error.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onRcvFirstIframe</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Received the first frame event.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onRcvTrackInformation</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >infoList: Video track.</td>

<td rowspan="1" colSpan="1" >Received video track information.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onRcvSubTitleTrackInformation</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >infoList: Video subtitle information.</td>

<td rowspan="1" colSpan="1" >Received video subtitle information.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onRecFileVideoInfo</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >params: Video file information.</td>

<td rowspan="1" colSpan="1" >Received video file information. This callback is usually triggered only when playing with fileId.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onResolutionChanged</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >width: Video width<br>height: Video height</td>

<td rowspan="1" colSpan="1" >Current video resolution changes</td>
</tr>
</table>


#### TUIVideoViewListener Class
<table>
<tr>
<td rowspan="1" colSpan="1" >**Function**</td>

<td rowspan="1" colSpan="1" >**Return Type**</td>

<td rowspan="1" colSpan="1" >**Parameters**</td>

<td rowspan="1" colSpan="1" >**Description**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onControllerBind</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >controller: The current video playback controller.</td>

<td rowspan="1" colSpan="1" >The current VideoView is bound to the playback controller, indicating that the current video becomes the one being played in the list.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onControllerUnBind</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >controller: The current video playback controller.</td>

<td rowspan="1" colSpan="1" >The VideoView is unbound from the playback controller, usually indicating that the page has slid out of the screen.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onBindData</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >videoSource: Video data.</td>

<td rowspan="1" colSpan="1" >The current VideoView is bound to the video data.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >onViewRecycled</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >videoView: Current player view container</td>

<td rowspan="1" colSpan="1" >The current videoView is recycled.</td>
</tr>
</table>


### 20. Player Functions

The interface functions of the current player.

#### ITUIPlayer Class
<table>
<tr>
<td rowspan="1" colSpan="1" >**Function**</td>

<td rowspan="1" colSpan="1" >**Return Type**</td>

<td rowspan="1" colSpan="1" >**Parameters**</td>

<td rowspan="1" colSpan="1" >**Description**</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >prePlay</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >model: Video data, of type TUIVideoSource.</td>

<td rowspan="1" colSpan="1" >Pre-play the video, with a de-duplication mechanism inside.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >resumePlay</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Resume playing the current video.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >seekTo</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >time: The time point to jump to, unit is second, of type int.</td>

<td rowspan="1" colSpan="1" >Jump to the specified playback position.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >isPlaying</td>

<td rowspan="1" colSpan="1" >boolean</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Whether the current video is playing.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >startPlay</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >model: Video data, of type TUIVideoSource.</td>

<td rowspan="1" colSpan="1" >Play the video. The current VOD player has a judgment mechanism inside: <br>- If the passed-in video is the same as the one already in the player: <br>  - a: If the player method has already been called internally but has not started playing, it is set to autoplay.<br>  - b: If the video has already been prepared or is in pause state, call resumePlay.<br>  - c: Other situations, replay the video.<br>- If the passed-in video is different, play the video directly.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >pause</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Pause the playback.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >stop</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Stop the playback.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getCurrentPlayTime</td>

<td rowspan="1" colSpan="1" >float</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Get the current playback time, in seconds.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setDisplayView</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >videoView: The video rendering View, of type TXCloudVideoView.</td>

<td rowspan="1" colSpan="1" >Set the View that the player should render.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setSurface</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >surface: The canvas.</td>

<td rowspan="1" colSpan="1" >Set the surface on which the video should be rendered.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >addPlayerObserver</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >observer: The player listener, of type PlayerObserver.</td>

<td rowspan="1" colSpan="1" >Set the player listener.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >removePlayerObserver</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >observer: The player listener, of type PlayerObserver.</td>

<td rowspan="1" colSpan="1" >Remove the player listener.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setPreloadFileInfo</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >info: Video information, of type TUIFileVideoInfo.</td>

<td rowspan="1" colSpan="1" >Set the video information returned by preloading. This method is usually called for fileId videos. After the video is set to the player by preloading, the player notifies the listener through an event.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setConfig</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >config: Video configuration, of type TXVodPlayConfig.</td>

<td rowspan="1" colSpan="1" >Set the video configuration.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setRenderMode</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >renderMode: The rendering mode, 0: stretch.</td>

<td rowspan="1" colSpan="1" >Set the tiling mode.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setStartTime</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >startTime: The start time of playback, in seconds, of type float.</td>

<td rowspan="1" colSpan="1" >Set the start time of playback, which is only effective before playback and only once. After looping, it will start from the beginning.</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setLoop</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >isLoop: Whether to loop</td>

<td rowspan="1" colSpan="1" >Set whether the video loops playback</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setBitrateIndex</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >index: Bitrate index</td>

<td rowspan="1" colSpan="1" >Set the current bitrate</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setNetStatusHolder</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >holder: Network status callback, type NetStatusHolder</td>

<td rowspan="1" colSpan="1" >Set the current network status print listener holder</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >switchResolution</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >resolution: Resolution, i.e., width × height</td>

<td rowspan="1" colSpan="1" >Set the resolution of this player, if there is a corresponding resolution, it will switch, only effective for this playback</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getSupportResolution</td>

<td rowspan="1" colSpan="1" >List<TUIPlayerBitrateItem></td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Get the resolution list of the currently playing video</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getBitrateIndex</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >-</td>

<td rowspan="1" colSpan="1" >Get the bitrate index of the currently playing video</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >setResolutionSelector</td>

<td rowspan="1" colSpan="1" >void</td>

<td rowspan="1" colSpan="1" >resolutionSelector: Resolution selector</td>

<td rowspan="1" colSpan="1" >Set the resolution selector of the current player</td>
</tr>

<tr>
<td rowspan="1" colspan="1">setAudioNormalization</td>

<td rowspan="1" colspan="1">void</td>

<td rowspan="1" colspan="1">value: `TXVodConstants.AUDIO_NORMALIZATION_OFF` off, `TXVodConstants.AUDIO_NORMALIZATION_LOW` low, `TXVodConstants.AUDIO_NORMALIZATION_STANDARD` standard, `TXVodConstants.AUDIO_NORMALIZATION_HIGH` high</td>

<td rowspan="1" colspan="1">Set audio normalization, loudness range: -70~0 (LUFS). Note: Only effective for the advanced version of the player</td>
</tr>
</table>

#### TUIPlayerBitrateItem Class
<table>
<tr>
<td rowspan="1" colSpan="1" >**Function**</td>

<td rowspan="1" colSpan="1" >**Return Type**</td>

<td rowspan="1" colSpan="1" >Description</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getIndex</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >Bitrate index of the current resolution</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getWidth</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >Video width of the current resolution</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getHeight</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >Video height of the current resolution</td>
</tr>

<tr>
<td rowspan="1" colSpan="1" >getBitrate</td>

<td rowspan="1" colSpan="1" >int</td>

<td rowspan="1" colSpan="1" >Video bitrate of the current resolution</td>
</tr>
</table>

