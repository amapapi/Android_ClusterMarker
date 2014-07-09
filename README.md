本工程为基于高德地图Android SDK进行封装，实现了Marker聚合效果的例子
##前述：

- [高德官方网站申请key](http://id.amap.com/?ref=http%3A%2F%2Fapi.amap.com%2Fkey%2F).
- 阅读[参考手册](http://api.amap.com/Public/reference/Android%20API%20v2/).
- 工程基于3D地图2.2.1版本，[基本介绍](https://github.com/amapapi/Android_AMap_3DMapSDK).

##效果图如下：

 * ![Screenshot](https://raw.githubusercontent.com/amapapi/Android_ClusterMarker/master/Resources/%E8%81%9A%E5%90%88%E6%95%88%E6%9E%9C.png)
 * ![Screenshot](https://raw.githubusercontent.com/amapapi/Android_ClusterMarker/master/Resources/%E8%81%9A%E5%90%88%E7%82%B9%E7%82%B9%E5%87%BB%E4%BA%8B%E4%BB%B6.png) 

 ##版本更新1.0.0 
 
 提供了基本的聚合效果，用户可以自定义渲染方式
 
 ##下载资源：
 
 + [clustermarkerlibrary.jar](https://github.com/amapapi/Android_ClusterMarker/raw/master/Resources/clustermarkerlibrary.jar) (library)
 + [实例应用](https://github.com/amapapi/Android_ClusterMarker/raw/master/Resources/ClusterMarkerDemo.apk) (apk)
 + 扫一扫下载应用
 
![Screenshot]( https://raw.githubusercontent.com/amapapi/Android_ClusterMarker/master/Resources/%E4%BA%8C%E7%BB%B4%E7%A0%81.png)  

 ## 使用方法：
 
-  1:搭建高德地图 AndroidSDK工程方法见：**[配置方法](http://developer.amap.com/api/android-sdk/guide/project/)**
-  2:接口使用

* 初始化聚合和加入要素

``` java
//初始化
		ClusterOverlay clusterOverlay = new ClusterOverlay(mAMap, 
				dp2px(getApplicationContext(),   clusterRadius),
				getApplicationContext());
		for (int i = 0; i < ConstantUtils.latlngs.length; i++) {
			RegionItem regionItem = new RegionItem(ConstantUtils.latlngs[i],
					ConstantUtils.names[i]);
			clusterOverlay.addClusterItem(regionItem);
		}
```		
						
* 设置监听

``` java							
//设置自定义绘制接口和聚合点点击接口
        clusterOverlay.setClusterRenderer(this);
		clusterOverlay.setOnClusterClickListener(this);
```	

* 自定义渲染

``` java
public Drawable getDrawAble(int clusterNum) {
		//根据聚合点数目不一样对聚合点进行不同样式的渲染
		int radius = dp2px(getApplicationContext(), clusterRadius);
		if (clusterNum == 1) {
			return getApplication().getResources().getDrawable(
					R.drawable.icon_openmap_mark);
		} else if (clusterNum < 5) {
			BitmapDrawable drawable = new BitmapDrawable(drawCircle(radius,
					Color.argb(159, 210, 154, 6)));
			return drawable;
		} else if (clusterNum < 10) {
			BitmapDrawable drawable = new BitmapDrawable(drawCircle(radius,
					Color.argb(199, 217, 114, 0)));
			return drawable;
		} else {
			BitmapDrawable drawable = new BitmapDrawable(drawCircle(radius,
					Color.argb(235, 215, 66, 2)));
			return drawable;
		}

	}

```	

* 聚合点点击事件

``` java
public void onClick(Marker marker, List<ClusterItem> clusterItems) {
		String content = "";
		for (ClusterItem clusterItem : clusterItems) {
			RegionItem item = (RegionItem) clusterItem;
			content += item.getTitle() + " ";
		}
		infoTextView.setText(content);
	}								
```								