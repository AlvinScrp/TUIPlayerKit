## 目录结构说明

本工程包含 Android 版 TUIPlayer Kit 组件和 Demo，主要演示接口如何调用以及最基本的功能。

```xml
├─ Demo(Player) // TUIPlayer 组件 Demo
│
├─ SDK 
│  ├─tuiplayercore-release_x.x.x.aar  // Core 组件
│  ├─tuiplayershortvideo-release_x.x.x.aar  // 短视频组件
```

## 使用指引

- [短视频组件（TUIPlayerShortVideo）使用文档](https://cloud.tencent.com/document/product/881/96685)

  TUIPlayerShortVideo 组件是腾讯云推出的一款性能优异，支持视频极速首帧和流畅滑动，提供优质播放体验的短视频组件。

  - 首帧秒开：首帧时间是短视频类应用核心指标之一，直接影响用户的观看体验。短视频组件通过预播放、预下载、播放器复用和精准流量控制等技术，实现极速首帧、滑动丝滑的优质播放体验，从而提升用户播放量和停留时长。

  - 优异的性能：通过播放器复用和加载策划的优化，在保证极佳流畅度的同时，始终让内存和 CPU 消耗保持在较低的水平。

  - 快速集成：组件对复杂的播放操作进行了封装，提供默认的播放UI，同时支持 FileId 和 Url 播放，可低成本快速集成到您的项目中。

- [画中画组件（TUIPIP）使用文档](https://cloud.tencent.com/document/product/881/96691)

  TUIPlayer 画中组件对系统画中画能力进行了封装，并提供默认 UI 交互，具有易于集成，灵活拓展等优势。

## v1.1.0 更新说明

1. TUIVideoStrategy 支持mediaType、自适应码率配置，预加载大小支持float
2. TUIShortVideoListener移除onReachLast回调，新增onPageChanged、onNetStatus回调
3. layer中可获得renderMode，TUIVideoSource新增extInfo成员变量用于业务扩展，新增isAutoPlay控制是否自动播放
4. 修复其余已知问题


## v1.2.0 更新说明

1. 新增独立分辨率配置以及相关demo
2. layer中可获取当前视频分辨率相关配置
3. 修复其余已知问题

# v1.3.0 更新说明

1. TUIShortVideoView新增getDataManager，用于对短视频列表数据控制
2. ITUIPlayer补充播放器函数
3. TUIBaseLayer中新增onPlayerEvent回调，可获得播放器所有事件通知
4. TUIVideoSource中新增extViewType字段，用于自定义页面类型
5. TUIVideoStrategy新增maxBufferSize配置，用来配置播放中最大缓存
6. TUIShortVideoView新增setUserInputEnabled方法，可以控制列表滑动禁用
7. TUIShortVideoView新增startPlayIndex(index, smoothScroll)重载方法，可以控制是否平滑切换页面
8. TUIVideoStrategy新增preDownloadSize配置，用于指定预下载大小，原先preloadBufferSizeInMB用于指定预播放缓存大小
9. TUIShortVideoView新增setPageScroller方法，用于自定义列表滑动速率
10. TUIVideoStrategy新增resumeModel配置，可配置当前视频列表续播逻辑
11. TUIVideoStrategy新增displayViewFactory配置，可自定义视频图层TXCloudVideoView
12. TUIShortVideoView的setPlayMode设置循环模式，新增MODE_CUSTOM不处理循环模式
13. TUIVideoSource新增setExtInfoAndNotify方法，从TUIKit中获取到的TUIVideoSource可以调用setExtInfoAndNotify来在赋值的同时，通知到layer中
14. TUIBaseLayer中新增onShortVideoDestroyed回调，当整个TUIShortVideoView被调用释放的时候，会触发该回调
15. 优化已知项，修复已知问题
16. ITUIPlayer和TUIVideoStrategy新增setAudioNormalization音量均衡接口
17. ITUIPlayer新增stop重载方法，可传入needClearLastImg来决定在停止视频的时候是否保留最后一帧
18. TUIVideoStrategy新增setEnableAccurateSeek方法，可以用来开启或关闭精准seek