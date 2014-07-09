������Ϊ���ڸߵµ�ͼAndroid SDK���з�װ��ʵ����Marker�ۺ�Ч��������
##ǰ����

- [�ߵ¹ٷ���վ����key](http://id.amap.com/?ref=http%3A%2F%2Fapi.amap.com%2Fkey%2F).
- �Ķ�[�ο��ֲ�](http://api.amap.com/Public/reference/Android%20API%20v2/).
- ���̻���3D��ͼ2.2.1�汾��[��������](https://github.com/amapapi/Android_AMap_3DMapSDK).

##Ч��ͼ���£�

 * ![Screenshot](https://raw.githubusercontent.com/amapapi/Android_ClusterMarker/master/Resources/%E8%81%9A%E5%90%88%E6%95%88%E6%9E%9C.png)
 * ![Screenshot](https://raw.githubusercontent.com/amapapi/Android_ClusterMarker/master/Resources/%E8%81%9A%E5%90%88%E7%82%B9%E7%82%B9%E5%87%BB%E4%BA%8B%E4%BB%B6.png) 

 ##�汾����1.0.0 
 
 �ṩ�˻����ľۺ�Ч�����û������Զ�����Ⱦ��ʽ
 
 ##������Դ��
 
 + [clustermarkerlibrary.jar](https://github.com/amapapi/Android_ClusterMarker/raw/master/Resources/clustermarkerlibrary.jar) (library)
 + [ʵ��Ӧ��](https://github.com/amapapi/Android_ClusterMarker/raw/master/Resources/ClusterMarkerDemo.apk) (apk)
 + ɨһɨ����Ӧ��
 
![Screenshot]( https://raw.githubusercontent.com/amapapi/Android_ClusterMarker/master/Resources/%E4%BA%8C%E7%BB%B4%E7%A0%81.png)  

 ## ʹ�÷�����
 
-  1:��ߵµ�ͼ AndroidSDK���̷�������**[���÷���](http://developer.amap.com/api/android-sdk/guide/project/)**
-  2:�ӿ�ʹ��

* ��ʼ���ۺϺͼ���Ҫ��

``` java
//��ʼ��
		ClusterOverlay clusterOverlay = new ClusterOverlay(mAMap, 
				dp2px(getApplicationContext(),   clusterRadius),
				getApplicationContext());
		for (int i = 0; i < ConstantUtils.latlngs.length; i++) {
			RegionItem regionItem = new RegionItem(ConstantUtils.latlngs[i],
					ConstantUtils.names[i]);
			clusterOverlay.addClusterItem(regionItem);
		}
```		
						
* ���ü���

``` java							
//�����Զ�����ƽӿں;ۺϵ����ӿ�
        clusterOverlay.setClusterRenderer(this);
		clusterOverlay.setOnClusterClickListener(this);
```	

* �Զ�����Ⱦ

``` java
public Drawable getDrawAble(int clusterNum) {
		//���ݾۺϵ���Ŀ��һ���Ծۺϵ���в�ͬ��ʽ����Ⱦ
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

* �ۺϵ����¼�

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