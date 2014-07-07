package com.example.clustermarkerdemo;

import java.util.List;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amp.apis.lib.ClusterClickListener;
import com.amp.apis.lib.ClusterItem;
import com.amp.apis.lib.ClusterOverlay;
import com.amp.apis.lib.ClusterRender;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements ClusterRender,
		OnMapLoadedListener, ClusterClickListener {
	private AMap mAMap;
	private MapView mMapView;
	private TextView infoTextView;
	// Test By Tab ABC
	private int clusterRadius = 80;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化地图控件
		mMapView = (MapView) findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		if (mAMap == null) {
			// 初始化地图
			mAMap = mMapView.getMap();
			mAMap.setOnMapLoadedListener(this);
			// 初始化控件和事件
			infoTextView = (TextView) findViewById(R.id.infoview);

		}
	}

	public Drawable getDrawAble(int clusterNum) {
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

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		 
		ClusterOverlay clusterOverlay = new ClusterOverlay(mAMap, 
				dp2px(getApplicationContext(),   clusterRadius),
				getApplicationContext());
		clusterOverlay.setClusterRenderer(this);
		clusterOverlay.setOnClusterClickListener(this);
		for (int i = 0; i < ConstantUtils.latlngs.length; i++) {
			RegionItem regionItem = new RegionItem(ConstantUtils.latlngs[i],
					ConstantUtils.names[i]);
			clusterOverlay.addClusterItem(regionItem);
		}
	}

	@Override
	public void onClick(Marker marker, List<ClusterItem> clusterItems) {
		String content = "";
		for (ClusterItem clusterItem : clusterItems) {
			RegionItem item = (RegionItem) clusterItem;
			content += item.getTitle() + " ";
		}
		infoTextView.setText(content);

	}

	private Bitmap drawCircle(int radius, int color) {
		Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2,
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
		paint.setColor(color);
		canvas.drawArc(rectF, 0, 360, true, paint);
		return bitmap;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;		
		return (int) (dpValue * scale + 0.5f);
	}

	// -----------------------生命周期必须重写的方法-----------------------------
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

}
