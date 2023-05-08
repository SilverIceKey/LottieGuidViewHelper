# Lottie引导界面(LottieGuidViewHelper)

说明：基于Lottie实现不操作时循环播放动画，操作时对手势进行判断来设置动画进度，当手势完成度到达预设值时松手继续播放动画直到结束并开始下一份引导或完成引导。

使用案例：

创建引导实体实现IGuidData接口，接口包括以下实现：

| 接口名称        | 接口说明                                                     | 接口返回    |
| --------------- | ------------------------------------------------------------ | ----------- |
| viewAnimation   | 说明动画，会一直播放，在操作时会根据进度变化的动画           | InputStream |
| fingerAnimation | 手势动画，不操作时会一直播放，操作时会隐藏                   | InputStream |
| clearProgress   | 通过当前引导需要的进度值0-1                                  | Float       |
| progressUpdate  | 操作时计算进度的方法，会传入ACTION_DOWN时的坐标和ACTION_MOVE时的坐标，以及MotionEvent，计算完进度之后返回进度进度值0-1 | Float       |

在需要显示引导的Activity中初始化

```kotlin
val guidViewHelper:GuidViewHelper?=null
fun initGuidViewHelper(){
	guidViewHelper = GuidViewHelper(this)
    	.setParentView(//动画的父容器，最后置于顶层)
    	.onFinish {
    	    //引导完成之后
    	}
    	.onNotClear {
			//引导未通过
    	}
    	.setGuidDataList(//传入引导实体)
}
```

重写onTouchEvent或者其他有MotionEvent的方法并在其中调用

```kotlin
guidViewHelper.onTouchEvent(event)
```

最后在需要开始的地方调用

`guidViewHelper.start()`

到此引导的初始化和启动就完成了

